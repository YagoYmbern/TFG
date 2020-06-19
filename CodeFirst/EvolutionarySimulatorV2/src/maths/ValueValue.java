/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maths;

/**
 *
 * @author Administrator
 */
public class ValueValue implements Comparable<ValueValue>{
    public ValueValue(double v1, double v2)
    {
        setValueOne(v1);
        setValueTwo(v2);
    }

    private double v1;
    private double v2;

    /**
     * Set the rank of this object
     * @param rank
     */
    public final void setValueOne(double v1)
    {
        this.v1 = v1;
    }

    /**
     * Set the value of this object
     * @param value
     */
    public final void setValueTwo(double v2)
    {
        this.v2 = v2;
    }

    /**
     * Get the rank
     * @return
     */
    public double getValueOne()
    {
        return v1;
    }

    /**
     * Get the value
     * @return
     */
    public double getValueTwo()
    {
        return v2;
    }

    /**
     * Compare this rankValue to another rank value object by the magnitude of the value One
     * @param o
     * @return
     */
    public int compareTo(ValueValue o) {
        return new Double(this.getValueTwo()).compareTo(o.getValueTwo());
    }
}
