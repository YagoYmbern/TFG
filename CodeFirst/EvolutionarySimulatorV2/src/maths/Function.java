/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maths;

/**
 *
 * @author olao
 */
public abstract class Function {

    public Function() {
    }

    protected Number number;

    /**
     * Ignore the function. Set the a number of interest
     *
     * @param number
     */
    public void setNumber(Number number) {
        this.number = number;
    }

    /**
     * Get the generated number
     *
     * @return
     */
    public Number getGeneratedNumber() {
        return number;
    }
    
    /**
     * Add a value to the generated number by this function
     * @param f
     * @return 
     */
    public FixedValue add_Integer(Function f)
    {
        return new FixedValue(this.getGeneratedNumber().intValue() + f.getGeneratedNumber().intValue());
    }

    /**
     * Add a value to the generated number by this function
     * @param f
     * @return 
     */
    public FixedValue minus_Double(Function f)
    {
        return new FixedValue(this.getGeneratedNumber().doubleValue() - f.getGeneratedNumber().doubleValue());
    }    
    
    public abstract String getFunctionRepresentation();

    protected abstract void generate_sample();
}
