/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import demography.Population;
import exception.ExceptionDemography;
import parametersSimulation.parameters.ModelParameter;

/**
 *
 * @author olao
 */
public class EventEvolutionaryBlockPopulation extends EventEvolutionarySimulator {

    public EventEvolutionaryBlockPopulation(ModelParameter generation, Population source) {
        super(generation, source);
    }

    @Override
    public void doEvent() throws ExceptionDemography {
        getPopulation().block();
    }

    @Override
    public String getNameOfEvent() {
        StringBuilder a = new StringBuilder();
        a.append("Block ").append(getWhenOccurred()).append(" ").append(getPopulation().getName());
        return a.toString();        
    }        
}
