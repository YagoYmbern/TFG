/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package locus;

import demography.Population;
import exception.ExceptionFragment;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeSet;
import maths.RandomPoison;
import maths.RandomUniformWithoutResampling;

/**
 * Class chromosome. It contains the information of the Fragments that have not
 * yet coalesced. The Fragments are maintained as a sorted list and cannot
 * overlap.
 *
 * @author oscar_000
 */
public class Chromosome extends TreeSet<Fragment> {

    public Chromosome() {
    }

    private double totalLength = 0;

    /**
     * Indicate the origin of this chromosome
     *
     * @param population
     * @throws ExceptionFragment
     */
    public void addAncestry(Population population) throws ExceptionFragment {
        for (Fragment f : this) {
            f.addAncestry(population);
        }
    }

    /**
     * Add a new fragment, checking that the physical order is maintained
     *
     * @param f
     * @return true if the element is inserted. False otherwise
     */
    @Override
    public boolean add(Fragment f) {
        totalLength += f.length();
        return super.add(f);
    }

    /**
     * Get the physical position of the beginning of the first fragment
     *
     * @return
     */
    public int getMinimum() {
        return this.first().getStart();
    }

    /**
     * Get the physical position of the end of the last fragment
     *
     * @return
     */
    public int getMaximum() {
        return this.last().getEnd();
    }

    /**
     * Get the total length of fragments covered by this Chromosome
     *
     * @return
     */
    public double getCoveredlLength() {
        return totalLength;
    }

//    /**
//     * Break the fragments of this chromosome according to the breaks
//     *
//     * @param breaks
//     * @return
//     */
//    public Chromosome break_chromosome(int[] breaks) {
//        Chromosome broken = new Chromosome();
//        Iterator<Fragment> fragments = this.iterator();
//        for (int br : breaks) {
//            for (; fragments.hasNext();) {
//                Fragment fr = fragments.next();
//                
//
//            }
//        }
//        return broken;
//    }

    /**
     * Do the recombination backward in time using the chromosomes a1 and a2
     *
     * @param a1 parental chromosome one
     * @param a2 parental chromosome two
     * @param mutation_rate the mutation rate per site and generation of the
     * chromosome
     * @param recombinationRate the recombination rate by bp of the chromosome
     * @param totalSequences the total number of simulated sequences. A fragment
     * is excluded when it contains all the possible simulated sequences (no
     * need to add more mutations, all the sequences will be monomorphic
     * derived)
     * @return
     * @throws ExceptionFragment
     */
    public boolean comesFrom(Chromosome a1, Chromosome a2, double mutation_rate, double recombinationRate, int totalSequences) throws ExceptionFragment {
        LinkedList<Fragment> fragments = new LinkedList<>();
        fragments.addAll(this);
// Recombination rate (lamda) for the Chromosome. Size covered by fragments goes between 0 and 1. To get number of breakpoints, we need to multiply it by the total length of the chromosome
        int startPosition = getMinimum();
        int endPosition = getMaximum();
        // Fragment length of this chromosome where recombination can occur
        double sizeCoveredByFragments = endPosition - startPosition;
// Which of the two parental chromosomes is the first in the breakpoint?            
        Random rand = new Random();
        int start_at_chromosome = rand.nextInt(2);

        Chromosome[] b = new Chromosome[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Chromosome();
        }

        double lamda = recombinationRate * sizeCoveredByFragments;
        RandomPoison poisBr = new RandomPoison(lamda);
        double n_breaks = poisBr.sample();

// If there is a recombination event        
        if (n_breaks > 0) {
// Position of the breaks (in bp)
            RandomUniformWithoutResampling ru = new RandomUniformWithoutResampling(startPosition, endPosition);
// Break points            
            int[] breakso = ru.sample((int) n_breaks);
            int[] breaks = new int[breakso.length + 1];
            System.arraycopy(breakso, 0, breaks, 0, breakso.length);
            breaks[breaks.length - 1] = endPosition;

            int current_break = 0;

            while (!fragments.isEmpty()) {
                Fragment current_fragment = fragments.removeFirst();
// breakpoint is after the fragment                
                if (current_fragment.getEnd() <= breaks[current_break]) {
                    boolean inserted = b[start_at_chromosome].add(current_fragment);
//                    if (!inserted) {
//                        throw new ExceptionFragment("Trying to add an element in the wrong order! " + b[start_at_chromosome].get(b[start_at_chromosome].size() - 1) + " " + current_fragment);
//                    }
                } else {
// breakpoint is either before or within the fragment
                    // Is this fragment within the break?                
                    ArrayList<Fragment> fr_split = current_fragment.splitBy(breaks[current_break]);
                    if (fr_split.size() > 0) {
// One fragment goes to the chromosome we were
                        boolean inserted = b[start_at_chromosome].add(fr_split.get(0));
//                        if (!inserted) {
//                            throw new ExceptionFragment("Trying to add an element in the wrong order! " + b[start_at_chromosome].get(b[start_at_chromosome].size() - 1) + " " + fr_split.get(0));
//                        }
// The other fragment goes to the other but we must check that it is not break by other windows
                        fragments.addFirst(fr_split.get(1));
                    } else {
                        fragments.addFirst(current_fragment);
                    }
                    // go to the next break
                    current_break++;
// Shift chromosome to add
                    start_at_chromosome = 1 - start_at_chromosome;
                }
            }

//            if (breaks.length > 0 && (b[0].size() + b[1].size()) < this.size()) {
//                System.out.println("THIS " + this);
//                System.out.println("BREAK AT " + java.util.Arrays.toString(breaks));
//                System.out.println("A " + b[0]);
//                System.out.println("B " + b[1]);
//                System.out.println("**********************************");
//                System.exit(0);
//
//            }
        } else {
            b[start_at_chromosome].addAll(this);
        }

// now add the fragments to the respective chromosomes
        a1.addOffspringChromosome(b[0], totalSequences, mutation_rate);
//        for (int i = 1; i < a1.size(); i++) {
//            if (a1.get(i).getEnd() < a1.get(i - 1).getStart()) {
//                System.out.println("Cagueit " + a1.get(i) + " " + a1.get(i - 1));
//            }
//            if (a1.get(i).getStart() < a1.get(i - 1).getStart()) {
//                System.out.println("Cagueit " + a1.get(i) + " " + a1.get(i - 1));
//            }
//        }
        a2.addOffspringChromosome(b[1], totalSequences, mutation_rate);
//        for (int i = 1; i < a2.size(); i++) {
//            if (a2.get(i).getEnd() < a2.get(i - 1).getStart()) {
//                System.out.println("Cagueit " + a2.get(i) + " " + a2.get(i - 1));
//            }
//            if (a2.get(i).getStart() < a2.get(i - 1).getStart()) {
//                System.out.println("Cagueit " + a2.get(i) + " " + a2.get(i - 1));
//            }
//        }
        return true;
    }

