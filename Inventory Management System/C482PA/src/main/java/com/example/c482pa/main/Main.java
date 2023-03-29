package com.example.c482pa.main;

//Imports
import com.example.c482pa.controllers.ConfirmBox;
import com.example.c482pa.model.InHouse;
import com.example.c482pa.model.Inventory;
import com.example.c482pa.model.OutSourced;
import com.example.c482pa.model.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *  <h3>Inventory Management System Main Class </h3>
 *
 *  <h5>FUTURE ENHANCEMENTS:</h5>
 *   <ol>
 *       <li>Add a database to store the data provided by the Parts, Products, and Inventory. It is
 *       best use databases for CRUD applications since it can enhance security and allow for
 *       better data management.</li>
 *       <li>Add more appealing colors and designs to application. It is good to avoid bright
 *       colors and too many images in applications. However, adding some more styling and colors
 *       to the applications allows for a better user experience.</li>
 *   </ol>
 *
 *   <p>The JavaDoc files are located in the following path:
 *   <b>C482PA\src\main\java\com\example\c482pa\javadoc</b></p>
 *
 * @author Aimy Kohli
 *
 */
public class Main extends Application {

    /**
     * The start() method from the Application class
     * @param primaryStage The main stage
     * @throws Exception Required exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/views/MainView.fxml"))));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.show();

        //Confirmation box will be displayed if a user wants to exit the application
        primaryStage.setOnCloseRequest( evt -> {
            boolean userChoice = ConfirmBox.display("Confirm Exit", "Are you sure you want to exit" +
                    " the application?");

            //If the user clicks the 'Yes' button
            if(userChoice){
                System.exit(0);
            }
            //If the user clicks the 'No' button
            else {
                evt.consume();
            }
        });
    }

    /**
     * Populate the tables with test data for testing
     */
    public static void populateTableTestData(){
        //Outsourced parts test data
        Inventory.addPart(new OutSourced(Inventory.partInvenSize(), "Red Part", 20.00, 5, 1, 5, "Red Co." ));
        Inventory.addPart(new OutSourced(Inventory.partInvenSize(), "Blue Part", 25.00, 8, 1, 8, "Blue Co." ));
        Inventory.addPart(new OutSourced(Inventory.partInvenSize(), "Purple Part", 30.00, 10, 1, 10, "Purple Co." ));

        //InHouse parts test data
        Inventory.addPart(new InHouse(Inventory.partInvenSize(), "Orange ", 10.00, 3, 1, 3, 11111));
        Inventory.addPart(new InHouse(Inventory.partInvenSize(), "Green Part", 13.90, 5, 1, 5, 22222));
        Inventory.addPart(new InHouse(Inventory.partInvenSize(), "Yellow Part", 15.80, 6, 1, 6, 33333));

        //Products test data
        Inventory.addProduct(new Product(Inventory.productInvenSize(), "A Product", 40.50, 5, 1, 5));
        Inventory.addProduct(new Product(Inventory.productInvenSize(), "The Product", 4.50, 3, 1, 3));

        //Match the related parts and their products
        Inventory.getAllProducts().get(0).addAssociatedPart(Inventory.lookupPart(1));
        Inventory.getAllProducts().get(0).addAssociatedPart(Inventory.lookupPart(2));
        Inventory.getAllProducts().get(1).addAssociatedPart(Inventory.lookupPart(3));

    }

    /**
     *
     * @param args CMD arguments
     */
    public static void main(String[] args) {
        //populateTableTestData();
        launch();
    }
}
