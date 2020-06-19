/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import plink.read.ReadSNPGenotypeArrayFile;
import plink.util.CompareSNPGenotypeByteByChrAndPosition;
import plink.util.demography.Individual;

/**
 *
 * @author oscar_000
 */
public class PlinkBEDDatabase extends PlinkDatabaseSNPGenotypeByteArray{

    /**
     * Create a new BED database in memory loading all the SNPs from the ReadSNPGenotypeByteArrayFile object
     * @param file
     * @throws IOException 
     */
    
    public PlinkBEDDatabase(ReadSNPGenotypeArrayFile file) throws Exception
    {
        super(file);
        setIndividualsToPops();
    }
    
    /**
     * Create a new BED database in memory with all the individuals
     * @param inds 
     */
    public PlinkBEDDatabase(Individual [] inds)
    {
        super(inds);
        setIndividualsToPops();
    }    
    
    private ReadSNPGenotypeArrayFile file;
    private HashMap<String,ArrayList<Integer>> indsByPop;
    private int excluded_SNPs = 0;
    private int included_SNPs = 0;

    /**
     * set the included SNPs
     * @param included_SNPs 
     */
    public void setIncluded_SNPs(int included_SNPs) {
        this.included_SNPs = included_SNPs;
    }

    /**
     * Set the excluded SNPs
     * @param excluded_SNPs 
     */
    public void setExcluded_SNPs(int excluded_SNPs) {
        this.excluded_SNPs = excluded_SNPs;
    }

    /**
     * Get the excluded SNPs
     * @return 
     */
    public int getExcluded_SNPs() {
        return excluded_SNPs;
    }

    /**
     * Get the included SNPs
     * @return 
     */
    public int getIncluded_SNPs() {
        return included_SNPs;
    }    
    
    
    public ReadSNPGenotypeArrayFile getFile() {
        return file;
    }    
    
    /**
     * Get the name of the populations
     * @return 
     */
    public Iterator<String> populationNames()
    {
        return indsByPop.keySet().iterator();
    }
    
    /**
     * Get the ids of each population
     * @param popName
     * @return 
     */
    public ArrayList<Integer> getIdsOfPopulation(String popName)
    {
        return indsByPop.get(popName);
    }
    
    
    public void setFile(ReadSNPGenotypeArrayFile file) {
        this.file = file;
    }
    
    /**
     * Sort the database by chromosome and position
     */
    public void sortDatabaseByChrPosition()
    {
        Collections.sort(this, new CompareSNPGenotypeByteByChrAndPosition());
    }
    
    /**
     * Add the ids of each individual to the popNames
     */
    public void setIndividualsToPops()
    {
        indsByPop = new HashMap<>();
        int r = 0;
        for(Individual i:individuals)
        {
            String [] split = i.getFamilyName().split("\\.");
            try
            {
                indsByPop.get(split[split.length-1]).add(r);
            }catch(NullPointerException tok)
            {
                ArrayList<Integer> newId = new ArrayList<>();
                newId.add(r);                
                indsByPop.put(split[split.length-1],newId);
            }
            r++;
        }
    }
    
    /**
     * Get the populations and ids of the individuals that are associated
     * @return 
     */
    public HashMap<String,ArrayList<Integer>> getPopulations()
    {
        return indsByPop;
    }
}
