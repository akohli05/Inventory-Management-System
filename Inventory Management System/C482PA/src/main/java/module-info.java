module com.example.c482pa {
    requires javafx.controls;
    requires javafx.fxml;
    opens com.example.c482pa.main to javafx.graphics;
    opens com.example.c482pa.controllers to javafx.fxml;
    opens com.example.c482pa.model to javafx.base;
}