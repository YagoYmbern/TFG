/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util.demography;
import plink.util.demography.Individual;


/**
 *
 * @author olao
 */
public class Individual_Ancient extends Individual{
    public Individual_Ancient(Individual i, double age, double lat, double lon)
    {
        super(i.getFamilyName(), i.getName());
        this.age = age;
        this.lat = lat;
        this.lon = lon;
    }
    
    private final double age, lat, lon;

    public double getAge() {
        return age;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
    
    @Override
    public String toString()
    {
        return super.toString() + " " + age + " " + lat + " " + lon;
    }
}
