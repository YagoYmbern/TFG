/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

import demography.EvolvePopulation;
import demography.IndividualSample;
import demography.ParametersDemography;
import demography.Population;
import demography.Samples_at_time;
import event.HistoryOfEventsEvolutionarySimulator;
import exception.ExceptionDemography;
import exception.ExceptionPopulation;
import exception.ParameterException;
import java.util.ArrayList;
import java.util.Collections;
import locus.BinarySequence;
import locus.FragmentWithAncestry;
import locus.Fragment;
import locus.Sequence;
import maths.FixedValue;
import maths.RandomUniform;
import parametersSimulation.MutationList;
import parametersSimulation.ParametersChromosome;
import parametersSimulation.ParametersGenome;
import parametersSimulation.parameters.ModelParameter;

/**
 *
 * @author olao
 */
public class AdmixtureModel3 extends DemographicModel{

    public AdmixtureModel3(ParametersGenome pg) throws ParameterException, ParameterException, ExceptionPopulation, ExceptionDemography {
        super(pg);
    }

    @Override
    public void defineDemography() throws ParameterException, ExceptionDemography, ExceptionPopulation {
        ArrayList<Population> populations = new ArrayList<>();
        String[] names = {"African", "European", "Easian", "Oceanian", "Neanderthal", "Denisovan"};
        ModelParameter[] Ne = new ModelParameter[names.length];
        Ne[0] = new ModelParameter("NeAfrica", new RandomUniform(new ModelParameter(new FixedValue(10000)), new ModelParameter(new FixedValue(80000)))); // Estos valores no los entiendo
        Ne[1] = new ModelParameter("NeEuropean", new RandomUniform(new ModelParameter(new FixedValue(10000)), new ModelParameter(new FixedValue(20000))));
        Ne[2] = new ModelParameter("NeEasian", new RandomUniform(new ModelParameter(new FixedValue(10000)), new ModelParameter(new FixedValue(20000))));  
        Ne[3] = new ModelParameter("NeOceanian", new RandomUniform(new ModelParameter(new FixedValue(10000)), new ModelParameter(new FixedValue(20000))));  
        Ne[4] = new ModelParameter("NeNeanderthal", new RandomUniform(new ModelParameter(new FixedValue(1000)), new ModelParameter(new FixedValue(10000))));
        Ne[5] = new ModelParameter("NeDenisovan", new RandomUniform(new ModelParameter(new FixedValue(1000)), new ModelParameter(new FixedValue(10000))));
        // Sample size is in number of diploid individuals
        int[] sample_size = {1, 1, 1, 1, 1, 1};
        // times when the samples are sampled
        int[] times = {0, 0, 0, 0, 1414, 2000};
        // Create the painting
        for (int p = 0; p < names.length; p++) {
            ParametersDemography pd = new ParametersDemography(Ne[p]);
            boolean traceAncestralityFromThisPopulationInOtherPopulations = names[p].equals("Denisovan");
            boolean shallICheckTheAncestralityInThisPopulation = names[p].equals("European");
            Population pop = new Population(names[p], pd, shallICheckTheAncestralityInThisPopulation, traceAncestralityFromThisPopulationInOtherPopulations);
            pop.add(new Samples_at_time(sample_size[p], times[p]));
            populations.add(pop);
        }

        events = new HistoryOfEventsEvolutionarySimulator(populations, pg);
//        events.addInitialMigrationBetween(names[0], names[1], new FixedValue(0.00001));
//        events.addInitialMigrationBetween(names[1], names[2], new FixedValue(0.0000000001));                

// EVENTS
// Event tIntrogression Neanderthal Europe
//        ModelParameter tIntrogressionNeanderthal_Europe = new ModelParameter("tIntrogression", new RandomUniform(new ModelParameter(new FixedValue(1000)), new ModelParameter(new FixedValue(2000))));
        ModelParameter tIntrogressionDenisovan_Europe = new ModelParameter("tIntrogression", new FixedValue(1600));

        ModelParameter introgression = new ModelParameter("introgession", new RandomUniform(new ModelParameter(new FixedValue(0.01)), new ModelParameter(new FixedValue(0.05))));
        events.mergeAPercentageOfSourceIntoRecipient(tIntrogressionDenisovan_Europe, "European", "Denisovan", introgression);

// Event time split Oceania Easia

        ModelParameter tSplitOceaniaEasia = new ModelParameter("tSplitOceaniaEasia", new FixedValue(1000));
        events.mergeSourceIntoRecipient(tSplitOceaniaEasia, "Oceanian", "Easian");
        
// Event time split Easia Europe
        ModelParameter tSplitEasiaEurope = new ModelParameter("tSplitEasiaEurope", new FixedValue(1500));
        events.mergeSourceIntoRecipient(tSplitEasiaEurope, "Easian", "European");    

// Event time split Denisovan from Neanderthal
        ModelParameter tSplitDenisovanNeanderthal = new ModelParameter("tSplitDenisovanNeanderthal", new FixedValue(10000));
        events.mergeSourceIntoRecipient(tSplitDenisovanNeanderthal, "Denisovan", "Neanderthal");            
        
// Event time split Europe Africa
//        ModelParameter tSplitAfricaEurope = new ModelParameter("tSplitAfricaEurope", new RandomUniform(tIntrogressionNeanderthal_Europe, new ModelParameter(new FixedValue(4000))));
        ModelParameter tSplitAfricaEurope = new ModelParameter("tSplitAfricaEurope", new FixedValue(1800));
        events.mergeSourceIntoRecipient(tSplitAfricaEurope, "European", "African");        
        
// Event time split Humans from Denisovan
//        ModelParameter tSplitHumanNeanderthal = new ModelParameter("tSplitHumanNeanderthal", new RandomUniform(tIntrogressionNeanderthal_Europe, new ModelParameter(new FixedValue(10000))));
        ModelParameter tSplitHumanNeanderthal = new ModelParameter("tSplitHumanNeanderthal", new FixedValue(14000));

        events.mergeSourceIntoRecipient(tSplitHumanNeanderthal, "African", "Neanderthal");
    }

