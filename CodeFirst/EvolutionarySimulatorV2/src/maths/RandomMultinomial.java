/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maths;

import java.util.Arrays;
import java.util.Random;

/**
 * RandomMultinomial uses classical shorting from smaller to largest and then
 * computing cummulative pdf.
 *
 * @author Administrator
 */
public class RandomMultinomial {

    public RandomMultinomial(double[] p) {
        vals = new ValueValue[p.length];
        for (int v = 0; v < vals.length; v++) {
            vals[v] = new ValueValue(p[v], v);
        }

        Arrays.sort(vals);

        cdf = new double[vals.length];
        cdf[0] = vals[0].getValueOne();
        for (int e = 1; e < vals.length; e++) {
            cdf[e] = cdf[e - 1] + vals[e].getValueOne();
        }
    }

    public RandomMultinomial(Double[] pe) {
        vals = new ValueValue[pe.length];
        for (int v = 0; v < vals.length; v++) {
            vals[v] = new ValueValue(pe[v], v);
        }

        Arrays.sort(vals);

        cdf = new double[vals.length];
        cdf[0] = vals[0].getValueOne();
        for (int e = 1; e < vals.length; e++) {
            cdf[e] = cdf[e - 1] + vals[e].getValueOne();
        }
    }

    ValueValue[] vals;
    double[] cdf;    
    
    /**
     * Ids refers to each of the k levels in p
     *
     * @param size
     * @return
     */
    public int [] sample(int size) {
        Random random = new Random();
        int[] ids = new int[size];

        for (int i = 0; i < ids.length; i++) {
            double rV = random.nextDouble();
            int e = 0;
            try {
                while (rV > cdf[e]) {
                    e++;
                }
            } catch (ArrayIndexOutOfBoundsException s) {
                e--;
            }
            ids[i] = (int) vals[e].getValueTwo();
        }

        return ids;
    }

    public static void main(String[] args) {
        double[] p = {0.4, 0.2, 0.3, 0.1};
        RandomMultinomial rMultinom = new RandomMultinomial(p);
        int [] ids = rMultinom.sample(1000);
        System.out.println(java.util.Arrays.toString(ids));
    }

}
