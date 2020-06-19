/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import demography.Population;
import demography.Population_MigrationRate;
import exception.ExceptionDemography;
import exception.ExceptionPopulation;
import java.util.ArrayList;
import maths.FixedValue;
import maths.Function;
import parametersSimulation.ParametersGenome;
import parametersSimulation.parameters.ModelParameter;

/**
 *
 * @author olao
 */
public class HistoryOfEventsEvolutionarySimulator extends ArrayList<EventEvolutionarySimulator> {

    /**
     * Create a new FastSimcoal Demography with a number of populations
     *
     * @param populations the populations that are going to be involved
     * @param pg
     */
    public HistoryOfEventsEvolutionarySimulator(ArrayList<Population> populations, ParametersGenome pg) {
        this.pg = pg;
        this.populations = populations;
        populations.forEach((pop) -> {
            ModelParameter modelNe = new ModelParameter("Ne_" + pop.getName(), pop.getParameterDemography().getEffectivePopulationSizeOfOneParent().getFg());
            mp.add(modelNe);
            if (!pop.getParameterDemography().growthIsEmpty()) {
                ModelParameter modelGrowth = new ModelParameter("Growth_" + pop.getName(), pop.getParameterDemography().getGrowthRate().getFg());
                mp.add(modelGrowth);
            }
        });
    }

// Genomic parameters to simulate
    private final ParametersGenome pg;
    // Populations so far in the model
    private final ArrayList<Population> populations;
    //Parameters so far in the model
    private final ArrayList<ModelParameter> mp = new ArrayList<>();

    /**
     * Add initial migration between source and receptor
     *
     * @param source
     * @param receptor
     * @param mop
     * @throws ExceptionDemography
     */
    public void addInitialMigrationBetween(String source, String receptor, Function mop) throws ExceptionDemography, ExceptionPopulation {
        Population p = retrievePopulation(source);
        Population pp = retrievePopulation(receptor);
        ModelParameter mig = new ModelParameter("migration " + p.getName() + "->" + pp.getName(), mop);
        Population_MigrationRate pmr = new Population_MigrationRate(pp, mig);
        p.getProbabilityThatParentsAreFromSourcePopulations().setMigration(pmr);
        mp.add(mig);
    }

    /**
     * Add initial migrations between source and receptors
     *
     * @param source
     * @param receptor
     * @param mop
     * @throws ExceptionDemography
     */
    public void addInitialMigrationBetween(Population source, Population[] receptor, Function[] mop) throws ExceptionDemography {
        for (int p = 0; p < receptor.length; p++) {
            ModelParameter mig = new ModelParameter("migration " + source.getName() + "->" + receptor[p].getName(), mop[p]);
            Population_MigrationRate pmr = new Population_MigrationRate(receptor[p], mig);
            source.getProbabilityThatParentsAreFromSourcePopulations().setMigration(pmr);
            mp.add(mig);
        }
    }

    /**
     * Get the populations
     *
     * @return
     */
    public ArrayList<Population> getPopulations() {
        return populations;
    }

    /**
     * Samples to simulate in this history
     *
     * @return
     */
    public int samplesToSimulate() {
        int t = 0;
        for (Population p : getPopulations()) {
            t += p.size();
        }
        return t;
    }

    /**
     * Get the parameters of the genome
     *
     * @return
     */
    public ParametersGenome getPg() {
        return pg;
    }

    @Override
    public boolean add(EventEvolutionarySimulator ev) {
        mp.addAll(ev.getParameters_in_this_event());
        return super.add(ev);
    }

    /**
     * Get the parameters associated to this model
     *
     * @return
     */
    public ArrayList<ModelParameter> getParametersInThisModel() {
        return mp;
    }

    public Population getPopulation(String name) {
        for (Population p : populations) {
            if (name.equals(p.getName())) {
                return p;
            }
        }

        return null;
    }

    private Population retrievePopulation(String name_pop) throws ExceptionPopulation {
        Population p = getPopulation(name_pop);
        if (p == null) {
            throw new ExceptionPopulation("Population " + name_pop + " is not loaded in this History!");
        }
        return p;
    }

    /**
     * Change the population size
     *
     * @param generation
     * @param name_pop
     * @param size
     * @param growthRate
     * @throws ExceptionPopulation
     */
    public void changePopulationSize(ModelParameter generation, String name_pop, ModelParameter size, ModelParameter growthRate) throws ExceptionPopulation {
        Population p = retrievePopulation(name_pop);
        this.add(new EventEvolutionarySimulatorChangePopSize(generation, p, size, growthRate));
    }

