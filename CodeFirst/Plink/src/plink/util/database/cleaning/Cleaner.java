/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util.database.cleaning;

import plink.util.SNPGenotype;

/**
 *
 * @author Oscar Lao
 */
public interface Cleaner {
    
    public boolean acceptSNPGenotype(SNPGenotype snp);
    
}
