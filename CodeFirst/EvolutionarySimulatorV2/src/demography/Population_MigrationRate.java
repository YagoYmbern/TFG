/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demography;

import parametersSimulation.parameters.ModelParameter;

/**
 *
 * @author Oscar Lao
 */
public class Population_MigrationRate implements Comparable<Population_MigrationRate> {

    /**
     * Migration associated to this population
     * @param population
     * @param migration_rate 
     */
    public Population_MigrationRate(Population population, ModelParameter migration_rate) {
        this.population = population;
        this.migration_rate = migration_rate;
    }

    private final Population population;
    private ModelParameter migration_rate;

    public Population_MigrationRate(Population receptorC, ModelParameter modelParameter, ModelParameter modelParameter0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setMigration_rate(ModelParameter migration_rate) {
        this.migration_rate = migration_rate;
    }

    public ModelParameter getMigration_rate() {
        // If the population is active
        if (population.isActive()) {
            return migration_rate;
        }
        // No migrants backward are accepted
        return null;
    }

    /**
     * Get the population associated to this migration rate (backward)
     *
     * @return
     */
    public Population getPopulation() {
        return population;
    }

    /**
     * Compare by migration rate
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Population_MigrationRate o) {
        double v1 = migration_rate.getFg().getGeneratedNumber().doubleValue();
        double v2 = o.migration_rate.getFg().getGeneratedNumber().doubleValue();
        return Double.compare(v1, v2);
    }

    /**
     * Equal if the Object o is Population_MigrateRate and has the same
     * Population
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Population_MigrationRate) {
            return this.getPopulation().equals(((Population_MigrationRate) o).getPopulation());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getPopulation().hashCode();
    }
}
