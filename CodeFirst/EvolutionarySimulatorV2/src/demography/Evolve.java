/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demography;

import event.HistoryOfEventsEvolutionarySimulator;
import exception.ExceptionDemography;
import exception.ExceptionFragment;
import exception.ExceptionGenome;
import exception.ExceptionIndividual;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public abstract class Evolve {
    public Evolve(Population name, HistoryOfEventsEvolutionarySimulator history)
    {
        this.name = name;
        this.history = history;
    }

    private final Population name;
    protected final HistoryOfEventsEvolutionarySimulator history;
    protected ArrayList<IndividualSample> sampleIndividuals = new ArrayList<>();

    /**
     * Get the name of the Demography element
     * @return
     */
    public Population getPopulation() {
        return name;
    }


    /**
     * Get the parameters genomic
     * @return the parameters genomic
     */
    public HistoryOfEventsEvolutionarySimulator getHistory() {
        return history;
    }

    /**
     * Get a sample of n individuals from this population
     * @return
     */
    public ArrayList<IndividualSample> getSampleIndividual()
    {
        return sampleIndividuals;
    }


    /**
     * Initialize the Demographic object
     * @throws ExceptionFragment
     * @throws ExceptionIndividual
     * @throws ExceptionGenome 
     */
    protected abstract void initialize() throws ExceptionFragment, ExceptionIndividual, ExceptionGenome;
    

    /**
     * Produce a next generation
     * @param si
     * @return
     * @throws demography.DemographyException
     */
    public abstract void generatePreviousGeneration() throws ExceptionIndividual, ExceptionGenome, ExceptionFragment, ExceptionDemography;
}
