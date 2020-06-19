/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.write;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import plink.read.ReadBEDFile;
import plink.util.SNPGenotype;
import plink.util.demography.Individual;
import write.WriteFile;

/**
 *
 * @author oscar_000
 */
public class WriteBED {

    public WriteBED() throws Exception {
    }

    private BufferedOutputStream bed;
    private WriteFile fam;
    private WriteFile bim;
    private Individual [] inds;

    private void addHeaderToBED() throws IOException {
        byte header1 = 108;
// Header1
        bed.write(header1);
        byte header2 = 27;
// Header2
        bed.write(header2);
// Mode        
        bed.write(1);        
    }

    /**
     * Open the connection for the bed, fam and bim files
     *
     * @param fileName
     * @throws Exception
     */
    public void open(String fileName) throws Exception {
        bed = new BufferedOutputStream(new FileOutputStream(fileName + ".bed"));
        addHeaderToBED();
        fam = new WriteFile(fileName + ".fam");
        bim = new WriteFile(fileName + ".bim");
    }

    /**
     * Add the individuals to the fam file
     *
     * @param individuals
     */
    public void addIndividuals(Individual[] individuals) {
        for (Individual i : individuals) {
            fam.println(i);
        }
        this.inds = individuals;
    }

    /**
     * Add a new SNP to the bed and bim file
     *
     * @param snp
     * @throws IOException
     */
    public void add(SNPGenotype snp) throws IOException {        
        bim.print(snp.getSnp());
        ArrayList<String> databases = snp.getSnp().getDatabases();
        try {
            for (String d : databases) {
                bim.print(" " + d);
            }
        } catch (NullPointerException e) {

        }

        bim.println("");
// Now add the binary file!
        // Read individuals in sets of four. Then convert each 
        int i = 0;
        byte[] g = snp.getGenotypes();
        if(inds.length!=g.length)
        {
            System.out.println("Not same size");
            System.exit(0);
        }
        while (i < g.length) {
            byte[] b = new byte[8];
            try {
                for (int w = 0; w < 4; w++) {
                    byte[] s = new byte[2];
                    switch (g[i]) {
                        case 1:
                            s[0] = 1;
                            break;
                        case 2:
                            s[0] = 1;
                            s[1] = 1;
                            break;
                        case 3:
                            s[1] = 1;
                            s[0] = 0;
                            break;
                        default:
                            break;
                    }
                    b[8 - 2 * w - 1] = s[1];
                    b[8 - 2 * w - 2] = s[0];
                    i++;
                }
            } catch (ArrayIndexOutOfBoundsException tok) {
            }
            int v = getInt(b);
            bed.write(v);
        }
    }

    private int getInt(byte[] b) {
        int i = 0;
        for (int e = 0; e < b.length; e++) {
            i += b[e] * Math.pow(2, b.length - 1 - e);
        }
        return i;
    }

    /**
     * Close all the connections
     *
     * @throws IOException
     */
    public void close() throws IOException {
        bed.close();
        bim.close();
        fam.close();
    }

    public static void main(String[] args) throws Exception {
//     1 1 0 0 1  0    G G    2 2    C C
//     1 2 0 0 1  0    A A    0 0    A C
//     1 3 1 2 1  2    0 0    1 2    A C
//     2 1 0 0 1  0    A A    2 2    0 0
//     2 2 0 0 1  2    A A    2 2    0 0
//     2 3 1 2 1  2    A A    2 2    A A 

//     1 snp1 0 1
//     1 snp2 0 2
//     1 snp3 0 3 
        ReadBEDFile rB = new ReadBEDFile("C:\\Users\\oscar_000\\Documents\\Australia_Project\\test");

        SNPGenotype snp = rB.nextSNPGenotype();

//        WriteBED wB = new WriteBED();
//        wB.open("C:\\Users\\oscar_000\\Documents\\Australia_Project\\test2");
//        
//        wB.addIndividuals(snp.getIndividuals());
        while (snp != null) {
            System.out.println(snp);
//            wB.add(snp);
            snp = rB.nextSNPGenotype();
        }

        rB.close();
//        wB.close();
    }
}
