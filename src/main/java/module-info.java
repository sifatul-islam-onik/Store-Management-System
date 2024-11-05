module com.example.storemanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires org.apache.pdfbox;

    opens com.example.storemanagementsystem to javafx.fxml;
    exports com.example.storemanagementsystem;
    exports com.example.storemanagementsystem.Controllers;
    opens com.example.storemanagementsystem.Controllers to javafx.fxml;
    exports com.example.storemanagementsystem.Utilities;
    opens com.example.storemanagementsystem.Utilities to javafx.fxml;
    exports com.example.storemanagementsystem.Models;
    opens com.example.storemanagementsystem.Models to javafx.fxml;
}