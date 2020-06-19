/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demography;

import exception.ExceptionDemography;
import java.util.ArrayList;

/**
 *
 * @author Oscar Lao
 */
public class Population extends ArrayList<Samples_at_time> {

    /**
     * Create a new population
     * @param name the name of the population
     * @param parameterDemography the present demographic parameters of this population (effective population size)
     * @param shallICheckTheAncestralityInThisPopulation
     * @param traceAncestralityFromThisPopulationInOtherPopulations A label indicating if the ancestrality of this population must be tracked
     * @throws ExceptionDemography 
     */
    public Population(String name, ParametersDemography parameterDemography, boolean shallICheckTheAncestralityInThisPopulation, boolean traceAncestralityFromThisPopulationInOtherPopulations) throws ExceptionDemography {
        this.name = name;
        this.traceAncestralityFromThisPopulationInOtherPopulations = traceAncestralityFromThisPopulationInOtherPopulations;
        this.parameterDemography = parameterDemography;
        sendMigrantsBackwardTo = new VectorOfMigrations(this);
        this.shallICheckTheAncestralityInThisPopulation = shallICheckTheAncestralityInThisPopulation;
    }
     

    protected final String name;
    private boolean isActive = true;
    private final ParametersDemography parameterDemography;
    private EvolvePopulation evolvePopulation = null;
    private final VectorOfMigrations sendMigrantsBackwardTo;
    private final ArrayList<Population> populations_that_send_backward_migrants_to_this_population = new ArrayList<>();
    private boolean prepareToBlock = false;
    private final boolean traceAncestralityFromThisPopulationInOtherPopulations;
    private final boolean shallICheckTheAncestralityInThisPopulation;

    /**
     * Return if the ancestralities present in this population must be checked
     * @return 
     */
    public boolean shallICheckTheAncestralityInThisPopulation() {
        return shallICheckTheAncestralityInThisPopulation;
    }
    
    
    /**
     * Retrieve if the ancestrality of this population must be traced in other populations
     * @return 
     */
    public boolean traceAncestralityFromThisPopulationInOtherPopulations() {
        return traceAncestralityFromThisPopulationInOtherPopulations;
    }
    
    
    /**
     * Prepare to block the population. After this generation it will be blocked
     */
    public void stopEvolvingAfterNextGeneration() {
        this.prepareToBlock = true;
    }
    
    

    /**
     * Is this population prepared to stop evolving?
     * @return 
     */
    public boolean isPreparedToStopEvolving() {
        return prepareToBlock;
    }
    
    /**
     * Add a population that forward in time sends migrants to this population
     * @param pop
     * @return 
     */
    public boolean addPopulation_that_sent_backward_migrants_to_this_population(Population pop)
    {
        int index = populations_that_send_backward_migrants_to_this_population.indexOf(pop);
        if(index < 0)
        {
            return populations_that_send_backward_migrants_to_this_population.add(pop);            
        }
        return false;
    }

    /**
     * Get the populations to which this population receives migrants backward in time. Forward in time it means that
     * the populations migrate to this population
     * @return 
     */
    public ArrayList<Population> getPopulations_to_which_this_population_sends_migrants_forward_in_time() {
        return populations_that_send_backward_migrants_to_this_population;
    }        

    /**
     * Set the probability of migrants of this population to the other
     * populations backward in time
     *
     * @param sourceOfMigrants
     * @throws ExceptionDemography
     */
    public void setProbabilityThatParentsAreFromSourceOfMigrants(Population_MigrationRate [] sourceOfMigrants) throws ExceptionDemography {
        if (this.isActive()) {
            this.sendMigrantsBackwardTo.setMigration(sourceOfMigrants);
        } else {
            throw new ExceptionDemography("The population " + getName() + " is not active anymore");
        }
    }

    /**
     * Get the migrations backward in time associated to this population. Forward in time it means that this population receives immigrants from the populations
     * in Vector of Migrations
     * @return
     */
    public VectorOfMigrations getProbabilityThatParentsAreFromSourcePopulations() {
        return sendMigrantsBackwardTo;
    }

    /**
     * Set this evolving population
     *
     * @param evolvePopulation
     */
    public void setEvolvePopulation(EvolvePopulation evolvePopulation) {
        this.evolvePopulation = evolvePopulation;
    }

    /**
     * Get the evolve population
     *
     * @return
     */
    public EvolvePopulation getEvolvePopulation() {
        return evolvePopulation;
    }

    /**
     * Get the parameter demography associated to this population
     *
     * @return
     */
    public ParametersDemography getParameterDemography() {
        return parameterDemography;
    }

    /**
     * Add the samples sorted by time
     *
     * @param stt
     * @return
     */
    @Override
    public boolean add(Samples_at_time stt) {
        for (int i = 0; i < this.size(); i++) {
            if (stt.compareTo(this.get(i)) <= 0) {
                super.add(i, stt);
                return true;
            }
        }
        return super.add(stt);
    }

    /**
     * Get the name
     *
     * @return
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }


    /**
     * Stop evolving this population. All currentIndividuals are erased. No
     * further migrations, etc are allowed
     * @throws ExceptionDemography 
     */
    public void block() throws ExceptionDemography{
        this.isActive = false;
// All other populations are going to switch off the possibility to send migrants to this population
        for(Population pp:populations_that_send_backward_migrants_to_this_population)
        {
            pp.getProbabilityThatParentsAreFromSourcePopulations().remove(this);
        }        
    }

    /**
     * Is the population active
     *
     * @return
     */
    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Population) {
            return getName().equals(((Population) o).getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
