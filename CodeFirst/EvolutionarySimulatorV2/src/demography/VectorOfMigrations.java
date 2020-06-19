/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demography;

import exception.ExceptionDemography;
import maths.RandomMultinomial;

/**
 *
 * @author olao
 */
public class VectorOfMigrations {

    public VectorOfMigrations(Population reference_population) throws ExceptionDemography {
        this.reference_population = reference_population;
        pdm = new Population_MigrationRate[0];
    }

    private RandomMultinomial migrants = null;
    private Population_MigrationRate[] pdm = null;
    private final Population reference_population;

    /**
     * Is this vector of migrations empty
     *
     * @return
     */
    public boolean isEmpty() {
        return pdm == null;
    }   
    
    /**
     * New migration vector
     *
     * @param migrationRates_per_generation
     */
    public void setMigration(Population_MigrationRate ... migrationRates_per_generation) throws ExceptionDemography {
        double t = 0;
        for (Population_MigrationRate m : migrationRates_per_generation) {
            t += m.getMigration_rate().getFg().getGeneratedNumber().doubleValue();
        }
        if (t > 1) {
            throw new ExceptionDemography("The fraction of migrants among all the populations is different than 1 = " + t);
        }
        this.pdm = migrationRates_per_generation;
        double[] migrationProportions = new double[pdm.length + 1];
        for (int i = 0; i < pdm.length; i++) {
            // Add this population to the populations that will send migrants to this population
            pdm[i].getPopulation().addPopulation_that_sent_backward_migrants_to_this_population(reference_population);
            migrationProportions[i] = pdm[i].getMigration_rate().getFg().getGeneratedNumber().doubleValue();
        }
        migrationProportions[migrationProportions.length - 1] = 1 - t;
        migrants = new RandomMultinomial(migrationProportions);
    }

    /**
     * Get the array of population_migration rates
     *
     * @return
     */
    public Population_MigrationRate[] getPdm() {
        return pdm;
    }

    /**
     * Remove the population from this vector of migrations
     * @param population
     * @return
     * @throws ExceptionDemography 
     */
    public boolean remove(Population population) throws ExceptionDemography{
        int removedIdx = -1;
        
        for(int i=0;i<pdm.length;i++)
        {
            if(pdm[i].getPopulation().equals(population))
            {
                removedIdx = i;
                break;
            }
        }
        
        if(removedIdx < 0)
        {
            return false;
        }
        else
        {
            Population_MigrationRate [] new_pdm = new Population_MigrationRate[pdm.length-1];
            System.arraycopy(pdm,0,new_pdm,0,removedIdx);
            System.arraycopy(pdm,removedIdx+1,new_pdm,removedIdx, pdm.length -1 - removedIdx);            
            setMigration(new_pdm);
            return true;
        }
    }

    /**
     * Migration is finished
     */
    public void erase() {
        migrants = null;
        pdm = null;
    }

    /**
     * Get a sample of populations given the migration rates backward in time
     *
     * @param samples
     * @return
     */
    public Population [] sample(int samples) {
        Population[] sampled_pops = new Population[samples];
        if (migrants != null) {
            int[] ids_samples = migrants.sample(samples);
            for (int i = 0; i < samples; i++) {
                if (ids_samples[i] < pdm.length) {
                    sampled_pops[i] = pdm[ids_samples[i]].getPopulation();
                } else {
                    // Reference population is the last index
                    sampled_pops[i] = reference_population;
                }
            }
        } else {
            for (int i = 0; i < samples; i++) {
// Reference population is the last index
                sampled_pops[i] = reference_population;
            }
        }
        return sampled_pops;
    }

    /**
     * Get the mutation rate of this population
     *
     * @param p
     * @return
     */
    public Population_MigrationRate getPopulationDemography_MigrationRate(int p) {
        return pdm[p];
    }
}
