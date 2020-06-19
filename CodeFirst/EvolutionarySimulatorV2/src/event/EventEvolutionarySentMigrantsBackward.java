/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package event;

import demography.Population;
import demography.Population_MigrationRate;
import exception.ExceptionDemography;
import parametersSimulation.parameters.ModelParameter;

/**
 *
 * @author oscar
 */
public class EventEvolutionarySentMigrantsBackward extends EventEvolutionarySimulator {

    public EventEvolutionarySentMigrantsBackward(ModelParameter generation, Population source, Population_MigrationRate [] descendent) {
        super(generation, source);
        this.descendent = descendent;
        for (Population_MigrationRate pmr : descendent) {
            if (pmr!=null && pmr.getMigration_rate().getFg().getGeneratedNumber().doubleValue() > 0) {
                ModelParameter mp = new ModelParameter(source.getName() + "->" + pmr.getPopulation().getName(), pmr.getMigration_rate().getFg());
                parameters_in_this_event.add(mp);
            }
        }
    }

    public EventEvolutionarySentMigrantsBackward(ModelParameter generation, Population source, Population_MigrationRate[] descendent, Population population_ancestry) {
        super(generation, source);
        this.descendent = descendent;
        for (Population_MigrationRate pmr : descendent) {
            if (pmr!=null && pmr.getMigration_rate().getFg().getGeneratedNumber().doubleValue() > 0) {
                ModelParameter mp = new ModelParameter(source.getName() + "->" + pmr.getPopulation().getName(), pmr.getMigration_rate().getFg());
                parameters_in_this_event.add(mp);
            }
        }
        this.population_ancestry = population_ancestry;
    }    
    
    protected final Population_MigrationRate[] descendent;
    private Population population_ancestry;

    /**
     * Population to be used to label the Fragments
     * @return 
     */
    public Population getPopulation_ancestry() {
        return population_ancestry;
    }
    
    @Override
    public void doEvent() throws ExceptionDemography {
        getPopulation().setProbabilityThatParentsAreFromSourceOfMigrants(descendent);
    }

    @Override
    public String toString() {
        StringBuilder a = new StringBuilder();
        a.append(getWhenOccurred()).append(" ").append(getPopulation().getName()).append(" ->");
        for (Population_MigrationRate pm : descendent) {
            a.append(" ").append(pm.getPopulation().getName()).append(" ").append(pm.getMigration_rate().toString());
        }
        return a.toString();
    }

    @Override
    public String getNameOfEvent() {
        StringBuilder a = new StringBuilder();
        a.append("Migration ").append(getWhenOccurred()).append(" ").append(getPopulation().getName()).append("->");
        for (Population_MigrationRate pmr : descendent) {
            a.append(pmr.getPopulation().getName()).append(",").append(pmr.getMigration_rate().getFg().getGeneratedNumber().doubleValue());
        }
        return a.toString();
    }
}
