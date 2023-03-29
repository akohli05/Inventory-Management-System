package com.example.c482pa.model;

/**
 * The InHouse class extends from the Part class.
 * This class will provide properties relating to the in house part.
 *
 * @author Aimy Kohli
 */
public class InHouse extends Part{
    //Variables
    private int machineId;

    /**
     * Constructor for the InHouse class
     * @param id Part id
     * @param name Part name
     * @param price Price of the part
     * @param stock Stock number of the part
     * @param min Minimum stock of the part
     * @param max Maximum stock of the part
     * @param machineId Machine id
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId ){
        super(id, name, price, stock, min, max);
        this.machineId = machineId;

    }

    /**
     * Setter for the machine id
     * @param machineId The machine id
     */
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }

    /**
     * Getter for the machine id
     * @return The machine id
     */
    public int getMachineId(){
       return machineId;
    }

}
