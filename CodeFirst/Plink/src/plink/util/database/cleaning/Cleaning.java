/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util.database.cleaning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import plink.util.SNPGenotypeByteArray;
import plink.util.database.PlinkBEDDatabase;
import plink.util.database.PlinkDatabaseSNPGenotypeByteArray;
import plink.util.demography.Individual;

/**
 *
 * @author olao
 */
public class Cleaning {

    /**
     * Compute the missingness matrix between pair of individuals. If two
     * individuals for a SNP have a null genotype or full genotypes, return 0,
     * otherwise 1. Add over all the SNPs
     *
     * @param db
     * @return
     */
    public static double[][] missingnessMatrix(PlinkDatabaseSNPGenotypeByteArray db) {
        double[][] m = new double[db.getIndividuals().length][db.getIndividuals().length];
// Go through each SNP
        db.stream().map((s) -> s.getGenotypes()).forEachOrdered((g) -> {
            for (int i1 = 0; i1 < g.length - 1; i1++) {
                for (int i2 = i1 + 1; i2 < g.length; i2++) {
                    if (g[i1] > 2) // g[i1] is null
                    {
                        if (g[i2] < 3) //g[i2] is not null
                        {
                            m[i1][i2]++;
                        }
                    } else // g[i1] is not null byt g[i2] is null
                    {
                        if (g[i2] > 2) {
                            m[i1][i2]++;
                        }
                    }
                }
            }
        });

        for (int e1 = 0; e1 < m.length - 1; e1++) {
            for (int e2 = e1 + 1; e2 < m.length; e2++) {
                m[e1][e2] /= db.size();
                m[e2][e1] = m[e1][e2];
            }
        }

        return m;
    }

    public static PlinkBEDDatabase cleanSNPsByMissingness(PlinkDatabaseSNPGenotypeByteArray db, double maximum_missingness_by_SNP) throws IOException {
        System.out.println("Here I go");
        PlinkBEDDatabase snps = new PlinkBEDDatabase(db.getIndividuals());

        db.forEach((o) -> {
            byte[] g = o.getGenotypes();
            double mf = 0;
            for (byte gg : g) {
                mf += (gg > 2) ? 1 : 0;
            }
            mf /= g.length;
            System.out.println(o.getSnp() + " " + mf);
            if (mf <= maximum_missingness_by_SNP) {
                snps.add(o);
            }
        });

        return snps;
    }

    public static PlinkBEDDatabase cleanSNPsBySameMissingnessAmongPopulations(PlinkDatabaseSNPGenotypeByteArray db, double maximum_missingness_by_SNP) throws IOException {
        PlinkBEDDatabase snps = new PlinkBEDDatabase(db.getIndividuals());
        HashMap<String, ArrayList<Integer>> ids_by_pop = new HashMap<>();

        Individual[] dp = db.getIndividuals();
// Individuals in populations
        for (int i = 0; i < dp.length; i++) {
            try {
                ids_by_pop.get(dp[i].getFamilyName()).add(i);
            } catch (NullPointerException tok) {
                ArrayList<Integer> ip = new ArrayList<>();
                ip.add(i);
                ids_by_pop.put(dp[i].getFamilyName(), ip);
            }
        }

// List of populations        
        ArrayList<String> pops = new ArrayList(ids_by_pop.keySet());

// Now evaluate each SNP        
        Hypergeometric hyp = new Hypergeometric(new LogFactorialSerie(db.get(0).getGenotypes().length + 1));

        db.forEach(new Consumer<SNPGenotypeByteArray>() {
            @Override
            public void accept(SNPGenotypeByteArray o) {
                byte[] g = o.getGenotypes();

                double mf = 0;
                for (byte gg : g) {
                    mf += (gg > 2) ? 1 : 0;
                }
                mf /= g.length;
// if the missigness among all the individuals independently of the population is less than the maximum, then go ahead
                if (mf <= maximum_missingness_by_SNP) {
// Now look that the proportions of missingness are not different from the expected
                    int[][] counts = new int[2][pops.size()];
                    for (int p = 0; p < pops.size(); p++) {
                        for (int i : ids_by_pop.get(pops.get(p))) {
                            int c = (o.isNa(g[i])) ? 0 : 1;
                            counts[c][p]++;
                        }
                    }

// Do a Fisher to check that the SNP has more o less the same proportion of missingness among all the populations
                    double log_pval = hyp.logpdf(counts);

                    for (int[] c : counts) {
                        System.out.println(java.util.Arrays.toString(c));
                    }

                    System.out.println(log_pval);

                    snps.add(o);
                }
            }
        });

        return snps;
    }

