/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util.database.cleaning;

/**
 *
 * @author Oscar
 */
public class Hypergeometric {

    /**
     * n is the number of observations in column 1. Nt is the total number of observations.
     * @param logFactorial
     */
    public Hypergeometric(LogFactorialSerie logFactorial) {
        this.logFactorial = logFactorial;
    }

    private final LogFactorialSerie logFactorial;

    /**
     * Get the logpdf of a table with n rows and 2 columns. The probability that is computed is
     * how likely are the values distributed for each cell when considering the first column considering the sum of rows
     * @param table
     * @return
     */
    public double logpdf(int[][] table) {
        int n = 0;
        int N = 0;
        // Marginals from rows
        double logP = 0;
        for (int[] table1 : table) {
            int rMarginals = table1[0] + table1[1];
            N += rMarginals;
            n += table1[0];
            logP += logFactorial.getLogFactorial(rMarginals) - logFactorial.getLogFactorial(table1[0]) - logFactorial.getLogFactorial(table1[1]);
        }
        logP+= -(logFactorial.getLogFactorial(N)-logFactorial.getLogFactorial(n)-logFactorial.getLogFactorial(N-n));
        return logP;
    }

    /**
     * Get the logpdf of a table with n rows and 2 columns. The probability that is computed is
     * how likely are the values distributed for each cell when considering the first column considering the sum of rows
     * @param c11
     * @param c12
     * @param c21
     * @param c22
     * @return
     */
    public double logpdf(int c11, int c12, int c21, int c22) {
        int n = c11+c21;
        int N = c11+c12+c21+c22;
        // Marginals from rows
        double logP = 0;
        logP+= logFactorial.getLogFactorial(c11+c12)-logFactorial.getLogFactorial(c11)-logFactorial.getLogFactorial(c12);
        logP+= logFactorial.getLogFactorial(c21+c22)-logFactorial.getLogFactorial(c21)-logFactorial.getLogFactorial(c22);
        logP+= -(logFactorial.getLogFactorial(N)-logFactorial.getLogFactorial(n)-logFactorial.getLogFactorial(N-n));
        return logP;
    }


    public static void main(String [] args)
    {
        LogFactorialSerie lserie = new LogFactorialSerie(351);

        Hypergeometric hyp = new Hypergeometric(lserie);

        System.out.println(Math.exp(hyp.logpdf(15,6,7,322)));
    }
}
