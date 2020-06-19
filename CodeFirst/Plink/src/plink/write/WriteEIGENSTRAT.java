/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.write;

import java.io.IOException;
import plink.util.SNPGenotype;
import plink.util.demography.Individual;
import write.WriteFile;

/**
 *
 * @author Oscar Lao
 */
public class WriteEIGENSTRAT {
    public WriteEIGENSTRAT() throws Exception {
    }

    private WriteFile bed;
    private WriteFile fam;
    private WriteFile bim;
    private Individual [] inds;

    /**
     * Open the connection for the bed, fam and bim files
     *
     * @param fileName
     * @throws Exception
     */
    public void open(String fileName) throws Exception {
        bed = new WriteFile(fileName + ".eigenstratgeno");
        fam = new WriteFile(fileName + ".ind");
        bim = new WriteFile(fileName + ".map");
    }

    /**
     * Add the individuals to the fam file
     *
     * @param individuals
     */
    public void addIndividuals(Individual[] individuals) {
        for (Individual i : individuals) {
            fam.println(i.getName()+ "\t" + ((i.getSex()==0)?"M":"F") + "\t" + i.getFamilyName());            
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
        bim.println(snp.getSnp().getChromosome() + "\t" + snp.getSnp().getRsId() + "\t0\t" + snp.getSnp().getPosition());
// Now add the binary file!
        // Read individuals in sets of four. Then convert each 
        int i = 0;
        byte[] g = snp.getGenotypes();
        if(inds.length!=g.length)
        {
            System.out.println("Not same size");
            System.exit(0);
        }
        for(byte gg:g)
        {
            bed.print((gg<3)?gg:9);
        }
        bed.println("");
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
}
