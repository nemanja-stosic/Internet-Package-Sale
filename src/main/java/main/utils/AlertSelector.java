package main.utils;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;

import java.util.List;
import java.util.Objects;

public class AlertSelector {

    private static boolean answer;

    public static void errorAlert(String title, String headerText, String contentText) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(title);
        error.setHeaderText(headerText);
        error.setContentText(contentText);
        error.showAndWait();
    }

    public static boolean confirmationAlert(String title, String message) {

        Stage confirmBoxWindow = new Stage();

        confirmBoxWindow.initModality(Modality.APPLICATION_MODAL);
        confirmBoxWindow.setTitle(title);
        confirmBoxWindow.setMinWidth(350);
        confirmBoxWindow.setMinHeight(170);

        Label label = new Label(message);
        label.setFont(new Font("Euphemia", 17));
        label.setStyle("-fx-text-fill: #FFFFFF");

        //Creating buttons
        Button yesButton = new Button("Yes");
        yesButton.setMinWidth(70);
        yesButton.setMinHeight(20);

        final String BUTTON_STYLE = "-fx-background-color: #1E88E5; -fx-background-radius: 10";
        final String HOVER_YES_BUTTON_STYLE = "-fx-background-color: #4CAF50; -fx-background-radius: 10";
        yesButton.setStyle(BUTTON_STYLE);
        yesButton.setOnMouseEntered(event -> yesButton.setStyle(HOVER_YES_BUTTON_STYLE));
        yesButton.setOnMouseExited(event -> yesButton.setStyle(BUTTON_STYLE));

        Button noButton = new Button("No");
        noButton.setMinWidth(70);
        noButton.setMinHeight(20);

        final String HOVER_NO_BUTTON_STYLE = "-fx-background-color: #d32f2f; -fx-background-radius: 10";
        noButton.setStyle(BUTTON_STYLE);
        noButton.setOnMouseEntered(event -> noButton.setStyle(HOVER_NO_BUTTON_STYLE));
        noButton.setOnMouseExited(event -> noButton.setStyle(BUTTON_STYLE));

        yesButton.setOnAction(e -> {
            answer = true;
            confirmBoxWindow.close();
        });

        noButton.setOnAction(e -> {
            answer = false;
            confirmBoxWindow.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label);
        layout.setAlignment(Pos.CENTER);

        HBox layout2 = new HBox();
        layout2.getChildren().addAll(yesButton, noButton);
        layout2.setSpacing(20);
        layout2.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(layout);
        borderPane.setCenter(layout2);
        borderPane.setStyle("-fx-background-color: #455A64;");

        Scene scene = new Scene(borderPane);
        confirmBoxWindow.setScene(scene);
        confirmBoxWindow.setResizable(false);
        confirmBoxWindow.showAndWait();

        return answer;
    }

    public static void jfxPopOutDialog(StackPane rootPane, Node nodeToBeBlured, List<JFXButton> controls, String heading, String body) {
        BoxBlur blur = new BoxBlur(3, 3, 3); //width, height, iterations(intensity of blurs i think)

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

        controls.forEach(controlButton -> {
            controlButton.getStyleClass().add("dialog-button");
            controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> dialog.close());
        });

        Label headingLabel = new Label(heading);
        headingLabel.getStyleClass().add("jfx-label-color-black");
        dialogLayout.setHeading(headingLabel);
        Label bodyLabel = new Label(body);
        bodyLabel.getStyleClass().add("jfx-label-color-black");
        dialogLayout.setBody(bodyLabel);
        dialogLayout.setActions(controls); //sets button on layout
        dialogLayout.getStylesheets().add(Objects.requireNonNull(AlertSelector.class.getResource("/main/css/style.css")).toExternalForm());
        dialogLayout.getStyleClass().add("style.css");
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent event2) -> nodeToBeBlured.setEffect(null));

        nodeToBeBlured.setEffect(blur);
    }

}
