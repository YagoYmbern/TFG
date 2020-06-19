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
import maths.RandomPoison;

/**
 * Fragment class. It stores the information of a Fragment (start, end) and the
 * sampled sequences that have coalesced at this Fragment
 *
 * @author oscar_000
 */
public class Fragment extends LinkedList<Sequence> implements Comparable<Fragment> {

    public Fragment()
    {
        
    }
    
    /**
     * Create a new fragment.
     *
     * @param start the start of the fragment.
     * @param end the end of the fragment. Start and end must be between 0 and 1
     * @throws ExceptionFragment
     */
    public Fragment(int start, int end) throws ExceptionFragment {
        if (start > end) {
            throw new ExceptionFragment("End " + end + " smaller than start " + start);
        }
        this.start = start;
        this.end = end;
    }

    private int start;
    private int end;

    /**
     * Create a fragment with breakPoints start and end
     * @param start
     * @param end
     * @return
     * @throws ExceptionFragment 
     */
    public static Fragment createFragment(BreakPoint start, BreakPoint end) throws ExceptionFragment
    {
        Fragment nf = new Fragment(start.getPosition(),end.getPosition());
        nf.addAll(start);
        return nf;
    }
    
    /**
     * Length of the fragment
     *
     * @return
     */
    public double length() {
        return end - start;
    }

    /**
     * Get the start and end positions
     * @return 
     */
    public LinkedList<BreakPoint> getPositions()
    {
        LinkedList<BreakPoint> pos = new LinkedList<>();
        BreakPoint s = new BreakPoint(getStart());
        s.addAll(this);
        BreakPoint f = new BreakPoint(getEnd());
        f.addAll(this);
        return pos;
    }
    
    /**
     * Add ancestry to this fragment in the sequence
     *
     * @param p
     * @throws ExceptionFragment
     */
    public void addAncestry(Population p) throws ExceptionFragment {
        for (Sequence s : this) {
            s.add(new FragmentWithAncestry(this.getStart(), this.getEnd(), p));
        }
    }

    /**
     * Add the mutation m to all the sequences associated to this fragment
     *
     * @param m
     * @throws ExceptionFragment
     */
    public void addMutation(double m) throws ExceptionFragment {
        if (m >= getStart() && m <= getEnd()) {
            this.stream().forEach((s) -> {
                s.add(m);
            });
        } else {
            throw new ExceptionFragment("Mutation out of range " + m + " start " + getStart() + " " + getEnd());
        }
    }

    /**
     * Mutate the fragment given the mutation rate
     *
     * @param mutation_rate
     * @throws ExceptionFragment
     */
    public void mutate(double mutation_rate) throws ExceptionFragment {
        // Given the mutation rate and the length of the fragment, how many mutations do we expect?
        RandomPoison poisBr = new RandomPoison(mutation_rate * this.length());
        double n_muts = poisBr.sample();
// If there is a recombination event        
        if (n_muts > 0) {
// Position of the breaks (in bp)
            Random ru = new Random();
            int n = 0;
            while(n < n_muts)
            {
// Add the mutation in a sorted way, so no mutations are repeated
                double m = ru.nextInt(this.getEnd()-this.getStart()) + this.getStart();
                if(getFirst().getM().add(m))
                {
                    this.addMutation(m);                    
                    n++;
                }                
            }
        }
    }
    
    /**
     * Check if the fragment contains the position pos
     * @param pos
     * @return 
     */
    public boolean fragment_contains(double pos)
    {
        return pos>=this.getStart() && pos <= this.getEnd();
    }

    /**
     * Set the end of the fragment
     *
     * @param end
     * @throws exception.ExceptionFragment
     */
    public void setEnd(int end) throws ExceptionFragment {
        if (end < start) {
            throw new ExceptionFragment("End " + end + " smaller than start " + start);
        }
        this.end = end;

    }

    /**
     * Set the beginning of the fragment
     *
     * @param start
     * @throws exception.ExceptionFragment
     */
    public void setStart(int start) throws ExceptionFragment {
        if (end < start) {
            throw new ExceptionFragment("End " + end + " smaller than start " + start);
        }
        this.start = start;

    }

    /**
     * Get the end of the fragment
     *
     * @return
     */
    public int getEnd() {
        return end;
    }

    /**
     * Get the beginning of the fragment
     *
     * @return
     */
    public int getStart() {
        return start;
    }

    @Override
    public String toString() {
        StringBuilder a = new StringBuilder();
        a.append(start).append(" - ").append(end).append(" : ");
        this.forEach((s) -> {
            a.append(s.toString()).append("\n");
        });
        return a.toString();
    }

    /**
     * Compare by the starting position
     *
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Fragment o) {
        return new Integer(this.getStart()).compareTo(o.getStart());
    }

    public ArrayList<Fragment> splitBy(int position) throws ExceptionFragment {
        ArrayList<Fragment> frs = new ArrayList<>();
        if (position >= start && position <= end) {
            Fragment before = new Fragment(start, position);
            before.addAll(this);
            Fragment after = new Fragment(position, end);
            after.addAll(this);
            frs.add(before);
            frs.add(after);
        }

        return frs;
    }

    /**
     * Merge this fragment with other fragment
     *
     * @param cc
     * @return
     * @throws ExceptionFragment
     */
    public Fragment[] mergeWith(Fragment cc) throws ExceptionFragment {
        Fragment[] newFragments = new Fragment[3];
        // There is overlap between the two fragments
        if (cc.getStart() <= this.getEnd() && cc.getEnd() >= this.getStart()) {
            Fragment before;
            Fragment shared;
            Fragment after;
            // Complete overlap
            if (cc.getStart() == this.getStart() && this.getEnd() == cc.getEnd() && this.getStart() != this.getEnd()) {
                shared = new Fragment(this.getStart(), this.getEnd());
                shared.addAll(cc);
                shared.addAll(this);
                newFragments[1] = shared;
                return newFragments;
            }

            if (cc.getStart() <= this.getStart()) {
                before = new Fragment(cc.getStart(), this.getStart());
                before.addAll(cc);
// Case where the fragment is contained in cc                    
                if (cc.getEnd() >= this.getEnd()) {
                    shared = new Fragment(this.getStart(), this.getEnd());
                    after = new Fragment(this.getEnd(), cc.getEnd());
                    after.addAll(cc);
                } else {
                    shared = new Fragment(this.getStart(), cc.getEnd());
                    after = new Fragment(cc.getEnd(), this.getEnd());
                    after.addAll(this);
                }
            } else {
                before = new Fragment(this.getStart(), cc.getStart());
                before.addAll(this);
                if (this.getEnd() >= cc.getEnd()) {
                    shared = new Fragment(cc.getStart(), cc.getEnd());
                    after = new Fragment(cc.getEnd(), this.getEnd());
                    after.addAll(this);
                } else {
                    shared = new Fragment(cc.getStart(), this.getEnd());
                    after = new Fragment(this.getEnd(), cc.getEnd());
                    after.addAll(cc);
                }
            }

            if (before.getStart() != before.getEnd()) {
                newFragments[0] = before;
            }
            if (shared.getStart() != shared.getEnd()) {
                shared.addAll(cc);
                shared.addAll(this);
                newFragments[1] = shared;
            }
            if (after.getStart() != after.getEnd()) {
                newFragments[2] = after;
            }
// no overlap            
        } else if (this.getStart() < cc.getStart()) {
            newFragments[0] = this;
            newFragments[2] = cc;
        } else {
            newFragments[0] = cc;
            newFragments[2] = this;
        }
        return newFragments;
    }
}
