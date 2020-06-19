/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util;

import java.util.Comparator;

/**
 *
 * @author oscar_000
 */
public class CompareSNPByChrAndPosition implements Comparator<SNPPlink>{

    @Override
    public int compare(SNPPlink o1, SNPPlink o2) {
        int chr = new Byte(o1.getChromosome()).compareTo(o2.getChromosome());
        return (chr==0)?new Integer(o1.getPosition()).compareTo(o2.getPosition()):chr;
    }
}
