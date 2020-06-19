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
public abstract class SNPGenotype implements Comparable<SNPGenotype> {    
    public SNPGenotype(SNPPlink snp, Individual [] individuals)
    {
        setSnp(snp);
        setIndividuals(individuals);
        try
        {
            this.size = individuals.length;            
        }
        catch(NullPointerException tok)
        {            
        }
    }
    
    public SNPGenotype(SNPPlink snp)
    {
        setSnp(snp);
    }    
    
    private SNPPlink snp;
    private Individual[] individuals;  
    private int size;

    /**
     * This is the size of the array
     * @param size 
     */
    public void setSize(short size)
    {
        this.size = size;
    }    
    
    public void setIndividuals(Individual[] individuals) {
        this.individuals = individuals;
    }

    public void setSnp(SNPPlink snp) {
        this.snp = snp;
    }

    public Individual[] getIndividuals() {
        return individuals;
    }

    public SNPPlink getSnp() {
        return snp;
    }

    @Override
    public int compareTo(SNPGenotype o) {
        return this.getSnp().compareTo(o.getSnp());
    }

    public int size()
    {
        return size;
    }
    
    /**
     * Set the genotype g in individual i
     * @param i
     * @param g 
     */
    public abstract void setGenotypeJinIndividualI(int i, byte g); 
    
    public abstract byte getGenotypeOfIndividualI(int i);
    
    public abstract byte [] getGenotypes();
    
    @Override
    public String toString()
    {
        StringBuilder a = new StringBuilder();
        a.append(this.getSnp());
        for(int i=0;i<size();i++)
        {
            String c;
            switch(getGenotypeOfIndividualI(i))
            {
                case 0: c = " " + getSnp().getA() + " " + getSnp().getA();break;
                case 1: c = " " + getSnp().getA() + " " + getSnp().getB();break;
                case 2: c = " " + getSnp().getB() + " " + getSnp().getB();break;
                default:c = " 0 0";break;
            }
            a.append(c);
        }        
        return a.toString();
    }
    
    /**
     * identify the genotype as NA. Return true if this genotype contains the null genotype
     * @param g
     * @return 
     */
    public boolean isNa(byte g)
    {
        return g >2;
    }
    
    /**
     * Define the null allele
     * @return 
     */
    public byte setNa()
    {
        return 3;
    }
}
