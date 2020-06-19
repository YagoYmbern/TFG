/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vcf;

import plink.read.FunctionToExtractChromosomalPosition;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import plink.read.ReadSNPGenotypeArrayFile;
import plink.util.Allele;
import plink.util.SNPDiHaplotypeByteArray;
import plink.util.SNPGenotypeByteArray;
import plink.util.SNPPlink;
import plink.util.demography.Individual;
import read.ReaderFile;
import write.OutputFile;

/**
 *
 * @author Oscar Lao
 */
public class ReadVCF implements ReadSNPGenotypeArrayFile {

    public ReadVCF(ReaderFile file) throws Exception {
        r = file;
        goToFirstSNP();
        ofile = null;
    }

    public ReadVCF(ReaderFile file, String[] conditions_to_check_in_info_column, String command_allele_to_be_considered_as_alternative) throws Exception {
        r = file;
        goToFirstSNP();
        this.conditions_to_check = conditions_to_check_in_info_column;
        this.allele_to_be_considered_as_alternative = command_allele_to_be_considered_as_alternative;
        ofile = null;
    }

    public ReadVCF(ReaderFile file, OutputFile ofile) throws Exception {
        r = file;
        this.ofile = ofile;
        goToFirstSNP();
    }

    public ReadVCF(ReaderFile file, OutputFile ofile, String[] conditions_to_check_in_info_column, String command_allele_to_be_considered_as_alternative) throws Exception {
        r = file;
        this.conditions_to_check = conditions_to_check_in_info_column;
        this.allele_to_be_considered_as_alternative = command_allele_to_be_considered_as_alternative;
        this.ofile = ofile;
        goToFirstSNP();
    }

    private final OutputFile ofile;
    private final ReaderFile r;
    private Individual[] individuals = null;
    private int ignored_snps = 0;
    private String[] conditions_to_check = null;
    private String allele_to_be_considered_as_alternative = null;
    private String line;

    /**
     * Get the current line of the VCF
     *
     * @return
     */
    public String getLine() {
        return line;
    }

    /**
     * Get the output file
     *
     * @return
     */
    public OutputFile getOfile() {
        return ofile;
    }

    public int getIgnored_snps() {
        return ignored_snps;
    }

    /**
     * Get the individuals
     *
     * @return
     */
    @Override
    public Individual[] getIndividuals() {
        return individuals;
    }

    /**
     * Read the header until starting the first SNP. Add the header to the ofile
     * if it exists
     */
    public final void goToFirstSNP() throws Exception {
        line = r.readRow();
        while (!line.contains("#CHROM")) {
            if (ofile != null) {
                ofile.println(line + "\n");
            }
            line = r.readRow();
        }
// Add the chromosome row
        if (ofile != null) {
            ofile.println(line + "\n");
        }

        String[] split = line.split("\t");
        int n_ind = split.length - 9;
        individuals = new Individual[n_ind];
        for (int s = 9; s < split.length; s++) {
            individuals[s - 9] = new Individual(split[s], split[s]);
        }
    }

