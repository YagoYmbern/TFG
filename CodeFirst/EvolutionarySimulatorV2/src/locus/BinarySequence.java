/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package locus;

import exception.ExceptionFragment;
import exception.ExceptionMutation;
import java.util.ArrayList;
import java.util.Collections;
import parametersSimulation.MutationList;

/**
 *
 * @author olao
 */
public class BinarySequence {

    public BinarySequence(Sequence s) throws ExceptionMutation, ExceptionFragment{
        this.binary = getBinarySequence(s);
        this.m = s.getM();
        this.s = s;
    }

    private final MutationList m;
    private final byte[] binary;
    private final Sequence s;

    
    /**
     * Get the mutationList
     * @return 
     */
    public MutationList getM() {
        return m;
    }
    
    
    /**
     * Get the binary sequence
     *
     * @return
     */
    public byte[] getBinary() {
        return binary;
    }

    private byte[] getBinarySequence(Sequence s) throws ExceptionMutation, ExceptionFragment{
        byte[] seq = new byte[s.getM().size()];
        for (double d : s) {            
            int id = Collections.binarySearch(s.getM(), d);
            if (id < 0) {
                throw new ExceptionMutation("Attempting to add a mutation that does not exist in the list of mutations!");
            }
            else
            {
                byte a = 1;
                seq[id] = a;                
            }
        }
        return seq;
    }

    /**
     * Get the binary representation of the fragment
     * @param f
     * @return 
     */
    public byte[] getBinary(Fragment f) {
        ArrayList<Integer> i = new ArrayList<>();
        for (int e = 0; e < m.size(); e++) {
            if (m.get(e) >= f.getStart() && m.get(e) <= f.getEnd()) {
                i.add(e);
            }
        }

        byte[] subs = new byte[i.size()];

        for (int e = 0; e < i.size(); e++) {
            subs[e] = binary[i.get(e)];
        }

        return subs;
    }
    
    /**
     * Get the fragments with their associated ancestries associated that are within this fragment
     * @param f
     * @return
     * @throws ExceptionFragment 
     */
    public ArrayList<FragmentWithAncestry> get_the_Ancestries_of_this_fragment(Fragment f) throws ExceptionFragment
    {
        ArrayList<FragmentWithAncestry> frs = new ArrayList<>();
        
        for(FragmentWithAncestry fr:s.getFragmentsWithAncestry())
        {
            Fragment [] fm = fr.mergeWith(f);
            if(fm[1]!=null)
            {
                frs.add(new FragmentWithAncestry(fm[1].getStart(), fm[1].getEnd(), fr.getAncestry()));
            }
        }        
        return frs;
    }
}
