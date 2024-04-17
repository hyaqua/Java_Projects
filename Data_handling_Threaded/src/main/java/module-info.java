module com.example.lab_6 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lab_6 to javafx.fxml;
    exports com.example.lab_6;
}