    /**
     * Print the missingness by individual
     *
     * @param db
     * @param max_missingness_byIndividual
     * @return
     * @throws IOException
     */
    public static PlinkBEDDatabase cleanMissingnessByIndividual(PlinkDatabaseSNPGenotypeByteArray db, double max_missingness_byIndividual) throws IOException {
        double[] m = new double[db.getIndividuals().length];
        db.forEach((o) -> {
            byte[] g = o.getGenotypes();
            for (int i = 0; i < g.length; i++) {
                m[i] += (g[i] > 2) ? 1 : 0;
            }
        });

        ArrayList<Integer> accepted_individuals = new ArrayList<>();

        for (int i = 0; i < m.length; i++) {
            double miss = m[i] / db.size();
            System.out.println(db.getIndividuals()[i] + " " + miss);
            if (miss < max_missingness_byIndividual) {
                accepted_individuals.add(i);
            }
        }

        Individual[] newIndividuals = new Individual[accepted_individuals.size()];

        for (int i = 0; i < accepted_individuals.size(); i++) {
            newIndividuals[i] = db.getIndividuals()[accepted_individuals.get(i)];
        }
        PlinkBEDDatabase snps = new PlinkBEDDatabase(newIndividuals);

        while (!db.isEmpty()) {
            SNPGenotypeByteArray o = db.remove(0);
            byte[] g = o.getGenotypes();
            byte[] gg = new byte[accepted_individuals.size()];
            double f = 0;
            double n = 0;
            for (int i = 0; i < accepted_individuals.size(); i++) {
                gg[i] = g[accepted_individuals.get(i)];
                if (gg[i] < 3) {
                    n += 2;
                    f += gg[i];
                }
            }
            f /= n;
// Only consider polymorphic SNPs for the accepted individuals            
            if (f * (1 - f) != 0) {
                snps.add(new SNPGenotypeByteArray(o.getSnp(), gg));
            }
        }
        return snps;
    }

    /**
     * Print the missingness by individual
     *
     * @param db
     * @param max_missingness_byIndividual
     * @throws IOException
     */
    public static void setGenotypesToMissingByIndividual(PlinkDatabaseSNPGenotypeByteArray db, double max_missingness_byIndividual) throws IOException {
        double[] m = new double[db.getIndividuals().length];
        db.forEach((o) -> {
            byte[] g = o.getGenotypes();
            for (int i = 0; i < g.length; i++) {
                m[i] += (g[i] > 2) ? 1 : 0;
            }
        });

        ArrayList<Integer> rejected_individuals = new ArrayList<>();

        for (int i = 0; i < m.length; i++) {
            double miss = m[i] / db.size();
            System.out.println(db.getIndividuals()[i] + " " + miss);
            if (miss >= max_missingness_byIndividual) {
                rejected_individuals.add(i);
            }
        }


        db.forEach((SNPGenotypeByteArray o) -> {
            rejected_individuals.forEach((i) -> {
                o.setGenotypeJinIndividualI(i, o.setNa());
            });
        });        
    }    
    
    /**
     * Print the missingness by individual
     *
     * @param db
     * @throws IOException
     */
    public static void computeMissingnessByIndividual(PlinkDatabaseSNPGenotypeByteArray db) throws IOException {
        double[] m = new double[db.getIndividuals().length];
        db.forEach((o) -> {
            byte[] g = o.getGenotypes();
            for (int i = 0; i < g.length; i++) {
                m[i] += (g[i] > 2) ? 1 : 0;
            }
        });
        for (int i = 0; i < m.length; i++) {
            System.out.println(db.getIndividuals()[i] + " " + m[i] / db.size());
        }
    }
}
