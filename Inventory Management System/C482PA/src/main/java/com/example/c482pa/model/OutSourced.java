package com.example.c482pa.model;

/**
 * The OutSourced class extends from the Part class.
 * This class will provide properties relating to the outsourced part.
 *
 * @author Aimy Kohli
 */
public class OutSourced extends Part {
    //Variables
    private String companyName;

    /**
     * Constructor for the InHouse class
     * @param id Part id
     * @param name Part name
     * @param price Price of the part
     * @param stock Stock number of the part
     * @param min Minimum stock of the part
     * @param max Maximum stock of the part
     * @param companyName The name of the company
     */
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Setter for the name of the company
     * @param companyName The name of the company
     */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    /**
     * Getter for the name of the company
     * @return The name of the company
     */
    public String getCompanyName(){
        return companyName;
    }
}
