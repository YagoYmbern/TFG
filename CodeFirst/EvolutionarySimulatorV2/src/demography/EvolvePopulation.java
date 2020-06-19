/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demography;

import event.HistoryOfEventsEvolutionarySimulator;
import exception.ExceptionDemography;
import locus.Chromosome;
import exception.ExceptionFragment;
import exception.ExceptionGenome;
import exception.ExceptionIndividual;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeSet;
import locus.Fragment;
import locus.Sequence;
import maths.FixedValue;
import parametersSimulation.parameters.ModelParameter;

/**
 *
 * @author oscar_000
 */
public class EvolvePopulation extends Evolve {

    /**
     * Create a new population
     *
     * @param pop is the name of the population
     * @param history is the history to simulate with this population
     * @throws ExceptionFragment
     * @throws ExceptionIndividual
     * @throws ExceptionGenome
     */
    public EvolvePopulation(Population pop, HistoryOfEventsEvolutionarySimulator history) throws ExceptionFragment, ExceptionIndividual, ExceptionGenome {
        super(pop, history);
        history.getPg().forEach((pC) -> {
            pop.forEach((sat) -> {
                pC.addSampleSize(sat.getSamples());
            });
        });
        pop.setEvolvePopulation(this);
        initialize();
    }
// Individuals in current generation
    private ArrayList<Individual> ccIndividuals = new ArrayList<>();
// Father and mothers in memory in current generation    
    private ArrayList<Individual> fathers_in_memory = new ArrayList<>();
    private ArrayList<Individual> mothers_in_memory = new ArrayList<>();
    private ArrayList<Samples_at_time> samples_to_be_added;
    private final Random r = new Random();
    private int counter_generations_backward = 0;

    /**
     * Individuals that have not coalesced
     *
     * @return
     */
    public int currentIndividualsToCoalesce() {
        return ccIndividuals.size();
    }

    /**
     * Get the current individuals that have at least a fragment that has not
     * coalesced
     *
     * @return
     */
    public ArrayList<Individual> getCcIndividuals() {
        return ccIndividuals;
    }

    /**
     * Remove from this population k current individuals
     *
     * @param k
     * @return
     */
    public ArrayList<Individual> removeKCcIndividuals(int k) {
        Collections.shuffle(ccIndividuals);
        ArrayList<Individual> removed_individuals = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            removed_individuals.add(ccIndividuals.get(i));
        }
        ArrayList<Individual> retained_individuals = new ArrayList<>();
        for (int i = k; i < ccIndividuals.size(); i++) {
            retained_individuals.add(ccIndividuals.get(i));
        }

