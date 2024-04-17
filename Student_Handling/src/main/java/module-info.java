module com.example.studentai {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires itextpdf;
    opens com.example.studentai to javafx.fxml;
    exports com.example.studentai;
}