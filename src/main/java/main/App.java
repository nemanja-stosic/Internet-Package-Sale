package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {

    private double x, y;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("");
        stage.setScene(loadScene(loadPane(), stage));
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    private Pane loadPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        return loader.load(getClass().getResourceAsStream("view/MainStage.fxml"));

    }

    private Scene loadScene(Pane pane, Stage stage) {
        Scene scene = new Scene(pane);
        scene.getStylesheets().setAll(Objects.requireNonNull(getClass().getResource("css/style.css")).toExternalForm());

        scene.setOnMousePressed(event -> {
            x = event.getScreenX() - stage.getX();
            y = event.getScreenY() - stage.getY();
        });

        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

        return scene;
    }

    public static void main(String[] args) {
        launch();
    }

}