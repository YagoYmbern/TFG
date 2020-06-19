/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util.database;

import java.util.List;
import plink.read.ReadSNPGenotypeArrayFile;
import plink.util.demography.Individual;
import plink.util.SNPGenotypeByteArray;

/**
 *
 * @author olao
 */
public class PlinkDatabaseSNPGenotypeByteArray extends PlinkDatabase<SNPGenotypeByteArray> {

    public PlinkDatabaseSNPGenotypeByteArray(Individual[] inds) {
        super(inds);
    }

    public PlinkDatabaseSNPGenotypeByteArray(ReadSNPGenotypeArrayFile rb) throws Exception {
        super(rb.getIndividuals());
        SNPGenotypeByteArray snp = rb.nextSNPGenotype();
        while (snp != null) {
            this.add(snp);
            snp = rb.nextSNPGenotype();
        }
        rb.close();
    }

    /**
     * Retrieve the data from the list of individuals. Exclude SNPs that are
     * monomorphic in the list of accepted individuals
     *
     * @param list_of_individuals
     * @return
     */
    public PlinkDatabaseSNPGenotypeByteArray extractIndividuals(List<Integer> list_of_individuals) {
        Individual[] newI = new Individual[list_of_individuals.size()];
        int c = 0;
        for(int i:list_of_individuals)
        {
            newI[c] = individuals[i];
            c++;
        }

        PlinkDatabaseSNPGenotypeByteArray l = new PlinkDatabaseSNPGenotypeByteArray(newI);

        this.forEach((t) -> {
            SNPGenotypeByteArray s = t.extractIndividuals(list_of_individuals);
            if (s.is_polymorphic()) {
                l.add(s);
            }
        });

        return l;
    }
}
