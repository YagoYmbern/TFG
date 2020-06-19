/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parametersSimulation;

import java.util.ArrayList;
import locus.Fragment;

/**
 *
 * @author oscar_000
 */
public class MutationList extends ArrayList<Double> {

    @Override
    public boolean add(Double d) {
        double dd = d;
        for (int e = 0; e < this.size(); e++) {
            if (this.get(e) == dd) {
                return false;
            }
            if (this.get(e) > dd) {
                if (this.get(e) != dd) {
                    super.add(e, d);
                    return true;
                }
            }
        }
        super.add(d);
        return true;
    }
    
    /**
     * Retrieve the mutations in this fragment fr
     * @param fr
     * @return 
     */
    public MutationList mutations_of_fragment(Fragment fr)
    {
        MutationList m = new MutationList();
        for(double mm:this)
        {
            if(mm>=fr.getStart() && mm<=fr.getEnd())
            {
                m.add(mm);
            }
        }        
        return m;
    }
    
}
