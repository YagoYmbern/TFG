/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plink.util;

/**
 *
 * @author Administrator
 */
public enum Allele {
    A, T, C, G,N;
   
    public static char getChar(Allele a)
    {
        if(a==A)
        {
            return 'A';
        }
        if(a==C)
        {
            return 'C';
        }
        if(a==T)
        {
            return 'T';
        }
        if(a==G)
        {
            return 'G';
        }
        return 'N';
    }

    public static boolean isNull(Allele a)
    {
        return Allele.getChar(a)=='N';
    }

    /**
     * Get the allele. a must be either A, C, G, T or 1, 2, 3, 4
     * @param a
     * @return 
     */
    public static Allele getAllele(char a)
    {
        if(a=='A' | a=='1')
        {
            return Allele.A;
        }
        if(a=='C' | a=='2')
        {
            return Allele.C;
        }
        if(a=='G' | a=='3')
        {
            return Allele.G;
        }
        if(a=='T' | a=='4')
        {
            return Allele.T;
        }
        return Allele.N;
    }

    @Override
    public String toString()
    {
        return "" + Allele.getChar(this);
    }

    public Allele transpose()
    {
        if(this==Allele.A)
        {
            return T;
        }
        if(this==Allele.C)
        {
            return G;
        }
        if(this==Allele.G)
        {
            return C;
        }
        if(this==Allele.T)
        {
            return A;
        }
        return N;
    }        
}