        ccIndividuals = retained_individuals;
        return removed_individuals;
    }

    /**
     * Initialize the population and the individuals that are sampled
     *
     * @throws ExceptionFragment
     * @throws ExceptionIndividual
     * @throws ExceptionGenome
     */
    @Override
    protected void initialize() throws ExceptionFragment, ExceptionIndividual, ExceptionGenome {
        samples_to_be_added = new ArrayList<>();
        samples_to_be_added.addAll(getPopulation());
        sampleIndividuals = new ArrayList<>();
        // Add samples at time 0
        while (!samples_to_be_added.isEmpty() && samples_to_be_added.get(0).getTime_backward_in_time() == 0) {
            for (int i = 0; i < samples_to_be_added.get(0).getSamples(); i++) {
                addIndividual();
            }
            samples_to_be_added.remove(0);
        }
    }

    /**
     * Add a sampled individual to this population
     *
     * @param i
     * @throws ExceptionFragment
     * @throws ExceptionIndividual
     * @throws ExceptionGenome
     */
    private void addIndividual() throws ExceptionFragment, ExceptionIndividual, ExceptionGenome {
        Individual ind = generateNewIndividual();
        ccIndividuals.add(ind);
    }

    /**
     * sampled individual to this population
     *
     * @param i
     * @throws ExceptionFragment
     * @throws ExceptionIndividual
     * @throws ExceptionGenome
     */
    private Individual generateNewIndividual() throws ExceptionFragment, ExceptionIndividual, ExceptionGenome {
        IndividualSample ind = new IndividualSample(this.getPopulation(), history.getPg());
        IndividualSample sample = new IndividualSample(this.getPopulation(), history.getPg());
        sampleIndividuals.add(sample);
        for (int g = 0; g < history.getPg().size(); g++) {
            for (int l = 0; l < 2; l++) {
                Sequence seq = new Sequence(this.getPopulation(), (short) sampleIndividuals.size(), history.getPg().get(g).getMutationList());
                Fragment f = new Fragment(0, history.getPg().get(g).getChromosomeLength());
                f.add(seq);
                ind.getGenome(g).setChromosome(l, new Chromosome());
                sample.getGenome(g).setChromosome(l, new Chromosome());
                ind.getGenome(g).getChromosome(l).add(f);
                Fragment fs = new Fragment(0, history.getPg().get(g).getChromosomeLength());
                fs.add(seq);
                sample.getGenome(g).getChromosome(l).add(fs);
            }
        }
        return ind;
    }

    /**
     * Sample a father from this population at this generation
     *
     * @return
     * @throws ExceptionDemography
     */
    public Individual sampleAFather() throws ExceptionDemography {
        if (getPopulation().isActive()) {
            Individual father;
            double f_f = fathers_in_memory.size() / (double) getPopulation().getParameterDemography().getEffectivePopulationSizeOfOneParent().getFg().getGeneratedNumber().intValue();
            boolean father_already_in_memory = r.nextDouble() <= f_f;
            if (father_already_in_memory) {
                int f = r.nextInt(fathers_in_memory.size());
                father = fathers_in_memory.get(f);
            } else {
                father = new Individual(this.getPopulation(), history.getPg());
                fathers_in_memory.add(father);
            }
            return father;
        }
        throw new ExceptionDemography("Population " + getPopulation().getName() + " is not active");
    }

    /**
     * Sample a mother from this population at this generation
     *
     * @return
     * @throws ExceptionDemography
     */
    public Individual sampleAMother() throws ExceptionDemography {
        if (getPopulation().isActive()) {
            Individual mother;
            // Fraction of mothers that are already in memory
            double f_m = mothers_in_memory.size() / (double) getPopulation().getParameterDemography().getEffectivePopulationSizeOfOneParent().getFg().getGeneratedNumber().intValue();
            boolean mother_already_in_memory = r.nextDouble() <= f_m;
            // Sample a mother at random from the mothers that are in memory
            if (mother_already_in_memory) {
                int f = r.nextInt(mothers_in_memory.size());
                mother = mothers_in_memory.get(f);
            } else {
                // Create a new mother and add it to the mothers in memory
                mother = new Individual(this.getPopulation(), history.getPg());
                mothers_in_memory.add(mother);
            }
            return mother;
        }
        throw new ExceptionDemography("Population " + getPopulation().getName() + " is not active");
    }

    /**
     * Propose a new backward generation on the individuals that contain
     * fragments that have not yet coalesced
     *
     * @throws ExceptionIndividual
     * @throws ExceptionGenome
     * @throws ExceptionFragment
     * @throws ExceptionDemography
     */
    @Override
    public void generatePreviousGeneration() throws ExceptionIndividual, ExceptionGenome, ExceptionFragment, ExceptionDemography {
        if (getPopulation().isActive()) {
// Check if there are ancient samples to be added
            // Add samples at time current generation as one of the putative father-mother of ccIndividuals
            while (!samples_to_be_added.isEmpty() && samples_to_be_added.get(0).getTime_backward_in_time() == counter_generations_backward) {                
                for (int i = 0; i < samples_to_be_added.get(0).getSamples(); i++) {
                    Individual ancient = generateNewIndividual();
                    
                    if (r.nextBoolean()) {
                        fathers_in_memory.add(ancient);
                    } else {
                        mothers_in_memory.add(ancient);
                    }
                }
                samples_to_be_added.remove(0);
            }

            for (Individual i : ccIndividuals) {
// Take parents            
                Individual father = null;
                Individual mother = null;
// This population receives migrants from other populations                
                if (!getPopulation().getProbabilityThatParentsAreFromSourcePopulations().isEmpty()) {
// Sample the population of the parents using the migrant vector of probabilities
                    Population[] pops_of_parents = getPopulation().getProbabilityThatParentsAreFromSourcePopulations().sample(2);
                    father = pops_of_parents[0].getEvolvePopulation().sampleAFather();
                    mother = pops_of_parents[1].getEvolvePopulation().sampleAMother();
                } else {
// This population does not receive migrants from other populations                    
                    father = this.sampleAFather();
                    mother = this.sampleAMother();
                }
// Set the current individual as offspring of the parents
                mate(i, father, mother);
            }
        }
    }

    /**
     * After a generatePreviousGeneration, one must call
     * update_previous_generation to load the fathers and mothers of the
     * previous generation as the new generation
     *
     * @throws ExceptionIndividual
     * @throws ExceptionGenome
     * @throws ExceptionFragment
     * @throws ExceptionDemography
     */
    public void update_previous_generation() throws ExceptionIndividual, ExceptionGenome, ExceptionFragment, ExceptionDemography {
// New individuals in the next backward generation are the parents. Add them to the ccIndividuals
        if (getPopulation().isActive()) {
            ccIndividuals = new ArrayList<>();
            for (Individual i : fathers_in_memory) {
                updateCCIndividuals(i);
            }
            for (Individual i : mothers_in_memory) {
                updateCCIndividuals(i);
            }
// Update population size given current growht rate  
            double growth = Math.exp(getPopulation().getParameterDemography().getGrowthRate().getFg().getGeneratedNumber().doubleValue());
            int new_size = (int) (getPopulation().getParameterDemography().getEffectivePopulationSizeOfOneParent().getFg().getGeneratedNumber().intValue() * growth);
            getPopulation().getParameterDemography().setEffectivePopulationSizeOfOneParent(new ModelParameter("Effective population size", new FixedValue(new_size)));
// Add one generation backward
            counter_generations_backward++;
// The generation of the parents going backward is emptied
            fathers_in_memory = new ArrayList<>();
            mothers_in_memory = new ArrayList<>();
            if (this.getPopulation().isPreparedToStopEvolving()) {
                getPopulation().block();
            }
        } else {
            throw new ExceptionDemography("Population " + getPopulation().getName() + " is not active");
        }
    }

    /**
     * Update the list of current individuals
     *
     * @param i
     * @return
     * @throws ExceptionIndividual
     * @throws ExceptionGenome
     * @throws ExceptionFragment
     */
    private boolean updateCCIndividuals(Individual i) throws ExceptionIndividual, ExceptionGenome, ExceptionFragment {
// Go through all the genomes
        for (int g = 0; g < i.size(); g++) {
            if (i.getGenome(g) != null) {
// Go through all the chromosomes within each genome
                for (int l = 0; l < 2; l++) {
                    if (i.getGenome(g).getChromosome(l) != null) {
                        TreeSet<Fragment> fragments = i.getGenome(g).getChromosome(l);
// If there is one fragment that is still active, add the individual to the current list of individuals.                       
                        if (fragments.size() > 0) {
                            ccIndividuals.add(i);
                            return true;
                        }
                    }
                }
            }
        }
// The individual is empty. Do not include it in the list of ccIndividuals        
        return false;
    }

    /**
     * Mate f and m to produce i in backward
     *
     * @param i
     * @param f
     * @param m
     * @throws ExceptionIndividual
     * @throws ExceptionGenome
     * @throws ExceptionFragment
     */
    private void mate(Individual i, Individual f, Individual m) throws ExceptionIndividual, ExceptionGenome, ExceptionFragment {
        // For all the independent genomic regions
        for (int g = 0; g < i.size(); g++) {
            double mutation_rate = history.getPg().get(g).getMutationRate();
            double recombination_rate = history.getPg().get(g).getRecombinationRate();
            // If the individual has a Genome that has not yet completely coalesced
            int parentRandom = r.nextInt(2);
            // If the Chromosome is not null
            if (!i.getGenome(g).getChromosome(parentRandom).isEmpty()) {
                if (i.getPopulation().shallICheckTheAncestralityInThisPopulation() && !i.getPopulation().equals(f.getPopulation()) && f.getPopulation().traceAncestralityFromThisPopulationInOtherPopulations()) {
                    i.getGenome(g).getChromosome(parentRandom).addAncestry(f.getPopulation());
                }
                i.getGenome(g).getChromosome(parentRandom).comesFrom(f.getGenome(g).getChromosome(0), f.getGenome(g).getChromosome(1), mutation_rate, recombination_rate, 2 * history.samplesToSimulate());
            }
            if (!i.getGenome(g).getChromosome(1 - parentRandom).isEmpty()) {
                if (i.getPopulation().shallICheckTheAncestralityInThisPopulation() && !i.getPopulation().equals(m.getPopulation()) && m.getPopulation().traceAncestralityFromThisPopulationInOtherPopulations()) {
                    i.getGenome(g).getChromosome(1 - parentRandom).addAncestry(m.getPopulation());
                }
                i.getGenome(g).getChromosome(1 - parentRandom).comesFrom(m.getGenome(g).getChromosome(0), m.getGenome(g).getChromosome(1), mutation_rate, recombination_rate, 2 * history.samplesToSimulate());
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EvolvePopulation) {
            return this.getPopulation().equals(((EvolvePopulation) o).getPopulation());
        }
        return false;
    }

//    public static void main(String[] args) throws ExceptionFragment, ExceptionGenome, ExceptionIndividual, DemographyException {
//        ParametersGenome pG = new ParametersGenome();
//        pG.add(new ParametersChromosome(1.25 * Math.pow(10, -7), 1.8 * Math.pow(10, -8), 1000000, new MutationList()));
//        Population p = new Population("POPA", 1, pG);
//        p.setEffectivePopulationSizeParentOneInIndividuals(1000);
//        int r = 0;
//        while (p.currentIndividualsToCoalesce() > 0) {
//            System.out.println("Size " + p.currentIndividualsToCoalesce());
////            if (p.currentIndividualsToCoalesce() == 2) {
////                ArrayList<Individual> inds = p.getCcIndividuals();
////                for (Individual i : inds) {
////                    for (int g = 0; g < i.size(); g++) {
////                        Genome gen = i.getGenome(g);
////                        System.out.println("CHR 0 " + gen.getChromosome(0));
////                        System.out.println("CHR 1 " + gen.getChromosome(1));
////                    }
////                    System.out.println("**************************");
////                }
////                System.exit(0);
////            }
//            p.nextBackwardGeneration();
//            r++;
//        }
//        
//        ArrayList<Individual> samples = p.getSampleIndividual();
//        for(Individual i:samples)
//        {
//            Collections.sort(i.getGenome(0).getChromosome(0).get(0).getSequences().get(0));
//            System.out.println(i.getGenome(0).getChromosome(0).get(0).getSequences().get(0));
//            Collections.sort(i.getGenome(0).getChromosome(1).get(0).getSequences().get(0));            
//            System.out.println(i.getGenome(0).getChromosome(1).get(0).getSequences().get(0));
//        }
//    }
    @Override
    public int hashCode() {
        return getPopulation().hashCode();
    }
}
