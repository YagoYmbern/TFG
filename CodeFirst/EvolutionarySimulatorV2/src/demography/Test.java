/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demography;

/**
 *
 * @author olao
 */
public class Test {

    public static void main(String[] args) {
        int[] pdm = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int [] new_pdm = new int[pdm.length-1];
        int removedIdx = 4;
        System.arraycopy(pdm, 0, new_pdm, 0, removedIdx);
        System.arraycopy(pdm, removedIdx + 1, new_pdm, removedIdx, pdm.length - 1 - removedIdx);
        System.out.println(java.util.Arrays.toString(new_pdm));

    }

}
