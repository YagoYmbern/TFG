/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Oscar Lao
 */
public class Test {
    public static void main(String[] args)
    {
        ArrayList<Double> sorted_list = new ArrayList<>();
        Random ran = new Random();
        for(int k=0;k<10;k++)
        {
            Test.add(sorted_list, k);
        }
        Test.add(sorted_list,0.0);
        
        for(double d:sorted_list)
        {
            System.out.println(d);
        }
        
    }
    
    public static void add(ArrayList<Double> sorted_list, double d)
    {
        if(sorted_list.isEmpty())
        {
            sorted_list.add(d);
        }
        else
        {
            boolean not_added = true;
            for(int e=0;e<sorted_list.size();e++)
            {
                if(sorted_list.get(e) > d)
                {
                    not_added = false;
                    sorted_list.add(e, d);
                    break;
                }
            }
            if(not_added)
            {
                sorted_list.add(d);
            }
        }                    
    }    
}
