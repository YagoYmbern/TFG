/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vcf;

import plink.read.ReadBEDFile;
import plink.util.Allele;
import plink.util.SNPGenotypeByteArray;
import plink.util.demography.Individual;
import write.OutputFile;
import write.WriteFile;

/**
 *
 * @author Oscar Lao
 */
public class WriteVCF {

    public WriteVCF(ReadBEDFile rb) {
        this.rb = rb;
    }

    private ReadBEDFile rb;

    public void writeVCF(OutputFile wf) throws Exception{
//        wf.println("fileformat=VCFv4.2");
//        wf.println("##FORMAT=<ID=GT,Number=1,Type=Integer,Description=\"Genotype\">");
//        wf.println("##FORMAT=<ID=GP,Number=G,Type=Float,Description=\"Genotype Probabilities\">");
//        wf.println("##FORMAT=<ID=PL,Number=G,Type=Float,Description=\"Phred-scaled Genotype Likelihoods\">");
        StringBuilder a = new StringBuilder();
        a.append("#CHROM	POS	ID	REF	ALT	QUAL	FILTER	INFO	FORMAT");
        Individual[] inds = rb.getIndividuals();
        for (Individual i : inds) {
            a.append("\t").append(i.getName());
        }
        wf.println(a.toString());

        SNPGenotypeByteArray snp = rb.nextSNPGenotype();        
        int c = 0;
        while (snp != null) {
            StringBuilder b = new StringBuilder();
            if(c%1000==0)
            {
                System.out.println(c + " " + snp.getSnp().getChromosome());
            }
                byte[] gg = snp.getGenotypes();
                if (snp.getSnp().getA() != Allele.N && snp.getSnp().getB() != Allele.N) {
                    double f = 0;
                    double n = 0;
                    for (byte g : gg) {
                        if (g < 3) {
                            f += g;
                            n += 2;
                        }
                    }
                    f /= n;
                    if (f * (1 - f) != 0) {
//20	1291018	rs11449	G	A	.	PASS	.	GT	0/0	0/1
                        b.append(snp.getSnp().getChromosome()).append("\t").append(snp.getSnp().getPosition()).append("\t").append(snp.getSnp().getRsId()).append("\t").append(snp.getSnp().getA()).append("\t").append(snp.getSnp().getB()).append("\t.\tPASS\t.\tGT");
                        for (byte g : gg) {
                            switch (g) {
                                case 0:
                                    b.append("\t0/0");
                                    break;
                                case 1:
                                    b.append("\t0/1");
                                    break;
                                case 2:
                                    b.append("\t1/1");
                                    break;
                                default:
                                    b.append("\t./.");
                                    break;
                            }
                        }
                        wf.println(b.toString());
                    }
                }


            snp = rb.nextSNPGenotype();
            c++;
        }
        rb.close();
        wf.close();        
    }
    
    /**
     * Write the VCF as output
     *
     * @param output outputFile
     * @param chr chromosome to be written
     */
    public void writeVCF(String output, int chr) throws Exception {
        WriteFile wf = new WriteFile(output);
//        wf.println("fileformat=VCFv4.2");
//        wf.println("##FORMAT=<ID=GT,Number=1,Type=Integer,Description=\"Genotype\">");
//        wf.println("##FORMAT=<ID=GP,Number=G,Type=Float,Description=\"Genotype Probabilities\">");
//        wf.println("##FORMAT=<ID=PL,Number=G,Type=Float,Description=\"Phred-scaled Genotype Likelihoods\">");
        wf.print("#CHROM	POS	ID	REF	ALT	QUAL	FILTER	INFO	FORMAT");
        Individual[] inds = rb.getIndividuals();
        for (Individual i : inds) {
            wf.print("\t" + i.getName());
        }
        wf.println("");

        boolean insideChromosome = false;
        SNPGenotypeByteArray snp = rb.nextSNPGenotype();        
        int c = 0;
        while (snp != null) {
            if(c%1000==0)
            {
                System.out.println(c + " " + snp.getSnp().getChromosome());
            }
            if (snp.getSnp().getChromosome() == chr) {
                insideChromosome = true;
                byte[] gg = snp.getGenotypes();
                if (snp.getSnp().getA() != Allele.N && snp.getSnp().getB() != Allele.N) {
                    double f = 0;
                    double n = 0;
                    for (byte g : gg) {
                        if (g < 3) {
                            f += g;
                            n += 2;
                        }
                    }
                    f /= n;
                    if (f * (1 - f) != 0) {
//20	1291018	rs11449	G	A	.	PASS	.	GT	0/0	0/1
                        wf.print(snp.getSnp().getChromosome() + "\t" + snp.getSnp().getPosition() + "\t" + snp.getSnp().getRsId() + "\t" + snp.getSnp().getA() + "\t" + snp.getSnp().getB() + "\t.\tPASS\t.\tGT");
                        for (byte g : gg) {
                            switch (g) {
                                case 0:
                                    wf.print("\t0/0");
                                    break;
                                case 1:
                                    wf.print("\t0/1");
                                    break;
                                case 2:
                                    wf.print("\t1/1");
                                    break;
                                default:
                                    wf.print("\t./.");
                                    break;
                            }
                        }
                        wf.println("");
                    }
                }
            }
            else
            {
                if(insideChromosome)
                {
                    break; // We have gone through all the SNPs of the chromosome.
                }
            }

            snp = rb.nextSNPGenotype();
            c++;
        }
        rb.close();
        wf.close();
    }
}
