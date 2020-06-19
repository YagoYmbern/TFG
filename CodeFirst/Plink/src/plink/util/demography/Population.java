/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plink.util.demography;

/**
 *
 * @author 503760
 */
public class Population {
    public Population(String name, double latitude, double longitude)
    {
        setName(name);
        setLatitude(latitude);
        setLongitude(longitude);
    }
    
    private String name;
    private double latitude, longitude;

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Population)
        {
            return this.getName().equals(((Population)o).getName());
        }
        return false; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        return getName().hashCode(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
