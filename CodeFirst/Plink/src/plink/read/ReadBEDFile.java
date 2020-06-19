/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.read;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import plink.PlinkFileException;
import plink.util.Allele;
import plink.util.SNPByIndividualMatrix;
import plink.util.demography.Individual;
import plink.util.SNPPlink;
import plink.util.SNPGenotypeByteArray;
import read.ReadFile;

/**
 *
 * @author Administrator
 */
public class ReadBEDFile implements ReadSNPGenotypeArrayFile{

    public ReadBEDFile(String fileName) throws FileNotFoundException, IOException, PlinkFileException {
        bedB = new BufferedInputStream(new FileInputStream(new File(fileName + ".bed")));
        bimF = new ReadFile(new File(fileName + ".bim"));
// read the table with the individual names
//        checkNumberOfSNPs(new File(fileName + ".bim"));
        checkHeader();
        checkMode();
        individuals = ReadBEDFile.this.getIndividuals(new File(fileName + ".fam"));
        this.fileName = fileName;
    }
    
    protected ReadFile bimF;
    protected Individual[] individuals;
    protected byte header1 = 108;
    protected byte header2 = 27;
    protected BufferedInputStream bedB;
//    protected int numberSNPs = 0;
    protected String fileName;

    
    public void open_connection_again()throws FileNotFoundException, IOException, PlinkFileException 
    {
        bedB = new BufferedInputStream(new FileInputStream(new File(fileName + ".bed")));
        bimF = new ReadFile(new File(fileName + ".bim"));
// read the table with the individual names
        checkHeader();
        checkMode();        
    }
    
    public String getFileName() {
        return fileName;
    }
    
    @Override
    public HashMap<String,ArrayList<Integer>> classify_individuals_by_family_id()
    {
        HashMap<String, ArrayList<Integer>> classify_individuals_by_family_id = new HashMap<>();
        for(int i=0;i<individuals.length;i++)
        {
            try
            {
                classify_individuals_by_family_id.get(individuals[i].getFamilyName()).add(i);
            }catch(NullPointerException tok)
            {
                ArrayList<Integer> id = new ArrayList<>();
                id.add(i);
                classify_individuals_by_family_id.put(individuals[i].getFamilyName(), id);
            }
        }
        
        return classify_individuals_by_family_id;
    }

//    private void checkNumberOfSNPs(File bim) {
//        try {
//            ReadFile bimFile = new ReadFile(bim);
//            while (bimFile.readRow() != null) {
//                numberSNPs++;
//            }
//            bimFile.close();
//        } catch (IOException tok) {
//            System.out.println("File not found " + bim.getName());
//            System.exit(0);
//        }
//    }
//
//    public int getNumberSNPs() {
//        return numberSNPs;
//    }

    @Override
    public Individual[] getIndividuals() {
        return individuals;
    }

    public Individual[] getIndividuals(File fam) throws IOException {
        ReadFile famF = new ReadFile(fam);
        ArrayList<Individual> inds = new ArrayList<>();
        String line = famF.readRow();
        while (line != null) {
            String[] split = line.split("\\s");
            Individual individual = new Individual(split[0], split[1]);
            individual.setPaternalId(split[2]);
            individual.setMaternalId(split[3]);
            individual.setSex(Integer.parseInt(split[4]));
            try {
                individual.setPhenotype(Integer.parseInt(split[5]));
            } catch (NumberFormatException tok) {
                individual.setPhenotype(-9);
            }
            // Get the ids of the related individuals
            for (int e = 6; e < split.length; e++) {
                individual.addRelatedIndividual(Integer.parseInt(split[e]));
            }
            inds.add(individual);
            line = famF.readRow();
        }
        Individual[] individual = new Individual[inds.size()];
        inds.toArray(individual);
        famF.close();
        return individual;
    }

    protected void checkHeader() throws IOException, PlinkFileException {
        if (getNextGenotype() != header1) {
            throw new PlinkFileException(PlinkFileException.INCORRECT_HEADER);
        }
        if (getNextGenotype() != header2) {
            throw new PlinkFileException(PlinkFileException.INCORRECT_HEADER);
        }
    }

    protected void checkMode() throws IOException, PlinkFileException {
        if (getNextGenotype() != 1) {
            throw new PlinkFileException(PlinkFileException.INCORRECT_MODE);
        }
    }

    protected int getNextGenotype() throws IOException {
        int b = bedB.read();
        return b;
    }

    protected SNPPlink nextSNP() throws Exception{
        String line = bimF.readRow();
        if (line != null) {
            String[] split = line.split("\\s");
            Allele a = Allele.getAllele(split[4].charAt(0));
            Allele b = Allele.getAllele(split[5].charAt(0));
            byte chr;
            try {
// Chromosome is in non numeric format
                chr = Byte.parseByte(split[0]);
            } catch (NumberFormatException tok) {
                chr = -1;
            }
            int position = Integer.parseInt(split[3]);
            String rsId = split[1];
            SNPPlink snp = new SNPPlink(rsId, chr, position, a, b);
            for (int e = 6; e < split.length; e++) {
                snp.addDatabase(split[e]);
            }
            return snp;
        }
        return null;
    }

    protected SNPPlink nextSNP(FunctionToExtractChromosomalPosition ftcp) throws Exception{
        String line = bimF.readRow();
        if (line != null) {
            String[] split = line.split("\\s");
            Allele a = Allele.getAllele(split[4].charAt(0));
            Allele b = Allele.getAllele(split[5].charAt(0));
            byte chr;
            try {
// Chromosome is in non numeric format
                chr = ftcp.retrieve_chromosome(split[0]);
            } catch (NumberFormatException tok) {
                chr = -1;
            }
            int position = Integer.parseInt(split[3]);
            String rsId = split[1];
            SNPPlink snp = new SNPPlink(rsId, chr, position, a, b);
            for (int e = 6; e < split.length; e++) {
                snp.addDatabase(split[e]);
            }
            return snp;
        }
        return null;
    }
    
