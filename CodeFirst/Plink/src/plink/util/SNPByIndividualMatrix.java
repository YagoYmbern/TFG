/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util;

import java.util.ArrayList;
import java.util.Collections;
import plink.util.demography.Individual;

/**
 *
 * @author oscar_000
 */
public class SNPByIndividualMatrix extends ArrayList<SNPGenotypeByteArray>{

  public SNPByIndividualMatrix (Individual[] individual) {
        setIndividuals(individual);
    }
    private Individual[] individuals;

    public void setIndividuals(Individual[] individual) {
        this.individuals = individual;
    }
    
    /**
     * Get the individual columns
     * @return 
     */
    public Individual[] getIndividual() {
        return individuals;
    }

    public void sort()
    {
        Collections.sort(this);
    }
    
    /**
     * Get the index of the SNP in the genome. First call the sort method!
     * Return a negative number if it does not exist
     * @param snp
     * @return 
     */
    public int getIndex(SNPGenotypeByteArray snp)
    {
        return Collections.binarySearch(this, snp);
    }
    
    /**
     * Update the individual i of SNP j with genotype b
     * @param i
     * @param j
     * @param b 
     */
    public void updateIndividualIOfSNPJ(int i, int j, byte b)
    {
        this.get(j).setGenotypeJinIndividualI(i, b);
    }
        
    /**
     * Add a new SNP with the individuals that are currently in the database
     * All genotypes are initially null
     * @param snp 
     */
    public void addSNP(SNPPlink snp)
    {
        byte [] g = new byte[individuals.length];
        for(int e=0;e<g.length;e++)
        {
            g[e] = 3;
        }
    }    
    
    /**
     * Add new individuals at the end of the genome
     * @param iA 
     */
    public void addEndIndividuals(Individual[] iA) {
        Individual[] I2 = new Individual[individuals.length + iA.length];
        System.arraycopy(individuals, 0, I2, 0, individuals.length);
        System.arraycopy(iA, 0, I2, individuals.length, iA.length);
        setIndividuals(I2);
    }

    /**
     * Remove the SNPs with a number of genotypes different from the number of individuals
     */
    public void prune()
    {
        int c = 0;
        while(c < size())
        {
            if(get(c).getGenotypes().length != individuals.length)
            {
                remove(c);
            }
            else
            {
                c++;
            }
        }
    }
}
