module com.example.chatter {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.chatter to javafx.fxml;
    exports com.example.chatter;
    opens com.example.client to javafx.fxml;
    exports com.example.client;
}
