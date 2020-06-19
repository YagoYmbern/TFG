/*
 * WriteFile.java
 *
 * Created on May 12, 2006, 9:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package write;

import java.io.FileNotFoundException;

/**
 *
 * @author Oscar
 */
public class WriteFile implements OutputFile{
    
    /** Creates a new instance of WriteFile
     * @param theFile
     * @throws java.io.FileNotFoundException */
    public WriteFile(String theFile) throws FileNotFoundException
    {
        this.theFile = theFile;
        k =new java.io.PrintStream (new java.io.FileOutputStream ( new java.io.File (theFile)));
    }
    
    public WriteFile(java.io.File fileName) throws FileNotFoundException
    {
        k =new java.io.PrintStream (new java.io.FileOutputStream (fileName));
        theFile = fileName.getName();
    }
    private String theFile;
    private java.io.PrintStream k;

    public java.io.PrintStream getPrintStream()
    {
        return k;
    }
    
    public String getFileName()
    {
        return theFile;
    }
    
    @Override
    public void close()
    {
        k.close();
    }
    
    @Override
    public void println(Object object)
    {
        k.println(object);
    }

    public void print(Object object)
    {
        k.print(object);
    }
}
