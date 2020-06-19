/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.read;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import plink.util.Allele;
import plink.util.SNPGenotypeByteArray;
import plink.util.SNPPlink;
import plink.util.demography.Individual;
import read.ReadFile;

/**
 *
 * @author oscar_000
 */
public class ReadTPEDFile {

    public ReadTPEDFile(String file) throws Exception {
        tfam = new ReadFile(file + ".tfam");
        tPED = new ReadFile(file + ".tPED");
        fillIndividuals();
    }

    private ReadFile tfam;
    private ReadFile tPED;
    private Individual[] individuals;

    private void fillIndividuals() throws Exception{
        ArrayList<Individual> inds = new ArrayList<>();
        String line = tfam.readRow();
        while (line != null) {
            String[] split = line.split(" ");
            inds.add(new Individual(split[0], split[1]));
            line = tfam.readRow();
        }
        individuals = new Individual[inds.size()];
        inds.toArray(individuals);
        try {
            tfam.close();
        } catch (IOException tok) {

        }
    }

    /**
     * Get the individuals
     *
     * @return
     */
    public Individual[] getIndividualNames() {
        return individuals;
    }

    /**
     * Get the data
     *
     * @return
     * @throws IOException
     */
    public SNPGenotypeByteArray nextSNPGenotype() throws IOException {
        String line = tPED.readRow();
        if (line == null) {
            return null;
        }
        String[] split = line.split("\\s");

        byte chr = Byte.parseByte(split[0]);
        String rs = split[1];
        int position = Integer.parseInt(split[3]);
        // SNP

        // Individuals
        byte[] g = new byte[individuals.length];

        int ind = 0;

        HashSet<String> alleles = new HashSet<>();

        for (int r = 4; r < split.length; r++) {
            if (!split[r].equals("0")) {
                alleles.add(split[r]);
            }
        }
        ArrayList<String> als = new ArrayList<>(alleles);
// The individuals are not null for this allele
        if (als.size() > 0 && als.size() < 3) {
            Allele a = Allele.N;
            try {
                a = Allele.getAllele(als.get(0).charAt(0));
            } catch (IndexOutOfBoundsException tok) {

            }

            Allele b = Allele.N;

            try {
                b = Allele.getAllele(als.get(1).charAt(0));
            } catch (IndexOutOfBoundsException tok) {

            }

            SNPPlink snp = new SNPPlink(rs, chr, position, a, b);

            for (int row = 4; row < split.length; row += 2) {
                Allele a1 = Allele.getAllele(split[row].charAt(0));
                Allele a2 = Allele.getAllele(split[row + 1].charAt(0));
                if (a1 == Allele.N) {
                    g[ind] = 3;
                } else {
                    int gogo = 0;
                    if (snp.getB() == a1) {
// This will be the first allele
                        gogo++;
                    }
                    if (snp.getB() == a2) {
                        gogo++;
                    }
                    g[ind] = (byte) gogo;
                }
                ind++;
            }
            return new SNPGenotypeByteArray(snp, individuals, g);

        }
        return null;
    }

    /**
     * Get the data
     *
     * @return
     * @throws IOException
     */
    public SNPGenotypeByteArray nextSNPGenotypeWithoutIndividuals() throws IOException {
        String line = tPED.readRow();
        if (line == null) {
            return null;
        }
        String[] split = line.split("\\s");

        byte chr = Byte.parseByte(split[0]);
        String rs = split[1];
        int position = Integer.parseInt(split[3]);
        // SNP

        // Individuals
        byte[] g = new byte[individuals.length];

        int ind = 0;

        HashSet<String> alleles = new HashSet<>();

        for (int r = 4; r < split.length; r++) {
            if (!split[r].equals("0")) {
                alleles.add(split[r]);
            }
            if (alleles.size() == 2) {
                break;
            }
        }
        ArrayList<String> als = new ArrayList<>(alleles);
// The individuals are not null for this allele
        if (als.size() > 0 && als.size() < 3) {
            Allele a = Allele.getAllele(als.get(0).charAt(0));
            Allele b = Allele.N;
            try {
                b = Allele.getAllele(als.get(1).charAt(0));
            } catch (IndexOutOfBoundsException tok) {

            }

            SNPPlink snp = new SNPPlink(rs, chr, position, a, b);

            for (int row = 4; row < split.length; row += 2) {
                Allele a1 = Allele.getAllele(split[row].charAt(0));
                Allele a2 = Allele.getAllele(split[row + 1].charAt(0));
                if (a1 == Allele.N) {
                    g[ind] = 3;
                } else {
                    int gogo = 0;
                    if (snp.getB() == a1) {
// This will be the first allele
                        gogo++;
                    }
                    if (snp.getB() == a2) {
                        gogo++;
                    }
                    g[ind] = (byte) gogo;
                }
                ind++;
            }
            return new SNPGenotypeByteArray(snp, g);
        }
        return null;
    }

    /**
     * Close the connection
     */
    public void close() {
        try
        {
            tPED.close();            
        }catch(IOException tok)
        {
            
        }
    }
}
