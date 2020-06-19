/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util.base_conversion;

import java.math.BigDecimal;

/**
 *
 * @author Oscar Lao
 */
public class BaseConversion {

    /**
     * Convert the decimal value to base
     * @param value
     * @param length
     * @param base
     * @return 
     */
    public static int[] conversion_from_decimal_to_base(int value, int length, int base) {
        int[] converted = new int[length];
        int previous = value;
        for (int e = 0; e < length; e++) {
            converted[length - e - 1] = previous % base;
            previous /= base;
        }
        return converted;
    }

    /**
     * Convert the value to decimal one using the base
     * @param ternary
     * @param base
     * @return 
     */
    public static int conversion_from_base_to_decimal(int[] ternary, int base) {
        int v = 0;
        for (int e = 0; e < ternary.length; e++) {
            v += ternary[e] * Math.pow(base, ternary.length - 1 - e);
        }
        return v;
    }
    
        /**
     * Convert the value to decimal one using the base
     * @param ternary
     * @param base
     * @return 
     */
    public static double conversion_from_base_to_decimal_double(int[] ternary, int base) {
        double v = 0;
        for (int e = 0; e < ternary.length; e++) {
            v += ternary[e] * Math.pow(base, ternary.length - 1 - e);
        }
        return v;
    }
    
    public static BigDecimal conversion_from_base_to_decimal_as_BigDouble(int[] ternary, int base) {
        BigDecimal v = BigDecimal.valueOf(0);
        BigDecimal baseBD = BigDecimal.valueOf(base);
        for (int e = 0; e < ternary.length; e++) {
            BigDecimal tern = BigDecimal.valueOf(ternary[e]);
            BigDecimal baseBD_pow = baseBD.pow(ternary.length - 1 - e);
            v = v.add(tern.multiply(baseBD_pow));
        }
        return v;
    }
}
