/*
 * ReadFile.java
 *
 * Created on August 3, 2006, 11:20 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Oscar
 */
public class ReadFile extends ReaderFile {

    /**
     * Creates a new instance of ReadFile
     */
    public ReadFile(String file) throws IOException {
        super(file);
        this.file = new File(file);
        theFile = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file))));

    }

    public ReadFile(java.io.File file) throws IOException {
        super(file.getName());
        this.file = file;
        theFile = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    }

    private BufferedReader theFile;
    private File file;

    @Override
    public String readRow() throws IOException {
        String row = theFile.readLine();
        return row;
    }

    @Override
    public void close() throws IOException {
        try {
            theFile.close();
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete() {
        file.delete();
    }

    public File getFile() {
        return file;
    }
}
