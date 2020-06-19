/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package exception;

/**
 *
 * @author oscar
 */
public class ExceptionDemography extends Exception{

    public static final String NOT_MULTIPLE_TWO = "The size is not multiple of TWO";
    public static final String LARGER_THAN_TWO = "The chromosome ascertained is greather than 1";
    public static final String NO_COALESCENCE = "There was no final coalescence of the sample";
    public static final String GREATER_COUPLE = "The number of couples is greater than the effective population size";
    
    public ExceptionDemography(String cause)
    {
        super(cause);
    }    
}
