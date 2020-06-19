/*
 * ReadTable.java
 *
 * Created on 20 juni 2006, 9:01
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
import java.util.ArrayList;

/**
 * ReadTable is a class to read a file which is in a matrix format. Columns are
 * separated by a separator, which can be used in a Regular expression. TAB =
 * "\\t", SPACE = " ", COMMA = "," and so on
 *
 * @author 503760
 */
public class ReadTable {

    /**
     * Creates a ReadTable object using the path stored in the tableFile String
     * and the separator. If it cannot read the file, throw a IOException
     *
     * @param tableFile
     * @param separator
     * @throws IOException
     */
    public ReadTable(String tableFile, String separator) throws IOException {
        this.separator = separator;
// Create the BufferedReaderObject
        createBufferedReaderObject(new File(tableFile));
// Read the table
        readTable();
    }

    /**
     * Creates a ReadTable object using the path stored in the tableFile File
     * and the separator. If it cannot read the file, throw a IOException
     *
     * @param tableFile
     * @param separator
     * @throws IOException
     */
    public ReadTable(File tableFile, String separator) throws IOException {
        this.separator = separator;
        createBufferedReaderObject(tableFile);
        readTable();

    }

    // BufferedReader to be used to read the file
    private BufferedReader theFile;
    // Store the matrix with an initial capacity of 500
    private ArrayList<String[]> matrix = new ArrayList<>(500);
    // Store the separator
    private String separator = null;

    /**
     * Create the BufferedReaderObject from the tableFile
     *
     * @param tableFile
     */
    private void createBufferedReaderObject(File tableFile) throws IOException {
        theFile = new BufferedReader(new InputStreamReader(new FileInputStream(tableFile)));
    }

    /**
     * Get the separator associated to this matrix
     *
     * @return the separator
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Read one row
     *
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

    /**
     * Get the columns of a particular row. It is assumed that index < total
     * length of the matrix @param index
     * @
     *
     * r
     * eturn the columns of the row
     */
    public String[] getColumnsOfRow(int index) {
        return matrix.get(index);
    }

    /**
     * Get the rows of a column. It is assumed that index is smaller than the
     * total number of columns. Otherwise it will throw a ArrayIndexOutOfBounds
     * exception
     *
     * @param index
     * @return the rows of a column or an ArrayIndexOutOfBounds Exception
     */
    public String[] getRowsOfColumn(int index) {
        String[] column = new String[getNumberOfRows()];
// Iterate through each row
        for (int i = 0; i < getNumberOfRows(); i++) {
            column[i] = matrix.get(i)[index];
        }
// Return the array
        return column;
    }

// Read the table    
    private void readTable() throws IOException {
// First we put all the data in a vector with initial capacity of 500 rows.
        String line = readRow();
// Iterate while there is a row to be read
        while (line != null) {
            String[] lineSplit = line.split(separator);
            matrix.add(lineSplit);
            line = readRow();
        }
// Close the buffer
        theFile.close();
    }

    /**
     * Get the String value of the cell in the row row and column column. Row
     * and column MUST be within the range of the matrix values!
     *
     * @param row
     * @param column
     * @return the cell
     */
    public String getCell(int row, int column) {
        return matrix.get(row)[column];
    }

    /**
     * Get the number of columns of this matrix
     *
     * @return the number of columns
     */
    public int getNumberOfColumns() {
        return matrix.get(0).length;
    }

    /**
     * Get the number of rows of this matrix
     *
     * @return the number of rows
     */
    public int getNumberOfRows() {
        return matrix.size();
    }

    /**
     * Get a double matrix representation of this String matrix. BEWARE that all
     * the cells of the matrix are double format BEFORE applying this method!
     *
     * @return a double matrix or a NumberFormatException if any of the cells is
     * not a double
     */
    public double[][] getMatrixDouble() {
        double[][] doubleMatrix = new double[matrix.size()][matrix.get(0).length];
        for (int r = 0; r < doubleMatrix.length; r++) {
            for (int c = 0; c < doubleMatrix[0].length; c++) {
// Parse a String to Double. It only works if the String is has a DOUBLE REPRSENTATION
                doubleMatrix[r][c] = Double.parseDouble(matrix.get(r)[c]);
            }
        }
        return doubleMatrix;
    }

    /**
     * Get a double matrix representation of this String matrix. BEWARE that all
     * the cells of the matrix are int format BEFORE applying this method!
     *
     * @return a int matrix or a NumberFormatException if any of the cells is
     * not a double
     */
    public int[][] getMatrixInt() {
        int[][] intMatrix = new int[matrix.size()][matrix.get(0).length];
        for (int r = 0; r < intMatrix.length; r++) {
            for (int c = 0; c < intMatrix[0].length; c++) {
// Parse a String to Double. It only works if the String is has a INT REPRSENTATION
                intMatrix[r][c] = Integer.parseInt(matrix.get(r)[c]);
            }
        }
        return intMatrix;
    }

    //Exemple of use
    public static void main(String[] args) throws IOException {
// Format of t.txt
// a	b	c
// d	e	f
// g	h	i

        ReadTable r = new ReadTable("C:\\t.txt", "\t");
        System.out.println(r.getCell(0, 0));

    }
}
