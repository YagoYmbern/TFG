/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util.database;

import java.util.ArrayList;
import plink.util.SNPGenotypeByteArray;
import plink.util.demography.Individual;

/**
 *
 * @author Oscar Lao
 */
public class SNPGenotypeByteArrayDatabase extends ArrayList<SNPGenotypeByteArray>{
    /**
     * Create a new database
     * @param individuals 
     */
    public SNPGenotypeByteArrayDatabase(Individual [] individuals)
    {
        this.individuals = individuals;
    }
    
    private Individual [] individuals;

    /**
     * Get the individuals
     * @return 
     */
    public Individual[] getIndividuals() {
        return individuals;
    }

    /**
     * Set the individuals
     * @param individuals 
     */
    public void setIndividuals(Individual[] individuals) {
        this.individuals = individuals;
    }
}
