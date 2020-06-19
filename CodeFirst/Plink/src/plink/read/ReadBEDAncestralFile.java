/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.read;

import java.io.FileNotFoundException;
import java.io.IOException;
import plink.PlinkFileException;
import plink.util.SNPGenotypeByteArray;
import plink.util.SNPPlink;

/**
 *
 * @author olao
 */
public class ReadBEDAncestralFile extends ReadBEDFile{
    
    public ReadBEDAncestralFile(String fileName) throws FileNotFoundException, IOException, PlinkFileException {
        super(fileName);
    } 
    
    @Override
    public SNPGenotypeByteArray nextSNPGenotype() throws Exception {
        // SNP
        SNPPlink snp = nextSNP();
        if (snp != null) {
            byte[] g = new byte[individuals.length];
            int iterations = individuals.length / 4;
            int indiv = 0;
            for (int it = 0; it < iterations; it++) {
                byte[] nextFourBytes = getNextFourGenotypes();
                for (int i = 0; i < 4; i++) {
                    g[indiv] = nextFourBytes[i];
                    indiv++;
                }
            }
            if (indiv != individuals.length) {
                byte[] nextFourBytes = getNextFourGenotypes();
                int i = 0;
                while (indiv < individuals.length) {
                    g[indiv] = nextFourBytes[i];
                    if(g[indiv]==1)
                    {
                        g[indiv]=3;
                    }
                    i++;
                    indiv++;
                }
            }

            return new SNPGenotypeByteArray(snp, individuals, g);
        }
        return null;
    }

    @Override
    public SNPGenotypeByteArray nextSNPGenotypeWithoutIndividuals() throws Exception {
        // SNP
        SNPPlink snp = nextSNP();
        if (snp != null) {
            byte[] g = new byte[individuals.length];
            int iterations = individuals.length / 4;
            int indiv = 0;
            for (int it = 0; it < iterations; it++) {
                byte[] nextFourBytes = getNextFourGenotypes();
                for (int i = 0; i < 4; i++) {
                    g[indiv] = nextFourBytes[i];
 // Ancient data can only be homozygote!
                    if(g[indiv]==1)
                    {
                        g[indiv]= 3;
                    }                    
                    indiv++;
                }
            }
            if (indiv != individuals.length) {
                byte[] nextFourBytes = getNextFourGenotypes();
                int i = 0;
                while (indiv < individuals.length) {
                    g[indiv] = nextFourBytes[i];
                    if(g[indiv]==1)
                    {
                        g[indiv]=3;
                    }                    
                    i++;
                    indiv++;
                }
            }
            return new SNPGenotypeByteArray(snp, g);
        }
        return null;
    }    
}
