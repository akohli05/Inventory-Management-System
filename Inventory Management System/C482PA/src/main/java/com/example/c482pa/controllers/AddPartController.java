package com.example.c482pa.controllers;

import com.example.c482pa.model.InHouse;
import com.example.c482pa.model.Inventory;
import com.example.c482pa.model.OutSourced;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class focuses on the add part screen.
 * It includes methods for adding a part, canceling the addition
 * of a new part, validating the part, and generating the
 * part id.
 *
 * @author Aimy Kohli
 */

public class AddPartController implements Initializable {
    @FXML
    private ToggleGroup tgroup;

    @FXML
    private RadioButton btnInHouse;

    @FXML
    private RadioButton btnOutsourced;

    @FXML
    private Label lblPartID;

    @FXML
    private Label lblPartDynamic;

    @FXML
    private TextField tfInv;

    @FXML
    private TextField tfPartDynamic;

    @FXML
    private TextField tfPartID;

    @FXML
    private TextField tfPartMax;

    @FXML
    private TextField tfPartMin;

    @FXML
    private TextField tfPartName;

    @FXML
    private TextField tfPartPrice;

    @FXML
    private Button btnSavePart;

    @FXML
    private Button btnCancelPart;

    private int partID;

    /**
     * Method that will change a label to Machine ID if
     * the in house radio button is picked
     * @param event Radio button action
     */
    @FXML
    void radioAddInHousePart(ActionEvent event) {
        lblPartDynamic.setText("Machine ID");
    }

    /**
     * Method that will change a label to Company Name if
     * the outsourced radio button is picked
     * @param event Radio button action
     */
    @FXML
    void radioAddOutsourcedPart(ActionEvent event) {
        lblPartDynamic.setText("Company Name");
    }

    /**
     * Once the user wants to save a part, this method will cover the following mentioned below.
     * Get the TextField value entered in.
     * Convert the value to a String and pass it into another method for validation.
     * If no error message is displayed, then the part will added as an inhouse or
     * outsourced part depending on which radio button is selected.
     * The user will then be redirected back to main screen.
     * @param event Save button action
     * @throws IOException Thrown by FXMLLoader
     */
    @FXML
    void handleSavePart(ActionEvent event) throws IOException{
        String partName = tfPartName.getText();
        String partInven = tfInv.getText();
        String partPrice = tfPartPrice.getText();
        String partMin = tfPartMin.getText();
        String partMax = tfPartMax.getText();
        String partDynamic = tfPartDynamic.getText();

        try{
            String errorMessage = new String();

            errorMessage = partValidation(partName, Integer.parseInt(partInven), Double.parseDouble(partPrice),
                    Integer.parseInt(partMin), Integer.parseInt(partMax), errorMessage);

            if(errorMessage.length() > 0 ){
                AlertBox.display("Error Adding Part", errorMessage);
                errorMessage = "";
            }

            else {
                if(btnInHouse.isSelected()){
                    InHouse inHousePart = new InHouse(partID,partName,
                            Double.parseDouble(partPrice), Integer.parseInt(partInven),
                            Integer.parseInt(partMin), Integer.parseInt(partMax),
                            Integer.parseInt(partDynamic));

                    Inventory.addPart(inHousePart);
                }
                else {
                    OutSourced outSourcedPart = new OutSourced(partID,partName,
                            Double.parseDouble(partPrice), Integer.parseInt(partInven),
                            Integer.parseInt(partMin), Integer.parseInt(partMax),
                            partDynamic);

                    Inventory.addPart(outSourcedPart);
                }
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e){
            AlertBox.display("Error Adding Part", "The form contains empty fields" +
                    " or invalid data. Please make sure that each field includes the appropriate values.\n" +
                    "1. Name - valid name\n" +
                    "2. Inv - integers only\n" +
                    "3. Price/Cost - decimal or integer excluding dollar sign\n" +
                    "4. Max - integers only\n" +
                    "5. Min - integers only\n" +
                    "6. Machine ID(if applicable) - integers only\n" +
                    "7. Company Name(if applicable) - valid company name\n");
        }

    }

    /**
     * This method will take in all Textfield values and validate them.
     * If any of these variables contains incorrect data, then an error message is returned.
     * @param partName The part name
     * @param inven The stock level
     * @param price The price of the part
     * @param min The min stock level
     * @param max The max stock level
     * @param eMessage The error message
     * @return The error message
     */
    public static String partValidation(String partName, int inven, double price, int min, int max, String eMessage ){

        //Check if name field is empty
        if(partName.isEmpty()){
            eMessage = eMessage + "The part name is a required field and cannot be empty. \n";
        }

        //Other validation
        if(inven < 1){
            eMessage = eMessage + "The inventory count cannot be a number less than 1. \n";
        }

        if(price <= 0){
            eMessage = eMessage + "The price field is a required field and must be more than $0. \n";
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
     * If the user clicks the cancel button, a confirmation box is displayed. If the user
     * confirms the cancellation, then they will be redirected to the main screen and the part
     * will not be added. If the user does not confirm the cancellation,
     * they will remain on the same screen.
     * @param event Cancel button action
     * @throws IOException Thrown by FXMLLoader
     */
    @FXML
    void handleCancelPart(ActionEvent event) throws IOException {
        boolean userChoice = ConfirmBox.display("Confirm Cancellation", "Are you sure you would like to cancel " +
                "adding a new part?");

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
     * This method will auto generate the part ID field.
     * @param url Points to any specified resource such as a file or link
     * @param resourceBundle Locale-specific data from the end user's side
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partID = Inventory.partInvenSize();
        lblPartID.setText("ID: " + partID);
    }


}
