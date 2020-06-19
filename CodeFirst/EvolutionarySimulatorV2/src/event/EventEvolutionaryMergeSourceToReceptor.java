/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import demography.Population;
import demography.Population_MigrationRate;
import exception.ExceptionDemography;
import maths.FixedValue;
import parametersSimulation.parameters.ModelParameter;

/**
 *
 * @author olao
 */
public class EventEvolutionaryMergeSourceToReceptor extends EventEvolutionarySentMigrantsBackward implements EventThatBlocksSourcePopulation {

    public EventEvolutionaryMergeSourceToReceptor(ModelParameter generation, Population source, Population receptor) throws ExceptionDemography {
        super(generation, source, new Population_MigrationRate[1]);
        this.descendent[0] = new Population_MigrationRate(receptor, new ModelParameter(new FixedValue(1.0)));
    }

    public EventEvolutionaryMergeSourceToReceptor(ModelParameter generation, Population source, Population receptor, Population population_ancestry) throws ExceptionDemography {
        super(generation, source, new Population_MigrationRate[1], population_ancestry);
        this.descendent[0] = new Population_MigrationRate(receptor, new ModelParameter(new FixedValue(1.0)));
    }

    @Override
    public void doEvent() throws ExceptionDemography {
// Source will not be active for receiving or sending migrants from/to other populations backward in time
        for (Population pops_that_send_migrants_to_source : getPopulation().getPopulations_to_which_this_population_sends_migrants_forward_in_time()) {
// Populations that send migrants to source are not going to be able to send them anymore (because this population collapses with receptor)
            pops_that_send_migrants_to_source.getProbabilityThatParentsAreFromSourcePopulations().remove(getPopulation());       
        }        
        getPopulation().setProbabilityThatParentsAreFromSourceOfMigrants(descendent);
        getPopulation().stopEvolvingAfterNextGeneration();
    }
}
