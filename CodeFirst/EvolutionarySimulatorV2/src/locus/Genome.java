/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package locus;

import exception.ExceptionGenome;

/**
 *
 * @author oscar_000
 */
public class Genome {
    /**
     * Create a diploid genome
     */
    public Genome()
    {
        diploide[0] = new Chromosome();
        diploide[1] = new Chromosome();
    }
    private Chromosome [] diploide = new Chromosome[2];    

    /**
     * Add the Chromosome
     * @param i
     * @param chr
     * @throws ExceptionGenome if i is <0 or >1
     */
    public void setChromosome(int i, Chromosome chr) throws ExceptionGenome
    {
        if(i> -1 && i <2)
        {
            diploide[i] = chr;            
        }
        else
        {
            throw new ExceptionGenome("Genome can only be 0 or 1");
        }
    }
    
    /**
     * Get the chromosome in position i
     * @param i
     * @return
     * @throws ExceptionGenome 
     */
    public Chromosome getChromosome(int i) throws ExceptionGenome
    {
        if(i> -1 && i <2)
        {
            return diploide[i];            
        }
        else
        {
            throw new ExceptionGenome("Genome can only be 0 or 1");
        }        
    }
}
