/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package plink.util;

import plink.util.demography.Individual;

/**
 *
 * @author oscar_000
 */
public class IndividualGenotypeByte {
    public IndividualGenotypeByte(Individual i)
    {
        setI(i);
    }
    
    private byte [] g;
    private Individual i;

    /**
     * Set the individual name;
     * @param i 
     */
    public void setI(Individual i) {
        this.i = i;
    }

    /**
     * Get the individual name
     * @return 
     */
    public Individual getI() {
        return i;
    }
            
    public void setGenotype(byte [] g)
    {
        this.g = g;
    }
    
    public byte [] getGenotype()
    {
        return g;
    }
    
    /**
     * Add in position i the allele a
     * @param i
     * @param a 
     */
    public void add(int i, byte a)
    {
        g[i] = (byte)(g[i]+a);
    }
    
    /**
     * Get the genotype at position i
     * @param i
     * @return 
     */
    public byte getGenotype(int i)
    {
        return g[i];
    }
            
}
