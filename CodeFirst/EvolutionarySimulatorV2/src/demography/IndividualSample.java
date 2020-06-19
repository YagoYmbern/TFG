/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demography;

import exception.ExceptionGenome;
import java.util.Collections;
import locus.Sequence;
import parametersSimulation.ParametersGenome;

/**
 *
 * @author Oscar Lao
 */
public class IndividualSample extends Individual {

    public IndividualSample(Population population, ParametersGenome pg) {
        super(population, pg); // We are taking the parameters from the individuals class
    }

    /**
     * Get the sequences associated to this individual
     *
     * @return
     * @throws ExceptionGenome
     */
    public Sequence[][] getSequences() throws ExceptionGenome {
        Sequence[][] sequences = new Sequence[size()][2]; // Create a variable sequences that gets the size and the 2 is related to the second(i really dont know)
        for (int g = 0; g < size(); g++) { //We go through all the sequence 
            for (int dip = 0; dip < 2; dip++) {//As we know it is an haplotype so we need to go through the two "alleles"
                sequences[g][dip] = genome[g].getChromosome(dip).first().getFirst(); // we get the place in the sequence and which of the two alleles are related to the individual
                Collections.sort(sequences[g][dip]); // all these sequences that we have we sort them
            }
        }
        return sequences;
    }
}
