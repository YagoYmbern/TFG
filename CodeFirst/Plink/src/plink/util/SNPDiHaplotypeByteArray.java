/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util;

import java.util.List;
import plink.util.base_conversion.BinaryTransformer;
import plink.util.demography.Individual;

/**
 *
 * @author olao
 */
public class SNPDiHaplotypeByteArray extends SNPGenotypeByteArray {

    /**
     * Create an diploide haplotype based SNP
     *
     * @param snp
     * @param individuals
     * @param haplotype 0 -> 0 0 | 1 -> 0 1 | 2 -> 1 0 | 3 -> 1 1 \ 4 -> NA
     */
    public SNPDiHaplotypeByteArray(SNPPlink snp, Individual[] individuals, byte[] haplotype) {
        super(snp, individuals, haplotype);
    }

    /**
     * Create a diploide haplotype based SNP
     *
     * @param snp
     * @param haplotype 0 -> 0 0 | 1 -> 0 1 | 2 -> 1 0 | 3 -> 1 1 \ 4 -> NA
     */
    public SNPDiHaplotypeByteArray(SNPPlink snp, byte[] haplotype) {
        super(snp, haplotype);
    }

    /**
     * Create an diploide haplotype based SNP
     *
     * @param snp
     * @param size number of individuals
     */
    public SNPDiHaplotypeByteArray(SNPPlink snp, int size) {
        super(snp, size);
    }

    /**
     * Set the haplotypes at SNP
     *
     * @param individual
     * @param hap
     */
    @Override
    public void set_haplotypes_at_Individual(int individual, byte[] hap) {
        setGenotypeJinIndividualI(individual, (byte) BinaryTransformer.byteDecode(hap));
    }

    /**
     * Get the haplotype of the individual
     *
     * @param i
     * @return
     */
    public byte[] get_haplotype_individual(int i) {
        return BinaryTransformer.byteCode(genotypes[i], 2);
    }

    /**
     * Get the two haplotypes by individual
     *
     * @return
     */
    public byte[] get_haplotypes() {
        byte[] haps = new byte[2 * genotypes.length];
        for (int i = 0; i < genotypes.length; i++) {
            if (isNa(genotypes[i])) {
                byte[] h = get_haplotype_individual(i);
                haps[2 * i] = h[0];
                haps[2 * i + 1] = h[1];

            } else {
                haps[2 * i] = this.setNa();
                haps[2 * i + 1] = this.setNa();
            }
        }
        return haps;
    }

    /**
     * Get the genotypes by adding the two haplotypes
     *
     * @return
     */
    @Override
    public byte[] getGenotypes() {
        byte[] g = new byte[this.genotypes.length];
        for (int i = 0; i < genotypes.length; i++) {
            if (isNa(genotypes[i])) {
                byte[] h = BinaryTransformer.byteCode(genotypes[i], 2);
                g[i] = h[0];
                g[i] += h[1];

            } else {
                g[i] = this.setNa();
            }
        }
        return g;
    }

    /**
     * Set the allele A as B and B as A. Haplotypes that before were 0 now will
     * be 3 and vice-versa
     *
     * @return
     */
    @Override
    public SNPGenotypeByteArray shiftAlleles() {
        byte[] sGenotypes = new byte[genotypes.length];
        for (int e = 0; e < genotypes.length; e++) {
            sGenotypes[e] = (byte) (!isNa(genotypes[e]) ? (3 - genotypes[e]) : setNa());

        }
        return new SNPDiHaplotypeByteArray(getSnp().shiftAlleles(), getIndividuals(), sGenotypes);
    }

    /**
     * Set the allele A as B and B as A. Genotypes that before were 0 now will
     * be 2 and vice-versa
     *
     * @return
     */
    @Override
    public SNPGenotypeByteArray shiftAllelesIgnoreIndividuals() {
        byte[] sGenotypes = new byte[genotypes.length];
        for (int e = 0; e < genotypes.length; e++) {
            sGenotypes[e] = (byte) (!isNa(genotypes[e]) ? (3 - genotypes[e]) : setNa());
        }
        return new SNPDiHaplotypeByteArray(getSnp().shiftAlleles(), sGenotypes);
    }

    @Override
    public boolean isNa(byte g) {
        return g > 4;
    }

    @Override
    public byte setNa() {
        return 4;
    }

    /**
     * Extract the genotype of the individuals of interest
     *
     * @param l
     * @return
     */
    @Override
    public SNPGenotypeByteArray extractIndividuals(List<Integer> l) {
        byte[] ng = new byte[l.size()];
        int c = 0;
        for (int i : l) {
            ng[c] = genotypes[i];
            c++;
        }
        return new SNPDiHaplotypeByteArray(getSnp(), ng);
    }

        /**
     * Extract the genotype of the individuals of interest
     *
     * @param l
     * @return
     */
    @Override
    public SNPGenotypeByteArray extractIndividuals(int [] l) {
        byte[] ng = new byte[l.length];
        int c = 0;
        for (int i : l) {
            ng[c] = genotypes[i];
            c++;
        }
        return new SNPDiHaplotypeByteArray(getSnp(), ng);
    }    
    
    /**
     * Get an hemizygote representation by setting the haplotype to 0 and 2. In
     * the case of a SNPGenotypeByteArray, do nothing
     *
     * @return
     */
    @Override
    public SNPGenotypeByteArray generateHemizygote() {
        byte[] g = new byte[genotypes.length * 2];
        for (int i = 0; i < genotypes.length; i++) {
            if (!isNa(genotypes[i])) {
                byte[] h = get_haplotype_individual(i);
                g[i*2] = (byte)(2*h[0]);
                g[i*2+1] = (byte)(2*h[1]);
            } else {
                g[i * 2] = 3;
                g[i * 2 + 1] = 3;
            }
        }
        SNPGenotypeByteArray ab = new SNPGenotypeByteArray(getSnp(), g);
        return ab;
    }
    
    @Override
        public SNPGenotypeByteArray getStructure()
    {
        return new SNPGenotypeByteArray(getSnp(),genotypes);
    }
}