    /**
     * Collapse the chromosome
     *
     * @param a the offspring chromosome
     * @param totalSequences the number of total sequences
     * @param mutation_rate the mutation rate
     * @throws ExceptionFragment
     */
    public void addOffspringChromosome(Chromosome a, int totalSequences, double mutation_rate) throws ExceptionFragment {
        // Going backward, we add mutations to the sequences associated to this chromosome before merging it with the parental chromosome a
        for (Fragment f : a) {
            f.mutate(mutation_rate);
        }
        // Now we must merge the two list of fragments. In the case where there are two fragments that overlap, we must split it and produce the fragments
        LinkedList<Fragment> list_a = new LinkedList<>();
        list_a.addAll(a);
        Chromosome c_a = new Chromosome();
        c_a.addAll(a);
        Chromosome c_this = new Chromosome();
        c_this.addAll(this);
        LinkedList<Fragment> list_this = new LinkedList<>();
        list_this.addAll(this);
// Empty the ArrayList so we can add the new data.        
        this.clear();
// Iterate over all the elements of a
        while (!list_a.isEmpty()) {
            Fragment fr_a = list_a.removeFirst();
            // a fr_a is not processed if the list_this has already finished
            boolean fragment_is_not_processed = true;
// Iterate over all the elements of this            
            while (!list_this.isEmpty()) {
                fragment_is_not_processed = false;
                Fragment fr_this = list_this.removeFirst();
// Merge the two fragments. Three possible fragments are possible. First not shared, shared fragment, 
                Fragment[] elapsedFragments = fr_a.mergeWith(fr_this);
                // before
                if (elapsedFragments[0] != null) {
                    boolean is_inserted = this.add(elapsedFragments[0]);
                    if (!is_inserted) {
                        System.out.println(fr_a);
                        System.out.println(fr_this);
                        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$");
                        System.out.println(elapsedFragments[0]);
                        System.out.println(elapsedFragments[1]);
                        System.out.println(elapsedFragments[2]);
                        System.out.println("------------------------");
                        for (Fragment f : c_a) {
                            System.out.println(f);
                        }
                        System.out.println("**************");
                        for (Fragment f : c_this) {
                            System.out.println(f);
                        }
//                        throw new ExceptionFragment("Trying to add an element in the wrong order! " + this.get(this.size() - 1) + " " + elapsedFragments[0]);
                    }
                }
// shared
                if (elapsedFragments[1] != null) {
                    if (elapsedFragments[1].size() < totalSequences) {
                        boolean is_inserted = this.add(elapsedFragments[1]);
//                        if (!is_inserted) {
//                            throw new ExceptionFragment("Trying to add an element in the wrong order! " + this.get(this.size() - 1) + " " + elapsedFragments[1]);
//                        }
                    }
// If there is only a shared fragment, then it means that we have completely used fr_a & fr_this
                    if (elapsedFragments[2] == null) {
                        break;
                    }
                }
                // after
                if (elapsedFragments[2] != null) {
// Do not add mutations yet!
                    if (elapsedFragments[2].getEnd() == fr_a.getEnd()) {
                        list_a.addFirst(elapsedFragments[2]);
                        break;
                    } else {
                        list_this.addFirst(elapsedFragments[2]);
                        break;
                    }
                }
            }
            // Add all the fragments that are remanents
            if (fragment_is_not_processed) {
                this.add(fr_a);
            }
        }

        if (!list_this.isEmpty()) {
            while (!list_this.isEmpty()) {
                Fragment fr = list_this.removeFirst();
                boolean is_inserted = this.add(fr);
//                if (!is_inserted) {
//                    throw new ExceptionFragment("Trying to add an element in the wrong order! " + this.get(this.size() - 1) + " " + fr);
//                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder a = new StringBuilder();
        for (Fragment f : this) {
            a.append(f).append("\n");
        }
        return a.toString();
    }
}
