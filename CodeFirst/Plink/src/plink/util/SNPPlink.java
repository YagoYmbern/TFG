/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plink.util;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class SNPPlink implements Comparable<SNPPlink>{
    public SNPPlink(String rsId, byte chromosome, int position, Allele a, Allele b)
    {
        setRsId(rsId);
        setA(a);
        setB(b);
        setChromosome(chromosome);
        setPosition(position);        
    }

    private String rsId;
    byte chromosome;
    private Allele a, b;
    private Strand s;
    private int position;
    private ArrayList<String> databases;
    private Status status;
    private Allele anc;

    
    /**
     * Set an ancestral state
     * @param anc 
     */
    public void addAncestral(Allele anc)
    {
        this.anc = anc;
    }

    /**
     * Get the ancestral (or reference) allele
     * @return 
     */
    public Allele getAnc() {
        return anc;
    }
    
    
    
    /**
     * Set the status of this SNP
     * @param status 
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Get the status of this SNP
     * @return 
     */
    public Status getStatus() {
        return status;
    }
    
    public void addDatabase(String d)
    {        
        try
        {
            int indexOfD = databases.indexOf(d);
            if(indexOfD < 0)
            {
                databases.add(d);                
            }
        }catch(NullPointerException tok)
        {
            databases = new ArrayList<>();
            databases.add(d);
        }
    }
    
    public void deleteDatabases()
    {
        databases = new ArrayList<>();
    }
    
    public ArrayList<String> getDatabases()
    {
        return databases;
    }
    
    public void setChromosome(byte chromosome) {
        this.chromosome = chromosome;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public byte getChromosome() {
        return chromosome;
    }

    public int getPosition() {
        return position;
    }

    /**
     * Set the allele a
     * @param a
     */
    public void setA(Allele a) {
        this.a = a;
    }

    /**
     * Set the allele b
     * @param b
     */
    public void setB(Allele b) {
        this.b = b;
    }
    
    /**
     * Get the index of the allele c. Return 0 if it corresponds to the first allele.
     * Return 1 if it corresponds to the second allele.
     * Return -1 if it does not corresponds to the first or second allele
     * @param c
     * @return 
     */
    public int getIndexOf(Allele c)
    {
        if(c==a)
        {
            return 0;
        }
        if(c==b)
        {
            return 1;
        }
        return -1;
    }

    /**
     * Set the rsId
     * @param rsId
     */
    public void setRsId(String rsId) {
        this.rsId = rsId;
    }

    /**
     * Set the Strand
     * @param s
     */
    public void setS(Strand s) {
        this.s = s;
    }

    /**
     * get the allele a
     * @return
     */
    public Allele getA() {
        return a;
    }

    /**
     * get the allele B
     * @return
     */
    public Allele getB() {
        return b;
    }

    /**
     * Get the rsId
     * @return
     */
    public String getRsId() {
        return rsId;
    }

    /**
     * Get the strand of the SNP
     * @return
     */
    public Strand getS() {
        return s;
    }

    @Override
    public String toString()
    {
        String a = (getA()==Allele.N)?"0":getA().toString();
        String b = (getB()==Allele.N)?"0":getB().toString();
        //  1  rs123456  0  1234555
        return getChromosome() + " " + getRsId() + " 0 " + getPosition() + " " + a + " " + b;
    }

    /**
     * Compare by rsId
     * @param o
     * @return
     */
    public int compareTo(SNPPlink o) {
        return getRsId().compareTo(o.getRsId());
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(o instanceof SNPPlink)
        {
            return(this.getRsId().equals(((SNPPlink)o).getRsId()));
        }
        return false;
    }
    
    public double genomicDistance(SNPPlink o)
    {
        if(getChromosome()==(o.getChromosome()))
        {
            return Math.abs(getPosition()-o.getPosition());
        }
        return Double.POSITIVE_INFINITY;
    }
    
    /**
     * Shift the alleles
     * @return 
     */
    public SNPPlink shiftAlleles()
    {
        return new SNPPlink(getRsId(), getChromosome(), getPosition(), getB(), getA());
    }
    
    /**
     * Indicate if the SNP contains at least one null allele
     * @return 
     */
    public boolean containsNullAlleles()
    {
        if(getA()==Allele.N)
        {
            return true;
        }
        if(getB()==Allele.N)
        {
            return true;
        }
        return false;
    }
    
    /**
     * Get a copy of this object
     * @return 
     */
    public SNPPlink getCopy()
    {
        return new SNPPlink(getRsId(),getChromosome(),getPosition(),getA(),getB());
    }
}
