module com.example.miguelgarciasoftwareii {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.Controller to javafx.fxml;
    exports com.example.Controller;
    exports Model;
    opens Model to javafx.fxml;
}