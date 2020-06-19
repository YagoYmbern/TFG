/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package write;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author olao
 */
public class WriteGZFile implements OutputFile{

    public WriteGZFile(String theFile) throws FileNotFoundException, IOException {
        this.theFile = theFile;
        k = new GZIPOutputStream(new FileOutputStream(this.theFile));    
    }

    public WriteGZFile(java.io.File fileName) throws FileNotFoundException, IOException {
        theFile = fileName.getName();
        k = new GZIPOutputStream(new FileOutputStream(fileName));
    }
    private final String theFile;
    GZIPOutputStream k;

    /**
     * Get the file name
     * @return 
     */
    public String getFileName() {
        return theFile;
    }

    /**
     * Print a new line
     * @param object
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    @Override
    public void println(Object object) throws UnsupportedEncodingException, IOException{
        String str = object.toString()+"\n";
        InputStream is = new ByteArrayInputStream(str.getBytes());
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            k.write(buffer, 0, len);
        }        
    }
    
    /**
     * Close the gzip file
     * @throws IOException 
     */
    @Override
    public void close() throws IOException{
        k.close();
    }        
}
