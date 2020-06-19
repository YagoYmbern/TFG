/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package event;

import demography.Population;
import exception.ExceptionDemography;
import maths.FixedValue;
import parametersSimulation.parameters.ModelParameter;

/**
 *
 * @author oscar
 */
public class EventEvolutionarySimulatorChangePopSize extends EventEvolutionarySimulator{
    public EventEvolutionarySimulatorChangePopSize(ModelParameter generation, Population population, ModelParameter size, ModelParameter growthRate)
    {
        super(generation,population);
        this.size = size;
        parameters_in_this_event.add(size);
        this.growthRate = growthRate;
        parameters_in_this_event.add(growthRate);
    }

    public EventEvolutionarySimulatorChangePopSize(ModelParameter generation, Population population, ModelParameter size)
    {
        super(generation,population);
        this.size = size;
        parameters_in_this_event.add(size);
        growthRate = new ModelParameter(new FixedValue(0.0));
    }
    
    private final ModelParameter size;
    private final ModelParameter growthRate;

    @Override
    public void doEvent() throws ExceptionDemography {
        getPopulation().getParameterDemography().setEffectivePopulationSizeOfOneParent(size);
        getPopulation().getParameterDemography().setGrowthRate(growthRate);
    }

    @Override
    public String toString()
    {
        return getWhenOccurred() + " " + getPopulation().getName() + " new Ne: " + size + " " + growthRate;
    }

    @Override
    public String getNameOfEvent() {
        return "Change Population Size of " + getPopulation().getName();
    }
}
