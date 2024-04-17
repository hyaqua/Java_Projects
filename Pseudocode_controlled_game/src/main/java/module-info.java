module com.example.lab_7 {
    requires javafx.controls;
    requires javafx.fxml;
    requires core;
    requires java.desktop;


    opens com.example.lab_7 to javafx.fxml;
    exports com.example.lab_7;
}