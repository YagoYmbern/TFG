/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import demography.Population;
import exception.ExceptionDemography;
import java.util.ArrayList;
import parametersSimulation.parameters.ModelParameter;

/**
 *
 * @author oscar
 */
public abstract class EventEvolutionarySimulator implements Comparable<EventEvolutionarySimulator> {

    /**
     * Create an event associated to population at generation
     *
     * @param generation
     * @param population
     */
    public EventEvolutionarySimulator(ModelParameter generation, Population population) {
        this.generation = generation;
        parameters_in_this_event.add(generation);
        setPopulation(population);
    }

    private final ModelParameter generation;
    private Population population;
    protected ArrayList<ModelParameter> parameters_in_this_event = new ArrayList<>();

    /**
     * Get the ModelParameters associated to this event
     * @return 
     */
    public ArrayList<ModelParameter> getParameters_in_this_event() {
        return parameters_in_this_event;
    }
    
    public void setPopulation(Population population) {
        this.population = population;
    }

    public Population getPopulation() {
        return population;
    }


    /**
     * Get the time when this event occurred
     *
     * @return
     */
    public ModelParameter getWhenOccurred() {
        return generation;
    }

    /**
     * Compare to other event. If the time of this event is smaller than the
     * other, then return -1. If both times are equal, then return 0. Otherwise
     * return 1.
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(EventEvolutionarySimulator o) {

        if (o.getWhenOccurred().getFg().getGeneratedNumber().intValue() < this.getWhenOccurred().getFg().getGeneratedNumber().intValue()) {
            return 1;
        }
        if (o.getWhenOccurred().getFg().getGeneratedNumber().intValue() == this.getWhenOccurred().getFg().getGeneratedNumber().intValue()) {
            return 0;
        }
        return -1;
    }

    public abstract void doEvent() throws ExceptionDemography;
    
    public abstract String getNameOfEvent();
}
