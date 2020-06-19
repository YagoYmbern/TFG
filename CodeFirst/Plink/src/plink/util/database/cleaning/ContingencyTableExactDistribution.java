/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plink.util.database.cleaning;

/**
 *
 * @author Administrator
 */
public class ContingencyTableExactDistribution {
    /**
     * Computes the logPdf of a table using the formula described in "Note on an exact treatment of contingency, goodness of fit and other problems of significance"
     * by Freeman and Halton
     * @param table
     * @param logFactorial
     * @return
     */
    public static double logpdf(int [][] table, LogFactorialSerie logFactorial)
    {
        double logP = 0;
        int [] marginalsRow = new int[table.length];
        int [] marginalsColumn = new int[table[0].length];
        int n= 0;
        for(int r=0;r<table.length;r++)
        {
            for(int c=0;c<table[r].length;c++)
            {
                marginalsRow[r]+=table[r][c];
                marginalsColumn[c]+=table[r][c];
                n+=table[r][c];
                logP-=logFactorial.getLogFactorial(table[r][c]);
            }
        }

        logP-=logFactorial.getLogFactorial(n);

// Add the factorials of the rows and columns

        for(int r=0;r<table.length;r++)
        {
            logP+=logFactorial.getLogFactorial(marginalsRow[r]);
        }


        for(int c=0;c<table[0].length;c++)
        {
            logP+=logFactorial.getLogFactorial(marginalsColumn[c]);
        }

        return logP;
    }

    public static void main(String [] args)
    {
        int [][] matrix = {{15,6},{7,322}};
        System.out.println(java.util.Arrays.toString(matrix[0]));
        System.out.println(java.util.Arrays.toString(matrix[1]));
        LogFactorialSerie lSerie = new LogFactorialSerie(351);
        System.out.println(Math.exp(logpdf(matrix, lSerie)));
    }

}
