/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package maths;

import java.util.Random;

/**
 *
 * @author oscar
 */
public class RandomBinomial {
    public RandomBinomial(int n, double p)
    {
        this.n = n;
        this.p = p;
        
    }

    private int n;
    private double p;


    /**
     * Get a sample from this Binomial distribution
     * @return
     */
    public int getSample()
    {
        Random r = new Random();
        int y = 0;
        for(int e=0;e<n;e++)
        {
            y += (r.nextDouble()<p)?1:0;
        }
        return y;
        
    }
}
