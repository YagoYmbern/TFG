/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maths;


/**
 *
 * @author Oscar
 */
public class RandomUniformWithoutResampling {

    public RandomUniformWithoutResampling(int start, int finish) {
        this.start = start;
        N = finish - start;
    }
    private int start, N;

    public int sample() {
        return 0;
    }

    /**
     * Get a sample from a uniform distribution without replacement where n is smaller than the total number of points
     * @param n
     * @return
     */

    public int [] sample(int n) {
        if(n>N)
        {
            throw new ArrayIndexOutOfBoundsException("The number of samples is larger than the total number of points!: " + n + " " + N);
        }
        int[] s = new int[n];
        int m = 0;
        int t = 0;
        while (m < n) {
            double u = Math.random();
            if (((double) (N - t)) * u < (n - m)) {
                s[m] = start + t;
                m++;
            }
            t++;
        }
        return s;
    }

    /**
     * Get a boolean array indicating whether each of the elements between start and finish have been ascertained (true) or not (false)
     * the total number of true elements will be n
     * @param n
     * @return
     */
    public boolean [] sampleArray(int n)
    {
        if(n>N)
        {
            throw new ArrayIndexOutOfBoundsException("The number of samples is larger than the total number of points!: " + n + " " + N);
        }
// boolean array
        boolean [] s = new boolean[N];
// Set all the elements as false
        for(int e=0;e<s.length;e++)
        {
            s[e] = false;
        }
        int m = 0;
        int t = 0;
        while (m < n) {
            double u = Math.random();
            if (((double) (N - t)) * u < (n - m)) {
                s[t] = true;
                m++;
            }
            t++;
        }
        return s;
    }
}
