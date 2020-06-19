/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util.database;

import java.util.ArrayList;
import java.util.HashMap;
import plink.util.demography.Individual;


/**
 * 
 * @author olao
 * @param <SNPGenotype> 
 */
public class PlinkDatabase<SNPGenotype> extends ArrayList<SNPGenotype>{
    public PlinkDatabase(Individual [] individuals)
    {        
        this.individuals = individuals;
    }
    
    protected final Individual [] individuals;

    /**
     * Get the individuals
     * @return 
     */
    public Individual[] getIndividuals() {
        return individuals;
    }
    
    /**
     * Retrieve the population labels
     *
     * @return
     */
    public HashMap<String, ArrayList<Integer>> retrieve_individuals_by_family() {
// Pops
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
