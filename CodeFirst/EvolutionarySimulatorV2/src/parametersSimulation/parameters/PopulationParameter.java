/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parametersSimulation.parameters;

import maths.Function;

/**
 *
 * @author olao
 */
public class PopulationParameter extends ModelParameter{
    public PopulationParameter(String name_of_parameter, Function fg, int sample_size, int time_of_sample)
    {
        super(name_of_parameter, fg);
        this.sample_size = sample_size;
        this.time_of_sample = time_of_sample;
    }
    
    private final int sample_size, time_of_sample;      

    public int getSample_size() {
        return sample_size;
    }

    public int getTime_of_sample() {
        return time_of_sample;
    }        
}
