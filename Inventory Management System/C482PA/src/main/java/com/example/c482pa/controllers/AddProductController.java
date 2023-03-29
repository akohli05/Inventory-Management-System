package com.example.c482pa.controllers;

import com.example.c482pa.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.c482pa.model.Inventory.lookupPart;

/**
 * This class focuses on the add product screen.
 * It includes methods for adding a product, canceling the addition
 * of a new product, validating the product, and initializing the
 * table views.
 *
 * @author Aimy Kohli
 */

public class AddProductController implements Initializable {

    @FXML
    private TextField tfPartSearch;

    @FXML
    private TableView<Part> tvParts;

    @FXML
    private TableColumn<Part, Integer> colAllPartID;

    @FXML
    private TableColumn<Part, String> colAllPartName;

    @FXML
    private TableColumn<Part, Integer> colAllPartInv;

    @FXML
    private TableColumn<Part, Double> colAllPartPrice;

    @FXML
    private TableView<Part> tvRelatedParts;

    @FXML
    private TableColumn<Part, Integer> colAssocPartID;

    @FXML
    private TableColumn<Part, String> colAssocPartName;

    @FXML
    private TableColumn<Part, Integer> colAssocPartInv;

    @FXML
    private TableColumn<Part, Double> colAssocPartPrice;

    @FXML
    private Label lblID;

    @FXML
    private TextField tfID;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfInv;

    @FXML
    private TextField tfPrice;

    @FXML
    private TextField tfMin;

    @FXML
    private TextField tfMax;

    //Additional variables
    private  int productID;
    private static ObservableList<Part> allCurrentParts = FXCollections.observableArrayList();

    /**
     * Searches for a specific part
     * @param event Search field action
     */
    @FXML
    void handlePartSearch(ActionEvent event) {
        String partToSearch = tfPartSearch.getText();
        boolean partFound = false;
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
        tfPartSearch.setText("");
    }

    /**
     * Will add the selected part into the table view
     * for parts associated with a product
     * @param event Add button action
     */
    @FXML
    void addPart(ActionEvent event) {
        Part part = tvParts.getSelectionModel().getSelectedItem();
        if(part == null){
            AlertBox.display("Add Part Error", "You must select a part to add first.");
        }

        tvRelatedParts.getItems().add(part);

    }

    /**
     * Once the user wants to save a product, this method will cover the following mentioned below.
     * Get the TextField value entered in.
     * Convert the value to a String and pass it into another method for validation.
     * If no error message is displayed, then the product will be added
     * with any related parts if included.
     * The user will then be redirected back to main screen.
     * @param event Save button action
     * @throws IOException Thrown by the FXMLLoader
     */
    @FXML
    void handleSaveProduct(ActionEvent event) throws IOException{
        String productName = tfName.getText();
        String productInven = tfInv.getText();
        String productPrice = tfPrice.getText();
        String productMin = tfMin.getText();
        String productMax = tfMax.getText();

        try{
            String errorMessage = new String();
            errorMessage = productValidation(productName, Integer.parseInt(productInven), Double.parseDouble(productPrice),
                    Integer.parseInt(productMin), Integer.parseInt(productMax), errorMessage);

            if(errorMessage.length() > 0){
                AlertBox.display("Error Adding Product", errorMessage);
                errorMessage = "";
            }
            else {
                Product product = new Product(productID,productName,
                        Double.parseDouble(productPrice), Integer.parseInt(productInven),
                        Integer.parseInt(productMin), Integer.parseInt(productMax));

                for(Part part: tvRelatedParts.getItems()) {
                    product.addAssociatedPart(part);
                }

                Inventory.addProduct(product);

                Parent root = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
                Scene scene = new Scene(root);
                Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                primaryStage.setScene(scene);
                primaryStage.show();
                }

            } catch (Exception e){
            AlertBox.display("Error Adding Part", "The form contains empty fields" +
                    " or invalid data. Please make sure that each field includes the appropriate values.\n" +
                    "1. Name - valid name\n" +
                    "2. Inv - integers only\n" +
                    "3. Price/Cost - decimal or integer excluding dollar sign\n" +
                    "4. Max - integers only\n" +
                    "5. Min - integers only\n");
        }

    }

    /**
     * This method will take in all Textfield values and validate them.
     * If any of these variables contains incorrect data, then an error message is returned.
     * @param productName The product name
     * @param inven The stock level
     * @param price The price of the product
     * @param min The min stock level
     * @param max The max stock level
     * @param eMessage The error message
     * @return The error message
     */
    public static String productValidation(String productName, int inven, double price, int min, int max, String eMessage ){

        if(productName.isEmpty()){
            eMessage = eMessage + "The part name is a required field and cannot be empty. \n";
        }

        if(inven < 1){
            eMessage = eMessage + "The inventory count cannot be a number less than 1. \n";
        }

        if(price <= 0){
            eMessage = eMessage + "The price field must be more than $0. \n";
        }

        if(max < min){
            eMessage = eMessage + "The max value should be greater than or equal to the min value. \n";
        }

        if(inven > max || inven < min){
            eMessage = eMessage + "The inventory value should be between the max and min values. \n";
        }

        return eMessage;
    }

    /**
     * This method removes any selected parts relating to the product.
     * A confirm box is displayed. If the user confirms the deletion,
     * the selected part is removed. If not, then it will not be removed.
     * @param event Remove Associated Part button
     */
    @FXML
    void handleDeletePart(ActionEvent event) {
        Part part = tvRelatedParts.getSelectionModel().getSelectedItem();

        if(part == null){
            AlertBox.display("Remove Part Error", "You must select a part to remove first.");
        }
        else {
            boolean userChoice = ConfirmBox.display("Delete Part Confirmation", "Are you sure" +
                    " you would like to delete " + part.getName() + "?");
            if (userChoice) {
                tvRelatedParts.getItems().remove(part);
                System.out.println("The part " + part.getName() + " has been removed.");
            } else {
                System.out.println("The part " + part.getName() + " has not been removed.");
            }
        }

    }

    /**
     * If the user clicks the cancel button, a confirmation box is displayed. If the user
     * confirms the cancellation, then they will be redirected to the main screen and the product
     * will not be added. If the user does not confirm the cancellation,
     * they will remain on the same screen.
     * @param event Cancel button action
     * @throws IOException Thrown by the FXMLLoader
     */
    @FXML
    void handleCancelProduct(ActionEvent event) throws IOException {
        boolean userChoice = ConfirmBox.display("Confirm Cancellation", "Are you sure you would like to cancel " +
                "adding a new product?");

        if(userChoice){
            Parent root = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        else {
            System.out.println("The cancel button was clicked.");
        }
    }

    /**
     * The table views and the product id field are initialized.
     * @param url Points to any specified resource such as a file or link
     * @param resourceBundle Locale-specific data from the end user's side
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colAllPartID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        colAllPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        colAllPartInv.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        colAllPartPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        tvParts.setItems(Inventory.getAllParts());

        colAssocPartID.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        colAssocPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        colAssocPartInv.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        colAssocPartPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        productID = Inventory.productInvenSize();
        lblID.setText("ID: " + productID);
    }
}
