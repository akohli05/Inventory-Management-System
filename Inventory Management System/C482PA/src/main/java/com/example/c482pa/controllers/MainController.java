package com.example.c482pa.controllers;

import com.example.c482pa.model.Inventory;
import com.example.c482pa.model.Part;
import com.example.c482pa.model.Product;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.c482pa.model.Inventory.*;

/**
 *  This is the controller for the MainView.fxml which is the main screen.
 *
 * @author Aimy Kohli
 */

public class MainController implements Initializable {

    //TableView and columns for the Parts
    @FXML
    private TextField tfPartsSearch;

    @FXML
    private TableView<Part> tvParts;

    @FXML
    private TableColumn<Part, Integer> colPartsID;

    @FXML
    private TableColumn<Part, String> colPartsName;

    @FXML
    private TableColumn<Part, Integer> colPartsInven;

    @FXML
    private TableColumn<Part, Double> colPartsPrice;

    //TableView and columns for the Products
    @FXML
    private TextField tfProductsSearch;

    @FXML
    private TableView<Product> tvProducts;

    @FXML
    private TableColumn<Product, Integer> colProductsID;

    @FXML
    private TableColumn<Product, String> colProductsName;

    @FXML
    private TableColumn<Product, Integer> colProductsInven;

    @FXML
    private TableColumn<Product, Double> colProductsPrice;

    //Additional Variables
    private static int modifyPartIndex;
    private static Part modifyPart;
    private static int modifyProductIndex;
    private static Product modifyProduct;

    /**
     * Returns a specific part index that will be used for modifying the correct part
     * @return The part index
     */
    public static int getModifyPartIndex(){
        return modifyPartIndex;
    }

    /**
     * Returns a specific product index that will be used for modifying the correct product
     * @return The product index
     */
    public static int getModifyProductIndex(){
        return modifyProductIndex;
    }


    //Methods for the Parts section

    /**
     * <h5>RUNTIME ERROR:</h5>
     *
     *<p>I ran into some errors while coding this method. Originally, I had
     * excluded the try and catch phrase in this method. Everytime I would
     * run my application and enter in a string, I would receive errors relating
     * the NumberFormatException. After adding on a try and catch phrase, with
     * the catch statement having a NumberFormatException, I was able to fix this
     * issue properly.</p>
     *
     * Method that searches for a specific part
     * @param event Search parts field action
     */
    @FXML
    void handlePartsSearch(ActionEvent event) {
        String partToSearch = tfPartsSearch.getText();
        ObservableList<Part> searchResults = lookupPart(partToSearch);

           try {
                if (searchResults.size() == 0) {
                    Part tempPart = Inventory.lookupPart(Integer.parseInt(partToSearch));
                    if (tempPart != null) {
                        searchResults.add(tempPart);
                        tvParts.setItems(searchResults);
                    } else {
                        AlertBox.display("Part Search Error", "Part not found.");
                    }
                }

            } catch (NumberFormatException e) {
               AlertBox.display("Part Search Error", "Part not found.");
            }

        tvParts.setItems(searchResults);
        tfPartsSearch.setText("");

    }

    /**
     * Launches a screen for adding parts
     * @param event Add part button action
     * @throws IOException Thrown by FXMLLoader
     */
    @FXML
    void openAddPartsScreen(ActionEvent event) throws IOException{
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/AddPartView.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch(IOException e){

        }
    }

    /**
     * Launches a screen for modifying parts
     * @param event Modify part button
     * @throws IOException Thrown by FXMLLoader
     */
    @FXML
    void openModifyPartsScreen(ActionEvent event) throws IOException{
        if(tvParts.getSelectionModel().getSelectedItem() == null){
            AlertBox.display("Error Opening Modify Parts Screen","You must select a part" +
                    " to modify first.");
        }
        else {
            try {
                modifyPart = tvParts.getSelectionModel().getSelectedItem();
                modifyPartIndex = Inventory.getAllParts().indexOf(modifyPart);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ModifyPartView.fxml"));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {

            }
        }
    }

    /**
     * Method for updating the Part's table view
     */
    public void updatePartsTV(){
        tvParts.setItems(getAllParts());
    }

