/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plink.util;

import java.util.Comparator;

/**
 *
 * @author Administrator
 */
public class CompareSNPGenotypeByteByChrAndPosition implements Comparator<SNPGenotypeByteArray>{

    /**
     * Compare by genomic position
     * @param o1
     * @param o2
     * @return
     */
    public int compare(SNPGenotypeByteArray o1, SNPGenotypeByteArray o2) {
        int chrComp = new Byte(o1.getSnp().getChromosome()).compareTo(o2.getSnp().getChromosome());
        if(chrComp==0)
        {
            return new Integer(o1.getSnp().getPosition()).compareTo(o2.getSnp().getPosition());
        }
        return chrComp;
    }
}
