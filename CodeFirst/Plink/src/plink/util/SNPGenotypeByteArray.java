/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util;

import java.util.List;
import plink.util.demography.Individual;

/**
 *
 * @author Administrator
 */
public class SNPGenotypeByteArray extends SNPGenotype {

    public SNPGenotypeByteArray(SNPPlink snp, Individual[] individuals, byte[] genotypes) {
        super(snp, individuals);
        setGenotypes(genotypes);
    }

    public SNPGenotypeByteArray(SNPPlink snp, byte[] genotypes) {
        super(snp);
        setGenotypes(genotypes);
    }

    public SNPGenotypeByteArray(SNPPlink snp, int size) {
        super(snp);
        byte[] b = new byte[size];
        for (int e = 0; e < size; e++) {
            b[e] = setNa();
        }
        setGenotypes(b);
    }

    protected Individual[] individuals;
    protected byte[] genotypes;

    public void setGenotypes(byte[] genotypes) {
        this.genotypes = genotypes;
    }

    /**
     * Set the genotype g in individual i
     *
     * @param i
     * @param g
     */
    @Override
    public void setGenotypeJinIndividualI(int i, byte g) {
        genotypes[i] = g;
    }

    /**
     * Compute the genotype out of the haplotype
     *
     * @param individual
     * @param hap
     */
    public void set_haplotypes_at_Individual(int individual, byte[] hap) {
        setGenotypeJinIndividualI(individual, (byte) (hap[0] + hap[1]));
    }    
    
    /**
     * Set the null genotype at individual
     * @param individual 
     */
    public void set_null_genotype(int individual)
    {
        setGenotypeJinIndividualI(individual, setNa());
    }
    
    @Override
    public byte getGenotypeOfIndividualI(int i) {
        return genotypes[i];
    }

    @Override
    public byte[] getGenotypes() {
        return genotypes;
    }

    public int compareTo(SNPGenotypeByteArray o) {
        return this.getSnp().compareTo(o.getSnp());
    }

    @Override
    public int size() {
        return genotypes.length;
    }

    /**
     * Add at the end of this SNP the individuals and genotypes of anotherSNP
     *
     * @param anotherSNP
     * @return true if the add was successful. False otherwise
     */
    public boolean addAtEnd(SNPGenotypeByteArray anotherSNP) {
        if (compareTo(anotherSNP) == 0) {

            try {
                // Case where the individuals have been defined outside the SNP
                Individual[] iA = anotherSNP.getIndividuals();
                Individual[] I2 = new Individual[individuals.length + iA.length];
                System.arraycopy(individuals, 0, I2, 0, individuals.length);
                System.arraycopy(iA, 0, I2, individuals.length, iA.length);
                setIndividuals(I2);
            } catch (NullPointerException p) {
            }

            byte[] gA = anotherSNP.getGenotypes();
            byte[] g2 = new byte[genotypes.length + gA.length];
            System.arraycopy(genotypes, 0, g2, 0, genotypes.length);
            System.arraycopy(gA, 0, g2, genotypes.length, gA.length);
            setGenotypes(g2);
            return true;
        }
        return false;
    }

    /**
     * Set the allele A as B and B as A. Genotypes that before were 0 now will
     * be 2 and vice-versa
     *
     * @return
     */
    public SNPGenotypeByteArray shiftAlleles() {
        return new SNPGenotypeByteArray(getSnp().shiftAlleles(), getIndividuals(), shiftAllele(genotypes));
    }

    /**
     * Return the shifted allele
     * @param g
     * @return 
     */
    public byte shiftAlleles(byte g)
    {
        return (byte)(2-g);
    }
    
    /**
     * Shift the genotype using the first allele as the reference allele
     * @param g
     * @return 
     */
    public byte [] shiftAllele(byte [] g)
    {
        byte[] sGenotypes = new byte[g.length];
        for (int e = 0; e < g.length; e++) {
            if (g[e] >= 0 && !isNa(g[e])) {
                sGenotypes[e] = (byte) (2 - g[e]);
            } else {
                sGenotypes[e] = g[e];
            }
        }
        return sGenotypes;
    }
    
    /**
     * Set the allele A as B and B as A. Genotypes that before were 0 now will
     * be 2 and vice-versa
     *
     * @return
     */
    public SNPGenotypeByteArray shiftAllelesIgnoreIndividuals() {
        byte[] sGenotypes = new byte[genotypes.length];
        for (int e = 0; e < genotypes.length; e++) {
            if (genotypes[e] >= 0 && !isNa(genotypes[e])) {
                sGenotypes[e] = (byte) (2 - genotypes[e]);
            } else {
                sGenotypes[e] = genotypes[e];
            }
        }
        return new SNPGenotypeByteArray(getSnp().shiftAlleles(), sGenotypes);
    }

    /**
     * Extract the genotype of the individuals of interest
     *
     * @param l
     * @return
     */
    public SNPGenotypeByteArray extractIndividuals(List<Integer> l) {
        byte[] ng = new byte[l.size()];
        int c = 0;
        for (int i : l) {
            ng[c] = genotypes[i];
            c++;
        }
        return new SNPGenotypeByteArray(getSnp(), ng);
    }

        /**
     * Extract the genotype of the individuals of interest
     *
     * @param l
     * @return
     */
    public SNPGenotypeByteArray extractIndividuals(int [] l) {
        byte[] ng = new byte[l.length];
        int c = 0;
        for (int i : l) {
            ng[c] = genotypes[i];
            c++;
        }
        return new SNPGenotypeByteArray(getSnp(), ng);
    }
    
    @Override
    public boolean isNa(byte g) {
        return g > 2;
    }

    @Override
    public byte setNa() {
        return 3;
    }
    
    public boolean is_polymorphic()
    {
        double f = 0;
        double n = 0;
        for(byte gg:genotypes)
        {
            if(!isNa(gg))
            {
                f+=gg;
                n+=2;                        
            }
        }
        f/=n;
        return f*(1-f)!=0;
    }
    
    
    /**
     * Get an hemizygote representation by setting the haplotype to 0 and 2. In the case of a SNPGenotypeByteArray, do nothing
     * @return 
     */
    public SNPGenotypeByteArray generateHemizygote()
    {
        return this;
    }
    
    public SNPGenotypeByteArray getStructure()
    {
        return new SNPGenotypeByteArray(getSnp(),genotypes);
    }
}
