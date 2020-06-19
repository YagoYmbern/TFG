/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plink;

/**
 *
 * @author Administrator
 */
public class PlinkFileException extends Exception{
    public static final String INCORRECT_HEADER = "The header of the bed does not correspond to the expected";
    public static final String INCORRECT_MODE = "The mode is not 1";

    public PlinkFileException(String cause)
    {
        super(cause);
    }
}
