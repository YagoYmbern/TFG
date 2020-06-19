/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package locus;

import demography.Population;
import exception.ExceptionFragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import parametersSimulation.MutationList;

/**
 * Class to store the observed mutations associated to the sequence
 *
 * @author oscar_000
 */
public class Sequence extends LinkedList<Double> {

    public Sequence(Population p, short i, MutationList m) {
        this.p = p;
        this.i = i;
        this.m = m;
    }

    private final Population p;
    private final short i;
    private final ArrayList<FragmentWithAncestry> fragmentsWithAncestry = new ArrayList<>();
    private final MutationList m;

    /**
     * Get the mutation list containing all the mutations that have been found
     * in this sequence and the others
     *
     * @return
     */
    public MutationList getM() {
        return m;
    }

    @Override
    public boolean add(Double d) {
        m.add(d);
        return super.add(d);
    }

    /**
     * Add a fragment with ancestry
     *
     * @param fragmentAncestry
     */
    public void add(FragmentWithAncestry fragmentAncestry) {
        fragmentsWithAncestry.add(fragmentAncestry);
    }

    /**
     * Get the fragments with ancestry. Fragments are by nature unsorted by
     * position!
     *
     * @return
     */
    public ArrayList<FragmentWithAncestry> getFragmentsWithAncestry() throws ExceptionFragment {
            Collections.sort(fragmentsWithAncestry);     
        return fragmentsWithAncestry;
    }

    @Override
    public String toString() {
        return p.toString() + " , " + i + " " + super.toString();
    }
}
