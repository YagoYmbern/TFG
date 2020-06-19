/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package write;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author olao
 */
public interface OutputFile {
    
public void println(Object object) throws UnsupportedEncodingException, IOException;

public void close() throws IOException;
    
}
