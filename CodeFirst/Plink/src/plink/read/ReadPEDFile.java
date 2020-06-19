/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.read;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import plink.PlinkFileException;
import plink.util.Allele;
import plink.util.SNPGenotypeByteArray;
import plink.util.SNPPlink;
import plink.util.demography.Individual;
import read.ReadFile;

/**
 *
 * @author Oscar Lao
 */
public class ReadPEDFile {

    public ReadPEDFile(String fileName) throws FileNotFoundException, IOException, PlinkFileException {
        getSNPs(fileName);
        getData(fileName);
    }

    private Individual[] individuals;
    private ArrayList<byte[]> genotypes = new ArrayList<>();
    private ArrayList<SNPPlink> snps = new ArrayList<>();
    private int counter = 0;

    /**
     * Retrieve the SNPs, Genotypes, Individual Names, etc
     *
     * @param f
     * @return
     */
    private void getSNPs(String f) {
        try
        {
        ReadFile map = new ReadFile(new File(f + ".map"));
        String line = map.readRow();
        while (line != null) {
            String[] split = line.split("\t");
            byte chr = Byte.parseByte(split[0]);
            String rsId = split[1];
            int position = Integer.parseInt(split[3]);
            snps.add(new SNPPlink(rsId, chr, position, Allele.N, Allele.N));
            line = map.readRow();
        }
        map.close();            
        }catch(IOException tok)
        {
            System.out.println(f + " not found");
            System.exit(0);
        }
    }

    /**
     * Get the data
     *
     * @param f
     */
    private void getData(String f) {
        try {
            ReadFile ped = new ReadFile(new File(f + ".ped"));
            ArrayList<Individual> inds = new ArrayList<>();
            String line = ped.readRow();
            while (line != null) {
                String[] split = line.split(" ");
                byte[] gInd = new byte[(split.length - 6) / 2];
                String family = split[0];
                String name = split[1];
                int so = 0;
                for (int c = 6; c < split.length; c += 2) {
                    Allele a = Allele.getAllele(split[c].charAt(0));
                    Allele b = Allele.getAllele(split[c + 1].charAt(0));
// Do nothing. Go to the next individual
                    if (a == Allele.N) {
                        gInd[so] = 3;
                    } else {
                        // First allele is Null. Fill with the allele of the individual
                        if (snps.get(so).getA() == Allele.N) {
                            snps.get(so).setA(a);
                        }
                        if (snps.get(so).getA() != a) {
                            gInd[so] = (byte) (gInd[so] + 1);
                            if (snps.get(so).getB() == Allele.N) {
                                snps.get(so).setB(a);
                            }
                        }
// If b is not the first allele, then it must be the second allele                        
                        if (snps.get(so).getA() != b) {
                            gInd[so] = (byte) (gInd[so] + 1);
                        }
                        if (a != b && gInd[so] != 1) {
                            System.out.println(gInd[so] + " " + a + " " + b);
                        }
                    }
                    so++;
                }
                Individual in = new Individual(family, name);
                genotypes.add(gInd);
                System.out.println(in);
                inds.add(in);
                line = ped.readRow();
            }
            ped.close();
            individuals = new Individual[inds.size()];
            inds.toArray(individuals);
        } catch (IOException tok) {
            System.out.println(f + " not found");
            System.exit(0);
        }
    }

    /**
     * Get the next SNP
     *
     * @return
     */
    public SNPGenotypeByteArray nextSNPIgnoreIndividuals() {
        if (counter < snps.size()) {
            SNPPlink snp = snps.get(counter);
            byte[] g = new byte[individuals.length];
            int i = 0;
            for (byte[] gg : genotypes) {
                g[i] = gg[counter];
                i++;
            }
            counter++;
            return new SNPGenotypeByteArray(snp, g);
        } else {
            return null;
        }
    }

    public Individual[] getIndividuals() {
        return individuals;
    }
}
