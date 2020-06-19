/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.read;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import plink.util.SNPGenotypeByteArray;
import plink.util.demography.Individual;

/**
 *
 * @author olao
 */
public interface ReadSNPGenotypeArrayFile {
    public Individual[] getIndividuals();
    public SNPGenotypeByteArray nextSNPGenotype() throws Exception;
    public void close() throws IOException;    
    public HashMap<String,ArrayList<Integer>> classify_individuals_by_family_id(); 
    public SNPGenotypeByteArray nextSNPGenotype(FunctionToExtractChromosomalPosition ftcp) throws Exception;
}