    /**
     * Allows a user to delete a part if it is not used by one or more products
     * @param event Delete part button action
     */
    @FXML
    void handleDeleteParts(ActionEvent event) {
        Part part = tvParts.getSelectionModel().getSelectedItem();
        if(tvParts.getSelectionModel().getSelectedItem() == null){
            AlertBox.display("Error Deleting Part","You must select a part" +
                    " to delete first.");
        }
        else{
            boolean userChoice = ConfirmBox.display("Delete Part Confirmation","Are you sure" +
                    " you would like to delete " + part.getName() + "?");
            if(userChoice){
                deletePart(part);

                updatePartsTV();
                System.out.println("The part " + part.getName() + " has been removed.");
            }
            else{
                System.out.println("The part " + part.getName() + " has not been removed.");
            }

        }

    }

    //Methods for the Products section

    /**
     * Searches for a product
     * @param event Products search field action
     */
    @FXML
    void handleProductsSearch(ActionEvent event) {
        String productToSearch = tfProductsSearch.getText();
        boolean partFound = false;
        ObservableList<Product> searchResults = lookupProduct(productToSearch);

        try {
            if (searchResults.size() == 0) {
                Product tempProduct = Inventory.lookupProduct(Integer.parseInt(productToSearch));
                if (tempProduct != null) {
                    searchResults.add(tempProduct);
                    tvProducts.setItems(searchResults);
                } else {
                    AlertBox.display("Product Search Error", "Product not found.");
                }
            }

        } catch (NumberFormatException e) {
            AlertBox.display("Product Search Error", "Product not found.");
        }

        tvProducts.setItems(searchResults);
        tfProductsSearch.setText("");

    }

    /**
     * Launches a screen for adding products
     * @param event Add product button action
     * @throws IOException Thrown by FXMLLoader
     */
    @FXML
    void openAddProductsScreen(ActionEvent event) throws IOException{
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/AddProductView.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch(IOException e){

        }
    }

    /**
     * Launches a screen for modifying products
     * @param event Modify product button action
     * @throws IOException Thrown by FXMLLoader
     */
    @FXML
    void openModifyProductsScreen(ActionEvent event) throws IOException{
        if(tvProducts.getSelectionModel().getSelectedItem() == null){
            AlertBox.display("Error Opening Modify Products Screen","You must select a product" +
                    " to modify first.");
        }
        else {
            try {
                modifyProduct = tvProducts.getSelectionModel().getSelectedItem();
                modifyProductIndex = Inventory.getAllProducts().indexOf(modifyProduct);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ModifyProductView.fxml"));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {

            }
        }
    }

    /**
     *  Method for updating the products table view
     */
    public void updateProductsTV(){
        tvProducts.setItems(getAllProducts());
    }

    /**
     * Allows a user to delete a product as long as it does not contain one or more parts
     * @param event Delete product button action
     */
    @FXML
    void handleDeleteProducts(ActionEvent event) {
        Product product = tvProducts.getSelectionModel().getSelectedItem();
        if(tvProducts.getSelectionModel().getSelectedItem() == null){
            AlertBox.display("Error Deleting Product","You must select a product" +
                    " to delete first.");
        }
        else {
            if (Inventory.productDeleteValidation(product)) {
                AlertBox.display("Delete Product Error", "This product cannot be " +
                        "deleted as it contains a part(s).");
            } else {
                boolean userChoice = ConfirmBox.display("Delete Product Confirmation", "Are you sure" +
                        " you would like to delete " + product.getName() + "?");
                if (userChoice) {
                    Inventory.deleteProduct(product);
                    updateProductsTV();
                    System.out.println("The product " + product.getName() + " has been removed.");
                } else {
                    System.out.println("The part " + product.getName() + " has not been removed.");
                }

            }
        }
    }

    /**
     * Method that will allow the user to close the main program
     * @param event Exit button action
     */
    @FXML
    void handleMainScreenExit(ActionEvent event) {
        boolean result = ConfirmBox.display("Exit Confirmation","Are you sure you want to exit?");
        if(result){
            System.exit(0);
        }
    }

    /**
     * Initializes the TableView for the parts and products
     * @param url Points to any specified resource such as a file or link
     * @param resourceBundle Locale-specific data from the end user's side
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colPartsID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        colPartsName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        colPartsInven.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        colPartsPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        updatePartsTV();

        colProductsID.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        colProductsName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        colProductsInven.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        colProductsPrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        updateProductsTV();
    }

}