    /**
     * Migrate backward from source to other_pops
     *
     * @param generation
     * @param source
     * @param other_pops
     * @param migrations
     * @throws ExceptionPopulation
     */
    public void migrantsBackwardFromSourceToOtherPops(ModelParameter generation, String source, String[] other_pops, ModelParameter[] migrations) throws ExceptionPopulation {
        Population p = retrievePopulation(source);
        Population_MigrationRate[] sourceOfMigrants = new Population_MigrationRate[migrations.length];

        for (int i = 0; i < other_pops.length; i++) {
            Population pp = retrievePopulation(other_pops[i]);
            sourceOfMigrants[i] = new Population_MigrationRate(pp, migrations[i]);
        }
        EventEvolutionarySentMigrantsBackward ev = new EventEvolutionarySentMigrantsBackward(generation, p, sourceOfMigrants);
        this.add(ev);
    }

    /**
     * Merge the population source with recipient backward (the equivalent to do
     * a split forward)
     *
     * @param generation
     * @param source
     * @param recipient
     * @throws ExceptionPopulation
     */
    public void mergeSourceIntoRecipient(ModelParameter generation, String source, String recipient) throws ExceptionPopulation, ExceptionDemography {
        Population p = retrievePopulation(source);
        Population pp = retrievePopulation(recipient);
        this.add(new EventEvolutionaryMergeSourceToReceptor(generation, p, pp));
    }

    /**
     * Sent individuals to other pop in this generation (A single pulse
     * admixture process). It does NOT switch off the other migrations from
     * source to other pops, either updates the migration rate of popA (if it
     * already exchanges migrants with source) or adds it
     *
     * @param generation
     * @param source
     * @param popA
     * @param pdd fraction of migrants that go to popA. Take this proportion of
     * migrants from the proportion of migrants that admix in source
     * @throws ExceptionPopulation
     */
    public void mergeAPercentageOfSourceIntoRecipient(ModelParameter generation, String source, String popA, ModelParameter pdd) throws ExceptionPopulation {
        Population p = retrievePopulation(source);
        Population ppA = retrievePopulation(popA);
        Population_MigrationRate[] sourceOfMigrants_so_far = p.getProbabilityThatParentsAreFromSourcePopulations().getPdm();
// Check if the source already exchanges migrants with popA        
        int id_popA = -1;
        for (int i = 0; i < sourceOfMigrants_so_far.length; i++) {
            if (sourceOfMigrants_so_far[i].getPopulation().getName().equals(popA)) {
                id_popA = i;
                break;
            }
        }

        Population_MigrationRate[] sourceOfMigrants;
// It does not exchange migrants        
        if (id_popA < 0) {
            sourceOfMigrants = new Population_MigrationRate[sourceOfMigrants_so_far.length + 1];
        } else {
// it already exchanged migrants
            sourceOfMigrants = new Population_MigrationRate[sourceOfMigrants_so_far.length];
        }
        for (int e = 0; e < sourceOfMigrants_so_far.length; e++) {
            sourceOfMigrants[e] = new Population_MigrationRate(sourceOfMigrants_so_far[e].getPopulation(), sourceOfMigrants_so_far[e].getMigration_rate());
        }
        if (id_popA < 0) {
            sourceOfMigrants[sourceOfMigrants_so_far.length] = new Population_MigrationRate(ppA, pdd);
        } else {
            sourceOfMigrants[id_popA] = new Population_MigrationRate(ppA, pdd);
        }
// Update with the new migration rate matrix
        this.add(new EventEvolutionarySentMigrantsBackward(generation, p, sourceOfMigrants));
// The generation backward after this we stop the migration and we go back to the migration rate that existed before       
        this.add(new EventEvolutionarySentMigrantsBackward(new ModelParameter("Back migration of " + p.getName() + " to " + ppA.getName(), generation.getFg().add_Integer(new FixedValue(1))), p, sourceOfMigrants_so_far));
    }

    /**
     * Sent all the individuals to popA and B with probability pdd and 1-pdd
     * respectively
     *
     * @param generation
     * @param source
     * @param popA
     * @param popB
     * @param pdd fraction of migrants that go to popA (1-popA goes to B)
     * @throws ExceptionPopulation
     */
    public void mergeSourceIntoRecipients(ModelParameter generation, String source, String popA, String popB, ModelParameter pdd) throws ExceptionPopulation {
        Population p = retrievePopulation(source);
        Population ppA = retrievePopulation(popA);
        Population ppB = retrievePopulation(popB);
        this.add(new EventEvolutionaryMergeSourceToReceptorA_ReceptorB(generation, p, ppA, ppB, pdd));
    }
    //public void mergeSourceIntoRecipients2(ModelParameter generation, String source, String popA, String popB, String popC, ModelParameter pdd) throws ExceptionPopulation {
        //Population p = retrievePopulation(source);
        //Population ppA = retrievePopulation(popA);
        //Population ppB = retrievePopulation(popB);
        //Population ppC = retrievePopulation(popC);
        //this.add(new EventEvolutionaryMergeSourceToReceptorA_ReceptorB_ReceptorC(generation, p, ppA, ppB, popC, pdd));
    //}
    
    /**
     * Remove all the events of this list
     */
    public void resetEvents() {
        this.clear();
    }
}
