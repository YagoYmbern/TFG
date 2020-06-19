/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maths;

import java.util.Random;
import parametersSimulation.parameters.ModelParameter;

/**
 *
 * @author olao
 */
public class RandomUniform extends Function{
    public RandomUniform(ModelParameter min, ModelParameter max)
    {
        this.min = min;
        this.max = max;
        generate_sample();        
    }
    
    private Random r = new Random();
    private final ModelParameter min, max;
    
    @Override
    public void generate_sample()
    {
        double maxi = max.getFg().getGeneratedNumber().doubleValue();
        double mini = min.getFg().getGeneratedNumber().doubleValue();
        number =  r.nextDouble()*(maxi - mini) + mini;
    }

    @Override
    public String getFunctionRepresentation() {
        return "U("+min.getFg().getGeneratedNumber().doubleValue()+","+max.getFg().getGeneratedNumber().doubleValue()+")";
    }
    
    @Override
    public String toString()
    {
        return this.getGeneratedNumber().toString();
    }
}
