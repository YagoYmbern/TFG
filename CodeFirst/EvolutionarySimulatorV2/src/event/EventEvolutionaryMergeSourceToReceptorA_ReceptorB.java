/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import demography.Population;
import demography.Population_MigrationRate;
import maths.FixedValue;
import parametersSimulation.parameters.ModelParameter;

/**
 *
 * @author olao
 */
public class EventEvolutionaryMergeSourceToReceptorA_ReceptorB extends EventEvolutionarySentMigrantsBackward{

    public EventEvolutionaryMergeSourceToReceptorA_ReceptorB(ModelParameter generation, Population source, Population receptorA, Population receptorB, ModelParameter migration_source_to_A) {
        super(generation, source, new Population_MigrationRate[2]);
        this.descendent[0] = new Population_MigrationRate(receptorA, migration_source_to_A);
        this.descendent[1] = new Population_MigrationRate(receptorB, new ModelParameter(new FixedValue(1.0).minus_Double(migration_source_to_A.getFg())));   
    }

    public EventEvolutionaryMergeSourceToReceptorA_ReceptorB(ModelParameter generation, Population source, Population receptorA, Population receptorB, ModelParameter migration_source_to_A, Population population_ancestry) {
        super(generation, source, new Population_MigrationRate[1], population_ancestry);
        this.descendent[0] = new Population_MigrationRate(receptorA, migration_source_to_A);
        this.descendent[1] = new Population_MigrationRate(receptorB, new ModelParameter(new FixedValue(1.0).minus_Double(migration_source_to_A.getFg())));      }               
}
