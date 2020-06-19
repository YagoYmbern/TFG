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
public class FixedValue extends Function {

    public FixedValue(Number number) {
        this.number = number;
    }

    @Override
    public void generate_sample() {
    }

    @Override
    public String getFunctionRepresentation() {
        return number.toString();
    }

    @Override
    public String toString() {
        return getFunctionRepresentation();
    }
}
