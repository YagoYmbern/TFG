/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demography;

import maths.FixedValue;
import parametersSimulation.parameters.ModelParameter;

/**
 *
 * @author Oscar Lao
 */
public class ParametersDemography {
    
    public ParametersDemography(ModelParameter effectivePopulationSizeOfOneParent, ModelParameter growthRate)
    {
        setEffectivePopulationSizeOfOneParent(effectivePopulationSizeOfOneParent);
        setGrowthRate(growthRate);
    }            

    public ParametersDemography(ModelParameter effectivePopulationSizeOfOneParent)
    {
        setEffectivePopulationSizeOfOneParent(effectivePopulationSizeOfOneParent);
    }  
    
    private ModelParameter effectivePopulationSizeOfOneParent = null;
    private ModelParameter growthRate = new ModelParameter(new FixedValue(0.0));
    private boolean growthIsEmpty = true;

    /**
     * Set the effective population size in number of individuals of one parent. In number of chromosomes, it means 4*effectivePopulationSizeOfOneParent
     * @param effectivePopulationSizeOfOneParent 
     */
    public void setEffectivePopulationSizeOfOneParent(ModelParameter effectivePopulationSizeOfOneParent) {
        this.effectivePopulationSizeOfOneParent = effectivePopulationSizeOfOneParent;
    }

    public ModelParameter getEffectivePopulationSizeOfOneParent() {
        return effectivePopulationSizeOfOneParent;
    }

    /**
     * Set the growth rate. Negative values mean exponential growth
     * @param growthRate 
     */
    public void setGrowthRate(ModelParameter growthRate) {
        this.growthRate = growthRate;
        growthIsEmpty = false;
    }

    /**
     * Growth is empty
     * @return 
     */
    public boolean growthIsEmpty()
    {
        return growthIsEmpty;
    }
    
    /**
     * Get the growth rate
     * @return 
     */
    public ModelParameter getGrowthRate() {
        return growthRate;
    }
}
