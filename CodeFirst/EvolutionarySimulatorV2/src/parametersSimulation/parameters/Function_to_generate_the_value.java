///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package parametersSimulation.parameters;
//
//import maths.Function;
///**
// *
// * @author olao
// */
//public class Function_to_generate_the_value {
//    public Function_to_generate_the_value(Function function)
//    {
//        this.function = function;
//    }    
//    
//    protected Number number = null;
//    protected Function function;
//
//    /**
//     * Get the function associated to the number generator
//     * @return 
//     */
//    public Function getFunction() {
//        return function;
//    }    
//    
//    /**
//     * Generate a new value. Destroy the previous number
//     */
//    public void generate_new_value()
//    {
//        number = function.sample();
//    }
//    
//    /**
//     * Ignore the function. Set the a number of interest
//     * @param number 
//     */
//    public void setNumber(Number number)
//    {
//        this.number = number;
//    }
//    
//    /**
//     * Get the generated number
//     * @return 
//     */
//    public Number getGeneratedNumber()
//    {
//        return number;
//    }
//            
//}
