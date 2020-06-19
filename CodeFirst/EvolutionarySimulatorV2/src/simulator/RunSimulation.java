/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

import demography.Population;
import demography.EvolvePopulation;
import demography.Individual;
import event.EventEvolutionarySimulator;
import event.HistoryOfEventsEvolutionarySimulator;
import exception.ExceptionDemography;
import exception.ExceptionFragment;
import exception.ExceptionGenome;
import exception.ExceptionIndividual;
import exception.ExceptionPopulation;
import exception.ParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author Oscar Lao
 */
public class RunSimulation {

    public static ArrayList<EvolvePopulation> run(DemographicModel dm) throws ExceptionFragment, ExceptionIndividual, ExceptionGenome, ParameterException, ExceptionDemography, ExceptionPopulation {
// Define the demography of the model
        dm.defineDemography();
// The demography of this model        
        HistoryOfEventsEvolutionarySimulator gd = dm.getDemography();
        Collections.sort(gd);
        LinkedList<EventEvolutionarySimulator> events = new LinkedList<>();
        events.addAll(gd);
        EventEvolutionarySimulator currentEvent = events.removeFirst();
// Populations
        LinkedList<EvolvePopulation> evolvePopulation = new LinkedList<>();
// Populations that have evolved
        ArrayList<EvolvePopulation> evolvedPopulations = new ArrayList<>();
        for (Population pop : gd.getPopulations()) {
            EvolvePopulation e = new EvolvePopulation(pop, gd);
            evolvePopulation.add(e);
        }
// All the individuals
        ArrayList<Individual> ccInds = new ArrayList<>();

        for (EvolvePopulation e : evolvePopulation) {
            ccInds.addAll(e.getCcIndividuals());
        }

        int generation = 0;

        while (!ccInds.isEmpty()) {
//            System.out.println(generation + " " + ccInds.size());
            if (generation == currentEvent.getWhenOccurred().getFg().getGeneratedNumber().intValue()) {
                try {
                    currentEvent.doEvent();
                } catch (ExceptionDemography tok) {
                    throw new ExceptionDemography(tok.getMessage() + " when trying to run " + currentEvent);
                }
                while (!events.isEmpty() && events.getFirst().getWhenOccurred().getFg().getGeneratedNumber().intValue() == generation) {
                    currentEvent = events.removeFirst();
                    currentEvent.doEvent();
                }
                if (!events.isEmpty()) {
                    currentEvent = events.removeFirst();
                }
            }

            ccInds = new ArrayList<>();

            LinkedList<EvolvePopulation> remaining_pops = new LinkedList<>();
            
//            System.out.print(generation);
// Generate the previous generation (look for fathers and mothers of this generation)
            while(!evolvePopulation.isEmpty())
            {
                EvolvePopulation e = evolvePopulation.removeFirst();
                e.generatePreviousGeneration();
                remaining_pops.add(e);
            }

            evolvePopulation = remaining_pops;            
            remaining_pops = new LinkedList<>();
//             
            while(!evolvePopulation.isEmpty())
            {
                EvolvePopulation e = evolvePopulation.removeFirst();
                e.update_previous_generation();
                remaining_pops.add(e);
            }            
            
            evolvePopulation = remaining_pops;
            remaining_pops = new LinkedList<>();            
            
            while(!evolvePopulation.isEmpty())
            {
                EvolvePopulation e = evolvePopulation.removeFirst();
//                System.out.print(" " + e.getCcIndividuals().size());
                ccInds.addAll(e.getCcIndividuals());                
                if(e.getPopulation().isActive())
                {
                    remaining_pops.add(e);
                }
                else
                {
                    evolvedPopulations.add(e);
                }                
            }
            
//            System.out.println("");
          evolvePopulation = remaining_pops;
            generation++;
        }
        evolvedPopulations.addAll(evolvePopulation);
        return evolvedPopulations;
    }
}
