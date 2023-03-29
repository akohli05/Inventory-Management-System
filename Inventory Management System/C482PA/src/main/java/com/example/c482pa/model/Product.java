package com.example.c482pa.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Iterator;

/**
 * This class provides methods necessary for creating and returning a product. In addition,
 * methods that help associated a part with a product are provided.
 *
 * @author Aimy Kohli
 */

public class Product {
    //Variables
    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Constructor for the InHouse class
     * @param id Part id
     * @param name Part name
     * @param price Price of the part
     * @param stock Stock number of the part
     * @param min Minimum stock of the part
     * @param max Maximum stock of the part
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.associatedParts = FXCollections.observableArrayList();
    }

    //Setters

    /**
     * Setter for the id of a product
     * @param id The id for the product
     */
    public void setId(int id) {this.id = id;}

    /**
     * Setter for the product name
     * @param name The name of the product
     */
    public void setName(String name) {this.name = name;}

    /**
     * Setter for the price of a product
     * @param price The price of the product
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Setter for the stock level of a product
     * @param stock The stock level of the product
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Setter for the minimum stock level of a product
     * @param min The min stock level of the product
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Setter for the maximum stock level of a product
     * @param max The max stock level of the product
     */
    public void setMax(int max) {
        this.max = max;
    }

    //Getters

    /**
     * Getter for the id of a product
     * @return The id for the product
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the product name
     * @return The name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the price of a product
     * @return The price of the product
     */
    public double getPrice() {
        return price;
    }

    /**
     * Getter for the stock level of a product
     * @return The stock level of the product
     */
    public int getStock() {
        return stock;
    }

    /**
     * Getter for the minimum stock level of a product
     * @return The min stock level of the product
     */
    public int getMin() {
        return min;
    }

    /**
     * Getter for the maximum stock level of a product
     * @return The max stock level of the product
     */
    public int getMax() {
        return max;
    }

    /**
     * Adds a Part object into the relatedParts ObservableList
     * @param part The Part object to add into the ObservableList
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * Deletes associated parts
     * @param selectedAssociatedPart The selected part to remove
     * @return True if the part is deleted, else return false
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        Iterator<Part> iterator = associatedParts.iterator();
        while(iterator.hasNext()){
            Part partSearch = iterator.next();
            if(partSearch == selectedAssociatedPart){
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Gets an ObservableList with associated parts
     * @return An ObservableList of associated parts
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }



}
