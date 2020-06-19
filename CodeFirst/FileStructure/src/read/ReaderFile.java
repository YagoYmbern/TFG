/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package read;

import java.io.IOException;

/**
 *
 * @author Oscar Lao
 */
public abstract class ReaderFile {
    
    public ReaderFile(String file) throws IOException
    {
        this.file = file;
    }      
    
    private String file;

    /**
     * Get the name of the fucking file
     * @return 
     */
    public String getName() {
        return file;
    }           
    
    public abstract String readRow() throws IOException;
    
    public abstract void close() throws IOException;
}
