/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author olao
 */
public class ReadSetOfFiles extends ReaderFile {

    /**
     * Create a new instance of ReadSetOfFilters. Read all the files that are in
     * the folder.
     *
     * @param folder
     * @throws IOException
     */
    public ReadSetOfFiles(String folder) throws IOException {
        super(folder);
        this.folder = new File(folder);
        this.file = this.folder.listFiles();
        theFile = new BufferedReader(new InputStreamReader(new FileInputStream(file[file_counter])));

    }

    public ReadSetOfFiles(java.io.File folder) throws IOException {
        super(folder.getName());
        this.folder = folder;
        this.file = folder.listFiles();
        theFile = new BufferedReader(new InputStreamReader(new FileInputStream(file[file_counter])));
    }
    
    private File folder;
    private BufferedReader theFile;
    private File[] file;
    private int file_counter = 0;

    @Override
    public String readRow() {
        String row = "";
        try {
            row = theFile.readLine();
            // Reached the end of the file
            if (row == null) {
                try {
                    theFile.close();
                } catch (java.io.IOException e) {
                    System.out.println(e.getMessage());
                }
                // go to the next file. If that was the last, then return the null
                file_counter++;
                if (file_counter < file.length) {
                    theFile = new BufferedReader(new InputStreamReader(new FileInputStream(file[file_counter])));
                    row = theFile.readLine();
                }
            }
        } catch (java.io.IOException e) {
            System.out.println("I cannot read the line");
            System.exit(0);
        }
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
       folder.delete();
    }
}