    /**
     * Return the list of SNPs
     *
     * @return
     * @throws IOException
     */
    public ArrayList<SNPPlink> getOnlySNPs() throws Exception{
        ArrayList<SNPPlink> snps = new ArrayList<>();
        SNPPlink snp = nextSNP();
        while (snp != null) {
            snps.add(snp);
            snp = nextSNP();
        }
        return snps;
    }

    /**
     * Get the genotype given two alleles coded in the Plink format
     * 
     * @param a1
     * @param a2
     * @return
     */
    protected byte getGenotypeByte(byte a1, byte a2) {
        byte g = (byte) (a1 + a2);
        if (a1 == 1 && a2 == 0) {
            return 3;
        }
        return g;
    }

    @Override
    public SNPGenotypeByteArray nextSNPGenotype() throws Exception {
        // SNP
        SNPPlink snp = nextSNP();
        if (snp != null) {
            byte[] g = new byte[individuals.length];
            int iterations = individuals.length / 4;
            int indiv = 0;
            for (int it = 0; it < iterations; it++) {
                byte[] nextFourBytes = getNextFourGenotypes();
                for (int i = 0; i < 4; i++) {
                    g[indiv] = nextFourBytes[i];
                    indiv++;
                }
            }
            if (indiv != individuals.length) {
                byte[] nextFourBytes = getNextFourGenotypes();
                int i = 0;
                while (indiv < individuals.length) {
                    g[indiv] = nextFourBytes[i];
                    i++;
                    indiv++;
                }
            }

            return new SNPGenotypeByteArray(snp, individuals, g);
        }
        return null;
    }
    
    @Override
    public SNPGenotypeByteArray nextSNPGenotype(FunctionToExtractChromosomalPosition ftcp) throws Exception {                
        // SNP
        SNPPlink snp = nextSNP(ftcp);
        if (snp != null) {
            byte[] g = new byte[individuals.length];
            int iterations = individuals.length / 4;
            int indiv = 0;
            for (int it = 0; it < iterations; it++) {
                byte[] nextFourBytes = getNextFourGenotypes();
                for (int i = 0; i < 4; i++) {
                    g[indiv] = nextFourBytes[i];
                    indiv++;
                }
            }
            if (indiv != individuals.length) {
                byte[] nextFourBytes = getNextFourGenotypes();
                int i = 0;
                while (indiv < individuals.length) {
                    g[indiv] = nextFourBytes[i];
                    i++;
                    indiv++;
                }
            }

            return new SNPGenotypeByteArray(snp, g);
        }
        return null;
    }      

    public SNPGenotypeByteArray nextSNPGenotypeWithoutIndividuals() throws Exception {
        // SNP
        SNPPlink snp = nextSNP();
        if (snp != null) {
            byte[] g = new byte[individuals.length];
            int iterations = individuals.length / 4;
            int indiv = 0;
            for (int it = 0; it < iterations; it++) {
                byte[] nextFourBytes = getNextFourGenotypes();
                for (int i = 0; i < 4; i++) {
                    g[indiv] = nextFourBytes[i];
                    indiv++;
                }
            }
            if (indiv != individuals.length) {
                byte[] nextFourBytes = getNextFourGenotypes();
                int i = 0;
                while (indiv < individuals.length) {
                    g[indiv] = nextFourBytes[i];
                    i++;
                    indiv++;
                }
            }

            return new SNPGenotypeByteArray(snp, g);
        }
        return null;
    }

    /**
     * Get the next four genotypes. We read one byte, which we transform to
     * bits. Each two bits define a genotype
     *
     * @return
     * @throws IOException
     */
    protected byte[] getNextFourGenotypes() throws IOException {
        int b = getNextGenotype();
        byte[] bits = new byte[8];
        for (int e = 0; e < bits.length; e++) {
            bits[7 - e] = (byte) (b % 2);
            b /= 2;
        }
        byte[] g = new byte[4];
        for (int i = 0; i < 4; i++) {
            // Read reverse. Last bit is the first allele of the first individual!
            byte gI = getGenotypeByte(bits[6 - i * 2 + 1], bits[6 - i * 2]);
            g[i] = gI;
        }
        return g;
    }

    @Override
    public void close() throws IOException {
        bedB.close();
        bimF.close();
    }

    /**
     * Get a genome of the data
     *
     * @return
     */
    public SNPByIndividualMatrix getGenome(double nanThreshold) throws Exception {
        SNPByIndividualMatrix genome = new SNPByIndividualMatrix(individuals);
        SNPGenotypeByteArray snp = nextSNPGenotypeWithoutIndividuals();

//        int c = 0;
        while (snp != null) {
//            if(c%1000==0)
//            {
//                System.out.println(c);
//            }

            if (snp.getSnp().getA().transpose() != snp.getSnp().getB()) {
                byte[] g = snp.getGenotypes();
                int nans = 0;
                for (int e = 0; e < g.length; e++) {
                    if (g[e] > 2) {
                        nans++;
                    }
                }
                // Exclude SNPs with high percentage of missing genotypes and non polymorphic markers
                if (nans / (double) g.length < nanThreshold && snp.getSnp().getA() != Allele.N && snp.getSnp().getB() != Allele.N) {
                    genome.add(snp);
                }
            }
            snp = nextSNPGenotypeWithoutIndividuals();
//            c++;
        }
        genome.sort();
        return genome;

    }
}
