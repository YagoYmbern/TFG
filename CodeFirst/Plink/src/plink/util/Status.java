/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util;

/**
 *
 * @author olao
 */
public enum Status {
    DAMAGING, BENIGN, NaN;

    public static String getString(Status a) {
        switch (a) {
            case DAMAGING: return "DAMAGING";
            case BENIGN: return "BENIGN";
            default: return "NaN";

        }
    }

    /**
     * Get the allele. a must be either A, C, G, T or 1, 2, 3, 4
     *
     * @param a
     * @return
     */
    public static Status getStatus(String a) {
        if(a.equals("DAMAGING"))
        {
            return DAMAGING;
        }
        if(a.equals("BENIGN"))
        {
            return BENIGN;
        }
        return NaN;
    }
    
    @Override
    public String toString() {
        return "" + Status.getString(this);
    }
}
