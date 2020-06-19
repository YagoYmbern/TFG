/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package plink.util;

import java.util.ArrayList;

/**
 *
 * @author oscar_000
 */
public class IndividualBySNPMatrix extends ArrayList<IndividualGenotypeByte>{
    public IndividualBySNPMatrix(ArrayList<SNPPlink> snps)
    {
        this.snps = snps;
    }
    
    private ArrayList<SNPPlink> snps;
    
    public ArrayList<SNPPlink> getSNPs()
    {
        return snps;
    }
}
