/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plink.util.database.cleaning;

/**
 *
 * @author Oscar
 */
public class LogFactorialSerie {

    /**
     * Construct the logFactorial values from 0 to n (included)
     * @param n
     */
    public LogFactorialSerie(int n)
    {
        this.n = n;
        doLogFactorialRecursivity();
    }

    private int n;
    private double [] logFactorial;

    /**
     * Get the logFactorial sequence of a number n (it is also included)
     * @param n
     * @return
     */
    private void doLogFactorialRecursivity()
    {
        logFactorial = new double[n+1];
        logFactorial[0]=0;
        for(int i=1;i<=n;i++)
        {
            logFactorial[i]=logFactorial[i-1]+Math.log(i);
        }
    }

    /**
     * Get the logFactorial value of y. y must be <=n
     * @param y
     * @return
     */
    public double getLogFactorial(int y)
    {
        return logFactorial[y];
    }
}