    /**
     * Get the next SNP. Return null if the end of the file has been reached
     *
     * @return
     * @throws IOException
     */
    @Override
    public SNPGenotypeByteArray nextSNPGenotype() throws IOException {
        do {
            line = r.readRow();
            if (line != null) {
//                try {
                String[] split = line.split("\\s");
                byte chr = -1;
                try {
                    chr = Byte.parseByte(split[0].replaceAll("chr", ""));

                } catch (NumberFormatException tok) {
                }
                if (split[3].length() == 1 && split[4].length() == 1) {
                    Allele alternative_allele_should_be = null;
                    boolean has_all_conditions = true;

                    if (conditions_to_check != null) {
                        for (String pat : conditions_to_check) {
                            if (!split[7].contains(pat)) {
                                has_all_conditions = false;
                                break;
                            }
                        }
                    }
                    if (has_all_conditions) {
                        if (allele_to_be_considered_as_alternative != null) {
                            int i = split[7].indexOf(allele_to_be_considered_as_alternative);
                            String ab = split[7].substring(i + allele_to_be_considered_as_alternative.length(), i + allele_to_be_considered_as_alternative.length() + 1);
                            alternative_allele_should_be = Allele.getAllele(ab.charAt(0));
                        }

                        SNPPlink snp;
                        // I must switch genotypes if the alternative allele is the same as the reference of the VCF
                        boolean switch_genotypes = false;
                        if (alternative_allele_should_be != null) {
                            // Reference allele in VCF (counts for 0 in the genotypes)
                            Allele a = Allele.getAllele(split[3].charAt(0));
                            // Alternative allele in VCF (counts for 1 in the genotypes)
                            Allele b = Allele.getAllele(split[4].charAt(0));
                            // If the alternative is the reference, we have to switch the genotypes. Alternative must be always the second allele
                            if (alternative_allele_should_be == a) {
                                switch_genotypes = true;
                            }
                            snp = new SNPPlink(split[2], chr, Integer.parseInt(split[1]), a, b);
                        } else {
                            snp = new SNPPlink(split[2], chr, Integer.parseInt(split[1]), Allele.getAllele(split[3].charAt(0)), Allele.getAllele(split[4].charAt(0)));
                        }

                        SNPGenotypeByteArray snpG;

                        byte[] genotypes = new byte[individuals.length];
                        // This is a dihaplotype
                        if (split[9].contains("|")) {
                            snpG = new SNPDiHaplotypeByteArray(snp, genotypes);
                        } else {
                            // This is a genotype
                            snpG = new SNPGenotypeByteArray(snp, genotypes);
                        }

                        for (int e = 9; e < split.length; e++) {
                            char a = split[e].charAt(0);
                            char b = split[e].charAt(2);
                            // null genotype
                            if (a == '.') {
                                snpG.set_null_genotype(e - 9);
                            } else {
                                // haplotype or genotype to be stored
                                byte[] h = new byte[2];
                                h[0] = (a == '1') ? (byte) 1 : (byte) 0;
                                h[1] = (b == '1') ? (byte) 1 : (byte) 0;
                                snpG.set_haplotypes_at_Individual(e - 9, h);
                            }
                        }
                        if (switch_genotypes) {
                            snpG = snpG.shiftAllelesIgnoreIndividuals();
                        }
                        return snpG;
                    } else {
// Is not a SNP. Go to the next SNP
                        ignored_snps++;
                    }
                }
//                } catch (Exception tok) {                   
//                }
            } else {
                return null;
            }
        } while (true);
    }

    @Override
    public SNPGenotypeByteArray nextSNPGenotype(FunctionToExtractChromosomalPosition ftcp) throws Exception {
        line = r.readRow();
        if (line != null) {
            try {
                String[] split = line.split("\t");
//            try {
                byte chr = -1;
                String kk = split[0];
                try {
                    chr = ftcp.retrieve_chromosome(kk);
                } catch (NumberFormatException tok) {
                    System.out.println(split[0] + " " + kk);
                }
                if (split[3].length() == 1 && split[4].length() == 1) {
                    SNPPlink snp = new SNPPlink(split[2], chr, Integer.parseInt(split[1]), Allele.getAllele(split[3].charAt(0)), Allele.getAllele(split[4].charAt(0)));
                    byte[] genotypes = new byte[individuals.length];
                    for (int e = 9; e < split.length; e++) {
                        char a = split[e].charAt(0);
                        char b = split[e].charAt(2);
                        byte g = 0;
                        if (a == '.') {
                            g = 3;
                        } else {
                            if (a == '1') {
                                g = (byte) (g + 1);
                            }
                            if (b == '1') {
                                g = (byte) (g + 1);
                            }
                        }
                        genotypes[e - 9] = g;
                    }
                    SNPGenotypeByteArray snpG = new SNPGenotypeByteArray(snp, individuals, genotypes);
                    return snpG;
                } else {
//                    System.out.println(line);
//                    System.out.println(split[3] + " " + split[4]);
                    ignored_snps++;
                    return nextSNPGenotype(ftcp);
                }

            } catch (IOException | NumberFormatException tok) {
                return nextSNPGenotype(ftcp);
            }
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        r.close();
    }

    @Override
    public HashMap<String, ArrayList<Integer>> classify_individuals_by_family_id() {
        HashMap<String, ArrayList<Integer>> classify_individuals_by_family_id = new HashMap<>();
        for (int i = 0; i < individuals.length; i++) {
            try {
                classify_individuals_by_family_id.get(individuals[i].getFamilyName()).add(i);
            } catch (NullPointerException tok) {
                ArrayList<Integer> id = new ArrayList<>();
                id.add(i);
                classify_individuals_by_family_id.put(individuals[i].getFamilyName(), id);
            }
        }
        return classify_individuals_by_family_id;
    }
}
