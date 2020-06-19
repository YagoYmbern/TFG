/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util.base_conversion;

/**
 *
 * @author Oscar Lao
 */
public class TernaryConversion {

    /**
     * Conver the decimal value to ternary (base 3)
     * @param value
     * @param length
     * @return 
     */
    public static int[] conversion_from_decimal_to_ternary(int value, int length) {
        int[] converted = new int[length];
        int previous = value;
        for (int e = 0; e < length; e++) {
            converted[length - e - 1] = previous % 3;
            previous /= 3;
        }
        return converted;
    }

    /**
     * Convert the ternary value to decimal one
     * @param ternary
     * @return 
     */
    public static int conversion_from_ternary_to_decimal(int[] ternary) {
        int v = 0;
        for (int e = 0; e < ternary.length; e++) {
            v += ternary[e] * Math.pow(3, ternary.length - 1 - e);
        }
        return v;
    }
}
