package com.example.c482pa.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Iterator;

/**
 * This class has Part and Product objects.
 * Various methods for these objects such as searching, deleting, and more have been provided.
 *
 * @author Aimy Kohli
 */
public class Inventory {

    //Object reference arrays
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Returns An ObservableList of all parts
     * @return An ObservableList with references to the Part objects
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * Returns An ObservableList of all products
     * @return An ObservableList with references to the Product objects
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

    //Methods for the Parts section

    /**
     * Method to search for a specific part by id
     * @param partId The id to search for
     * @return The Part object if the part is found. Else return null
     */
    public static Part lookupPart(int partId){

        for(int i = 0; i < allParts.size(); i++){
            Part searchResults = allParts.get(i);

            if(searchResults.getId() == partId){
                return searchResults;
            }
        }
        return null;
    }

    /**
     * Method for search for a specific part by name
     * @param partName The part name to search for
     * @return An ObservableList that has references to a Part object
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> searchResults = FXCollections.observableArrayList();

        for(Part part : allParts){
            if(part.getName().toLowerCase().contains(partName.toLowerCase())){
                searchResults.add(part);
            }
        }
        return searchResults;
    }

    /**
     * Add a part
     * @param newPart The Part object to be added
     */
    public static void addPart(Part newPart){
            allParts.add(newPart);
    }

    /**
     * Remove a Part from the Inventory
     * @param selectedPart The Part object to be removed
     * @return Returns true if the part was removed, if not then false is returned
     */
    public static boolean deletePart(Part selectedPart){
        Iterator<Part> iterator = allParts.iterator();
        while(iterator.hasNext()){
            Part partSearch = iterator.next();
            if(partSearch == selectedPart){
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Update a Part object in the Inventory
     * @param index The Part object to look up, helps determine the location of the object
     * @param selectedPart The Part object which will overwrite the existing Part
     */
    public static void updatePart(int index, Part selectedPart){
        Part partToRemove = lookupPart(selectedPart.getId());

        ObservableList<Product> productsWithParts = getAllRelatedParts(partToRemove);

        //Remove associated parts from a product
        for(Product productUpdate : productsWithParts){
            productUpdate.deleteAssociatedPart(partToRemove);
        }

        deletePart(partToRemove);
        allParts.set(index, selectedPart);

        //Re-add the associated parts
        for(Product productUpdate : productsWithParts){
            productUpdate.addAssociatedPart(selectedPart);
        }
    }

    /**
     * Method that will determine the size of the part inventory
     * @return The Part inventory size
     */
    public static int partInvenSize(){
        return allParts.size();
    }

    /**
     * Iterates through the Part objects to confirm that part is not associated with one or more product
     * @param part The part to delete
     * @return Returns true if the part is associated with one or more products, false if it does not
     */
    public static boolean partDeleteValidation(Part part){
        for(int i = 0; i < allProducts.size(); i++){
            if(allProducts.get(i).getAllAssociatedParts().contains(part)){
                return true;
            }
        }
        return false;
    }

    //Methods for the Products section

    /**
     * Method to search for a specific product by id
     * @param productId The id to search for
     * @return The Product object if the product is found. Else return null
     */
    public static Product lookupProduct(int productId){

        for(int i = 0; i < allProducts.size(); i++){
            Product searchResults = allProducts.get(i);

            if(searchResults.getId() == productId){
                return searchResults;
            }
        }
        return null;
    }

    /**
     * Method to search for a specific product by name
     * @param productName The product name to search for
     * @return An ObservableList that has references to a Product object
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> searchResults = FXCollections.observableArrayList();

        for(Product product : allProducts){
            if(product.getName().toLowerCase().contains(productName.toLowerCase())){
                searchResults.add(product);
            }
        }
        return searchResults;
    }

    /**
     * Add a product
     * @param newProduct The Product object to be added
     */
    public static void addProduct(Product newProduct){
        if(newProduct != null){
            allProducts.add(newProduct);
        }
    }

    /**
     * Remove a Product from the Inventory
     * @param selectedProduct The Product object to be removed
     * @return Returns true if the product is deleted, else returns false
     */
    public static boolean deleteProduct(Product selectedProduct){
        Iterator<Product> iterator = allProducts.iterator();

        while(iterator.hasNext()){
            Product productSearch = iterator.next();
            if(productSearch == selectedProduct){
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Update a Product object in the Inventory
     * @param index The Product object to look up, helps determine the location of the object
     * @param newProduct The Product object which will overwrite the existing Product
     */
    public static void updateProduct(int index, Product newProduct){
        getAllProducts().set(index, newProduct);
    }

    /**
     * Method that will determine the size of the product inventory
     * @return The Product inventory size
     */
    public static int productInvenSize(){
        return allProducts.size();
    }

    /**
     * Iterates through the Product objects to confirm that a product does not contain one or more parts
     * @param product The product to delete
     * @return Returns true if a product contains one or more parts, false if it does not
     */
    public static boolean productDeleteValidation(Product product){
        int productID = product.getId();
        for(int i = 0; i < allProducts.size(); i++){
            if(allProducts.get(i).getId() == productID){
                if(!allProducts.get(i).getAllAssociatedParts().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks a part to see if it is associated with a product
     * @param partCheck The Part object that needs to be checked
     * @return An ObservableList with all products that are associated with a part
     */
    public static ObservableList<Product> getAllRelatedParts (Part partCheck){
        ObservableList<Product> productHasPart = FXCollections.observableArrayList();

        for(Product productCheck: Inventory.getAllProducts())
            for (Part relatedPart: productCheck.getAllAssociatedParts()){
                if(relatedPart == partCheck){
                    productHasPart.add(productCheck);
                }
            }
        return productHasPart;
    }

}
