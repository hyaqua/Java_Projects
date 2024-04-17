module com.example.skaiciuotuvas {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.skaiciuotuvas to javafx.fxml;
    opens data to javafx.base;
    exports data;
    exports com.example.skaiciuotuvas;
}