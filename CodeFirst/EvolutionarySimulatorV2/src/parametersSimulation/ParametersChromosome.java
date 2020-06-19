/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parametersSimulation;

import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Administrator
 */
public class ParametersChromosome {

    /**
     * Create a new ParametersChromosome
     *
     * @param mutationRate is the mutation rate of the region
     * @param recombinationRate is the recombination rate of the region
     * @param chromosomeLength is the chromosome length in bp
     * @param mutationList is the mutation list that will keep track on the mutations present in
     * the region
     */
    public ParametersChromosome(double mutationRate, double recombinationRate, int chromosomeLength, MutationList mutationList) {
        setChromosomeLength(chromosomeLength);
        setRecombinationRate(recombinationRate);
        setMutationRate(mutationRate);
        setMutationList(mutationList);
    }

    /**
     * Create a new ParametersChromosome
     *
     * @param chromosomeLength is the chromosome length
     * @param mutationList is the mutation list with the mutations present in
     * the region
     */
    public ParametersChromosome(int chromosomeLength, MutationList mutationList) {
        double mutation_rate = 1.61e-8;
        double sd_mutation_rate = 0.13e-8;
        mutationRate = sd_mutation_rate * ThreadLocalRandom.current().nextGaussian() + mutation_rate;
        setChromosomeLength(chromosomeLength);
        setMutationList(mutationList);
    }
    // From Lipson et al Plos Genetics 2015 1.61+0.13sd
    private double mutationRate = 0;
    private double recombinationRate = Math.pow(10, -8);
    private int chromosomeLength;
    private MutationList mutationList = new MutationList();
    private int totalSampleSize = 0;

    /**
     * Add a number of individuals to simulate
     *
     * @param sampleSizeIndividuals
     */
    public void addSampleSize(int sampleSizeIndividuals) {
        totalSampleSize += 2 * sampleSizeIndividuals;
    }

    /**
     * Set the mutation list in this chromosome.
     * @param mutationList 
     */
    public void setMutationList(MutationList mutationList) {
        this.mutationList = mutationList;
    }

    /**
     * Total number of haplotypes (NOT INDIVIDUALS!) to sample
     *
     * @return
     */
    public int getTotalHaplotypesSampleSize() {
        return totalSampleSize;
    }

    /**
     * The mutation list associated to this chromosome
     *
     * @return
     */
    public MutationList getMutationList() {
        return mutationList;
    }

    /**
     * Set the length of the chromosome in bp
     *
     * @param chromosomeLength
     */
    public void setChromosomeLength(int chromosomeLength) {
        this.chromosomeLength = chromosomeLength;
    }

    /**
     * Set the mutation rate (in bp)
     *
     * @param mutationRate
     */
    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    /**
     * Set the recombination rate in the chromosome
     * @param recombinationRate 
     */
    public void setRecombinationRate(double recombinationRate) {
        this.recombinationRate = recombinationRate;
    }

    /**
     * Get the chromosome length (in bp)
     * @return 
     */
    public int getChromosomeLength() {
        return chromosomeLength;
    }

    /**
     * Get the mutation rate
     * @return 
     */
    public double getMutationRate() {
        return mutationRate;
    }

    /**
     * Get the scaled mutation rate by chromosome length (mu*chromosome_length)
     *
     * @return
     */
    public double getScaledMutationRate() {
        return getMutationRate() * (double) getChromosomeLength();
    }

    /**
     * Get the recombination rate
     * @return 
     */
    public double getRecombinationRate() {
        return recombinationRate;
    }

    /**
     * Get the scaled recombination rate by chromosome length
     *
     * @return
     */
    public double getScaledRecombinationRate() {
        return getRecombinationRate() * (double) getChromosomeLength();
    }
}
