/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maths;

/**
 *
 * @author Oscar
 */
public class RandomPoison {

    public RandomPoison(double l) {
        this.l = l;
    }

    private double l;

    public double sample() {
//        algorithm poisson random number (Knuth):
//    init:
        double L = Math.exp(-l);
        double k = 0;
        double p = 1;
        do {
            k++;
            p = p * Math.random();
        } while (p > L);
        return k-1;
    }
}
