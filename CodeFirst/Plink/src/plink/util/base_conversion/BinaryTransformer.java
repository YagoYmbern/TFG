/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util.base_conversion;

/**
 *
 * @author Administrator
 */
public class BinaryTransformer {

    /**
     * Transform the data in byte array
     * @param value
     * @param numberByte
     * @return
     */
    public static byte[] byteCode(int value, int numberByte) {
        byte[] data = new byte[numberByte];
//        int minNumberBytes = getMinimumNumberBytes(value);
        int v = value;
        int i = 0;
        while (v > 0) {
            data[numberByte - i - 1] = new Byte((byte) (v % 2));
            v = (int) Math.round(v / 2);
            i++;
        }
        return data;
    }

    /**
     * Code the byteArray.
     * @param binaryArray
     * @return 
     */
    public static int byteDecode(byte[] binaryArray) {
        int bCode = 0;
        for (int n = 0; n < binaryArray.length; n++) {
            bCode += Math.pow(2, binaryArray.length - 1 - n) * (int) binaryArray[n];
        }
        return bCode;
    }

    /**
     * Get the number of non shared bytes between valueOne and valueTwo
     * @param valueOne
     * @param valueTwo
     * @return 
     */
    public static int getNumberOfNonSharedBytes(int valueOne, int valueTwo) {
        int maxNumberBytes = Math.max(getMinimumNumberBytes(valueOne), getMinimumNumberBytes(valueTwo));
        byte[] byteOne = byteCode(valueOne, maxNumberBytes);
        byte[] byteTwo = byteCode(valueTwo, maxNumberBytes);
        int dif = 0;
        for (int b = 0; b < maxNumberBytes; b++) {
            if (byteOne[b] != byteTwo[b]) {
                dif++;
            }
        }
        return dif;
    }

    /**
     * Get the number of non shared bytes between valueOne and valueTwo
     * @param valueOne
     * @param valueTwo
     * @return 
     */
    public static int getNumberOfNonSharedBytes(byte[] one, byte[] two) {
        int diff = 0;
        if (one.length > two.length) {
            return getNumberOfNonSharedBytes(two, one);
        }

        int bytesWithoutComparison = two.length - one.length;

        for (int b = 0; b < bytesWithoutComparison; b++) {
            diff += two[b];
        }

// comparable remaining
        for (int b = 0; b < one.length; b++) {
            diff += Math.abs(one[b] - two[b + bytesWithoutComparison]);
        }

        return diff;
    }

    /**
     * Get the number of non shared bytes between valueOne and valueTwo
     * @param valueOne
     * @param valueTwo
     * @return 
     */
    public static int getNumberOfConsecutiveSharedBytes(byte[] one, byte[] two) {
        int length = 0;
        int maxLength = 0;
        if (one.length > two.length) {
            return getNumberOfConsecutiveSharedBytes(two, one);
        }

        int bytesWithoutComparison = two.length - one.length;

        for (int b = 0; b < bytesWithoutComparison; b++) {
            if (two[b] == 0) {
                length++;
            } else {
                maxLength = Math.max(maxLength, length);
                length = 0;
            }
        }

// comparable remaining
        for (int b = 0; b < one.length; b++) {
            if (one[b] == two[b + bytesWithoutComparison]) {
                length++;
            } else {
                maxLength = Math.max(maxLength, length);
                length = 0;
            }
        }
        return maxLength;
    }

    /**
     * Get the minimum number of bytes to do a binary representation of this value
     * @param value
     * @return the minimum number of bytes to do a binary representation
     */
    public static int getMinimumNumberBytes(int value) {
        return (int) Math.ceil(Math.log(value + 1) / Math.log(2));
    }

    public static void main(String[] args) {
        byte[] binaryArray = new byte[4];
        for (int h1 = 0; h1 < 2; h1++) {
            binaryArray[0] = (byte) h1;
            for (int h2 = 0; h2 < 2; h2++) {
                binaryArray[1] = (byte) h2;
                for (int h3 = 0; h3 < 2; h3++) {
                    binaryArray[2] = (byte) h3;
                    for (int h4 = 0; h4 < 2; h4++) {
                        binaryArray[3] = (byte) h4;
                        System.out.println(java.util.Arrays.toString(binaryArray) + " " + BinaryTransformer.byteDecode(binaryArray));
                    }
                }
            }
        }
    }
}
