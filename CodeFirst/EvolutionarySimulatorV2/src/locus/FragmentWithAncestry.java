/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package locus;

import demography.Population;
import exception.ExceptionFragment;

/**
 *
 * @author Oscar Lao
 */
public class FragmentWithAncestry extends Fragment{
    public FragmentWithAncestry(int start, int end, Population ancestry) throws ExceptionFragment {
        super(start, end);
        this.ancestry = ancestry;
    }
    
    private final Population ancestry;

    public Population getAncestry() {
        return ancestry;
    }
    
    @Override
    public String toString()
    {
        return super.toString() + " " + ancestry.toString();
    }
}