    public static void main(String[] args) throws Exception {
        ParametersGenome pg = new ParametersGenome(); // Here we create an object with the parameters of the genome
        MutationList m = new MutationList(); // Create an object with the mutations
        pg.add(new ParametersChromosome(500000, m)); // We add to the list of the parameters the mutations and "kilobases"       
        AdmixtureModel3 a = new AdmixtureModel3(pg); // Create an object that contains the previous parameters
        ArrayList<EvolvePopulation> evolvedPopulations = RunSimulation.run(a);// Here we are running the simulation by creating an array for the evolved populations
        Collections.sort(m); // We are sorting the values of the mutations inside the class Collections
        ArrayList<BinarySequence> binSeqs = new ArrayList<>(); // It is the same as the evolved pops but now it is an array with binary sequences
        for (EvolvePopulation e : evolvedPopulations) { //We go through the evolved Populations through the e iterator
            System.out.println(e.getPopulation().getName()); // We are printing the name of the population
            ArrayList<IndividualSample> sample = e.getSampleIndividual(); // We create the object sample in which we get the sample individual ans save it in an array
            for (IndividualSample i : sample) { // We go through all the sample that have sample of the individuals with the iterator i
                Sequence[][] sequences = i.getSequences();// We create a dictionary in which we will have the sequences
                // Sequences by independent locus
                for (Sequence[] seq : sequences) {
                    // Sequence at each chromosome
                    for (int dip = 0; dip < 2; dip++) { // We are going through all the diploids, so the maximum length is 2 and they are binary sequences
                        BinarySequence bs = new BinarySequence(seq[dip]); 
                        binSeqs.add(bs); // We are adding to the variable binSeqs the variable bs in we have the sequence that are diploids
                        //System.out.println(java.util.Arrays.toString(bs.getBinary()));
                        ArrayList<FragmentWithAncestry> fr = seq[dip].getFragmentsWithAncestry(); // Here we are looking for those fragments that are related to the his ancestors
                        if(!fr.isEmpty()) // If there are fragments we enter
                        {
                            for(FragmentWithAncestry f:fr)// We go through all 
                            {
                                System.out.println(f); //Here we are printing the fragments
                            }
                        }
                    }
                }
            }
        }
        
// take a fragment of 100kb and move it along the 1 Mb window, previosuly we have said in the genomes how large is the total window
        
        Fragment window = new Fragment(0, 100000);
        
        for(BinarySequence bs:binSeqs)
        {
            double introgressed_frequency = 0;
            ArrayList<FragmentWithAncestry> intro = bs.get_the_Ancestries_of_this_fragment(window);
            for(FragmentWithAncestry fwa:intro)
            {
                introgressed_frequency += fwa.length();
            }
            introgressed_frequency/= window.length();
            System.out.println(java.util.Arrays.toString(bs.getBinary(window)) + " " + introgressed_frequency);
        }
    }
}

