/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package read;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.GZIPInputStream;

public class ReadGZFile extends ReaderFile{
    /**
     * Open a gz file.
     * @param gzFile
     * @throws IOException 
     */
    public ReadGZFile(String gzFile) throws IOException {
        super(gzFile);
        in = new GZIPInputStream(new FileInputStream(gzFile));
        decoder = new InputStreamReader(in);
        br = new BufferedReader(decoder);
    }
    GZIPInputStream in;
    private Reader decoder;
    BufferedReader br;

    @Override
    public String readRow() throws IOException{
        String row  = br.readLine();
        return row;     
    }

    @Override
    public void close() throws IOException
    {
        in.close();
        decoder.close();
        br.close();        
    }
}
