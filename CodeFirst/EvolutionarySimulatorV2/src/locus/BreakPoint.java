/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package locus;

import java.util.LinkedList;


/**
 *
 * @author olao
 */
public class BreakPoint extends LinkedList<Sequence> implements Comparable<BreakPoint> {

    /**
     * Create a new fragment.
     *
     * @param position
     */
    
    public BreakPoint(int position)  {
        this.position = position;
    }

    private final int position;

    public int getPosition() {
        return position;
    }
    
    /**
     * Compare by the position
     *
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(BreakPoint o) {
        return new Integer(this.getPosition()).compareTo(o.getPosition());
    }
}