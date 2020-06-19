/*
 * To change this template, choose Tools | Templates
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
 * @author oscar
 */
public class ReadDoubleTable {

    /**
     * Creates a ReadTable object using the path stored in the tableFile String and the separator. If it cannot read the file, throw a IOException
     * @param tableFile
     * @param separator
     * @throws IOException
     */

    public ReadDoubleTable(String tableFile, String separator, int r, int c) throws IOException {
        this.separator = separator;
// Create the BufferedReaderObject
        createBufferedReaderObject(new File(tableFile));
        matrix = new double[r][c];
// Read the table
        readTable();
    }

    /**
     * Creates a ReadTable object using the path stored in the tableFile File and the separator. If it cannot read the file, throw a IOException
     * @param tableFile
     * @param separator
     * @throws IOException
     */

    public ReadDoubleTable(File tableFile, String separator, int r, int c) throws IOException {
        this.separator = separator;
        createBufferedReaderObject(tableFile);
        matrix = new double[r][c];        
        readTable();

    }

    // BufferedReader to be used to read the file
    private BufferedReader theFile;
    // Store the matrix with an initial capacity of 500
    private double [][] matrix = null;
    // Store the separator
    private String separator = null;

    /**
     * Create the BufferedReaderObject from the tableFile
     * @param tableFile
     */

    private void createBufferedReaderObject(File tableFile)
    {
        try {
            theFile = new BufferedReader(new InputStreamReader(new FileInputStream(tableFile)));
        } catch (java.io.IOException e) {
            System.out.println("File " + tableFile.getAbsolutePath() + " not found");
            System.exit(0);
        }
    }

    /**
     * Get the separator associated to this matrix
     * @return the separator
     */
    
    public String getSeparator()
    {
        return separator;
    }

    /**
     * Read one row
     * @return one String row of the file
     */
    private String readRow() throws IOException {
        // Create a String object
        String row = null;
        // Read the row
        row = theFile.readLine();
// Return the row
        return row;
    }


// Read the table    
    private void readTable() throws IOException {
// First we put all the data in a vector with initial capacity of 500 rows.
        String line = readRow();
        for(int row=0;row<matrix.length;row++) {
            String[] lineSplit = line.split(separator);
            for(int col=0;col<lineSplit.length;col++)
            {
               matrix[row][col] = Double.parseDouble(lineSplit[col]); 
            }
            line = readRow();
        }
// Close the buffer
        theFile.close();
    }

    /**
     * Get the String value of the cell in the row row and column column. Row and column MUST be within the range of the matrix values!
     * @param row
     * @param column
     * @return the cell
     */
    public double getCell(int row, int column) {
        return matrix[row][column];
    }

    /**
     * Get the number of columns of this matrix
     * @return the number of columns
     */
    public int getNumberOfColumns() {
        return matrix[0].length;
    }

    /**
     * Get the number of rows of this matrix
     * @return the number of rows
     */
    public int getNumberOfRows() {
        return matrix.length;
    }
}