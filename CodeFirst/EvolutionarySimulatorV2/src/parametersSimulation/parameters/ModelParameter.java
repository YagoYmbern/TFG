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
public class ModelParameter {
    public ModelParameter(String name_of_parameter, Function fg)            
    {
        this.name_of_parameter = name_of_parameter;
        this.fg = fg;
    }

    /**
     * No name
     * @param fg 
     */
    public ModelParameter(Function fg)            
    {
        this.fg = fg;
        this.name_of_parameter = null;
    }
    
    
    private final String name_of_parameter;
    private final Function fg;

    /**
     * Function generating the value of this parameter
     * @return 
     */
    public Function getFg() {
        return fg;
    }
    
    /**
     * Name of the parameter
     * @return 
     */
    public String getName_of_parameter() {
        return name_of_parameter;
    }
    
    @Override
    public String toString()
    {
        return getName_of_parameter() + " " + getFg().toString();
    }
    
}
