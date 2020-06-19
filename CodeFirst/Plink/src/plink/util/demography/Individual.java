/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plink.util.demography;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class Individual implements Comparable<Individual>{
    public Individual(String familyName, String name)
    {
        setName(name);
        setFamilyName(familyName);
    }

    private String name, familyName;
    private ArrayList<String> database = new ArrayList<>();
    private ArrayList<Integer> idsOfRelated = new ArrayList<>();
    String paternalId = "0";
    String maternalId = "0";
    private int sex = 0;
    private int phenotype = 0;

    public void addRelatedIndividual(int id)
    {
        idsOfRelated.add(id);
    }
    
    public ArrayList<Integer> getIdsOfRelatedIndividuals()
    {
        return idsOfRelated;
    }
    
    /**
     * Add a database where this individual is present
     * @param database 
     */
    public void addDatabase(String database) {
        this.database.add(database);
    }

    public ArrayList<String> getDatabase() {
        return database;
    }    
    
    public void setMaternalId(String maternalId) {
        this.maternalId = maternalId;
    }

    public void setPaternalId(String paternalId) {
        this.paternalId = paternalId;
    }

    public void setPhenotype(int phenotype) {
        this.phenotype = phenotype;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getMaternalId() {
        return maternalId;
    }

    public String getName() {
        return name;
    }

    public String getPaternalId() {
        return paternalId;
    }

    public int getPhenotype() {
        return phenotype;
    }

    public int getSex() {
        return sex;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Compare by the name
     * @param o
     * @return 
     */
    @Override
    public int compareTo(Individual o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public String toString()
    {
        return getFamilyName() + " " + getName() + " " + getPaternalId() + " " + getMaternalId() + " " + getSex() + " " + getPhenotype();
    }
    
    /**
     * Two individuals are equal if they share the same name and family id
     * @param o
     * @return 
     */
    @Override
    public boolean equals(Object o)
    {
        if(o instanceof Individual)
        {
            Individual i = (Individual)o;
            return this.getFamilyName().equals(i.getFamilyName()) && this.getName().equals(i.getName());
        }
        return false;
    }
}
