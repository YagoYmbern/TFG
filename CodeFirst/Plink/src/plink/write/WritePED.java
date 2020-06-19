/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.write;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import plink.util.Allele;
import plink.util.IndividualBySNPMatrix;
import plink.util.IndividualGenotypeByte;
import plink.util.SNPByIndividualMatrix;
import plink.util.demography.Individual;
import write.WriteFile;
import plink.util.SNPPlink;
import plink.util.SNPGenotypeByteArray;

/**
 *
 * @author Administrator
 */
public class WritePED {

    public WritePED(String ped) throws FileNotFoundException {
        pedF = new WriteFile(ped + ".ped");
        mapF = new WriteFile(ped + ".map");
    }
    WriteFile pedF;
    WriteFile mapF;

    /**
     * Print a SNP by Individual matrix (SNPs are the rows, Individuals the columns)
     * @param snps 
     */
    public void printSNPByIndividualMatrix(SNPByIndividualMatrix snps) {
        for (SNPGenotypeByteArray snp:snps) {
            mapF.println(snp.getSnp().getChromosome() + " " + snp.getSnp().getRsId() + " 0 " +  snp.getSnp().getPosition());
        }
        mapF.close();
        Individual[] individuals = snps.getIndividual();
        System.out.println("Number of individuals: " + individuals.length);
        for (int indiv = 0; indiv < individuals.length; indiv++) {
            pedF.print(individuals[indiv].toString());
            for (int snp = 0; snp < snps.size(); snp++) {
                char a = Allele.getChar(snps.get(snp).getSnp().getA());
                char b = Allele.getChar(snps.get(snp).getSnp().getB());
                switch (snps.get(snp).getGenotypes()[indiv]) {
                    case 0:
                        pedF.print(" " + a + " " + a);
                        break;
                    case 1:
                        pedF.print(" " + a + " " + b);
                        break;
                    case 2:
                        pedF.print(" " + b + " " + b);
                        break;
                    default:
                        pedF.print(" 0 0");
                        break;
                }
            }
            pedF.println("");
        }
        pedF.close();
    }

    /**
     * Print in PED format an individual by SNP matrix (individuals are the rows, SNPs are the columns)
     * @param individuals 
     */
    public void printIndividualBySNPMatrix(IndividualBySNPMatrix individuals) {
        ArrayList<SNPPlink> snps = individuals.getSNPs();
        for (SNPPlink snp:snps) {
            mapF.println(snp.getChromosome() + " " + snp.getRsId() + " 0 " +  snp.getPosition());
        }
        mapF.close();
        System.out.println("Number of individuals: " + individuals.size());
        for (IndividualGenotypeByte individual:individuals) {
            pedF.print(individual.getI().toString());
            // Genotype byte of the individual
            byte [] g = individual.getGenotype();
            int c = 0;
            for (SNPPlink snp:snps) {
                char a = Allele.getChar(snp.getA());
                char b = Allele.getChar(snp.getB());
                switch (g[c]) {
                    case 0:
                        pedF.print(" " + a + " " + a);
                        break;
                    case 1:
                        pedF.print(" " + a + " " + b);
                        break;
                    case 2:
                        pedF.print(" " + b + " " + b);
                        break;
                    default:
                        pedF.print(" 0 0");
                        break;
                }
                c++;
            }
            pedF.println("");
        }
        pedF.close();
    }    
    
    
    public void close() {
        pedF.close();
    }
}
