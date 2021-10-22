module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires mongo.java.driver;

    opens main to javafx.fxml;
    opens main.model to javafx.fxml;
    opens main.controller to javafx.fxml;

    exports main;
    exports main.controller;
    exports main.model;

}
