/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util.database.cleaning;

/**
 *
 * @author Oscar
 */
public class MultivariateHypergeometricDistribution {

    /**
     * n is the number of observations in column 1. Nt is the total number of observations.
     * @param logFactorialSequence
     */
    public MultivariateHypergeometricDistribution(LogFactorialSerie logFactorialSequence) {
        this.logFactorialSequence = logFactorialSequence;
    }
    private final LogFactorialSerie logFactorialSequence;

    /**
     * Get the logpdf of a table with n rows and 2 columns. The probability that is computed is
     * how likely are the values distributed for each cell when considering the first column considering the sum of rows. Use the vector logFactorialSequence where for each
     * possible factorial the value is specified. In this way the algorithm is faster if logFactorialSequence is reused
     * @param column1
     * @param rMarginals
     * @param yT (the total number of observations of column1)
     * @param Nt (the total number of observations of column1 +column2)
     * @return
     */
    public double logpdf(short[] column1, short[] rMarginals, short yT, short Nt) {
        // Marginals from rows
        double logP = 0;
        // Marginal of the first column
        int n = 0;
        int r = 0;
// Only look the cells if we have not reached the total number of yT. After this, there is no sense to go further
        while (n < yT) {
            if (column1[r] > 0) {
                logP += logFactorialSequence.getLogFactorial(rMarginals[r]) - (logFactorialSequence.getLogFactorial(column1[r]) + logFactorialSequence.getLogFactorial(rMarginals[r] - column1[r]));
                n += column1[r];
            }
            r++;
        }
        logP -= logFactorialSequence.getLogFactorial(Nt) - (logFactorialSequence.getLogFactorial(yT) + logFactorialSequence.getLogFactorial(Nt - yT));
        return logP;
    }

    /**
    /**
     * Get the logpdf of a table with n rows and 2 columns. The probability that is computed is
     * how likely are the values distributed for each cell when considering the first column considering the sum of rows. Use the vector logFactorialSequence where for each
     * possible factorial the value is specified. In this way the algorithm is faster if logFactorialSequence is reused
     * @param column1
     * @param rMarginals
     * @return
     */
    public double logpdfv0(short[] column1, short[] rMarginals) {
        // Marginals from rows
        double logP = 0;
        // Marginal of the first column
        int Nt = 0;
        int n = 0;
        for (int r = 0; r < rMarginals.length; r++) {
            logP += logFactorialSequence.getLogFactorial(rMarginals[r]) - (logFactorialSequence.getLogFactorial(column1[r]) + logFactorialSequence.getLogFactorial(rMarginals[r] - column1[r]));
            Nt += rMarginals[r];
            n += column1[r];
        }
        logP -= logFactorialSequence.getLogFactorial(Nt) - (logFactorialSequence.getLogFactorial(n) + logFactorialSequence.getLogFactorial(Nt - n));
        return logP;
    }
//    public static void main(String[] args) {
//        LogFactorialSerie logFactorial = new LogFactorialSerie(901);
//        MultivariateHypergeometricDistribution mult = new MultivariateHypergeometricDistribution(logFactorial);
//        short[] columnB = new short[53];
//        short[] rMarginalsB = new short[53];
//        for (int p = 0; p < 53; p++) {
//            rMarginalsB[p] = 17;
//        }
//        for (short i = 1; i <= 34; i++) {
//            if(i<=17)
//            {
//             columnB[0] = i;
//            }
//            else
//            {
//                columnB[1] = (short)(i-17);
//            }
//            System.out.println(i + " " + mult.logpdf(columnB, rMarginalsB));
//        }
//
//        for(int i=0;i<53/2;i++)
//        {
//            columnB[i] = 17;
//        }
//
//        System.out.println(17*53/2 + " "  + mult.logpdf(columnB,rMarginalsB));
//    }
}
