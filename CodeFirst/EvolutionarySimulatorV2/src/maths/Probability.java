/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maths;

import exception.ExceptionProbability;

/**
 *
 * @author Oscar Lao
 */
public class Probability {
    public Probability(double p) throws ExceptionProbability
    {
        if(p>=0 && p<=1)
        this.p = p;
        else
        throw new ExceptionProbability(p + " is not in the range [0,1]");
            
        
    }
    
    private final double p;

    public double getP() {
        return p;
    }
    
    
}
