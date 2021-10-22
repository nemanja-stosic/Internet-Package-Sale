package main.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.database.MongoDBUtil;
import main.model.InternetPackage;
import main.utils.AlertSelector;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private BorderPane borderPane;

    @FXML
    private VBox formVbox;

    @FXML
    private StackPane tableStackPane;

    @FXML
    private TextField firstNameTf;

    @FXML
    private TextField lastNameTf;

    @FXML
    private TextField addressTf;

    @FXML
    private ToggleButton speed2Tb;

    @FXML
    private ToggleGroup speedGroup;

    @FXML
    private ToggleButton speed5Tb;

    @FXML
    private ToggleButton speed10Tb;

    @FXML
    private ToggleButton speed20Tb;

    @FXML
    private ToggleButton speed50Tb;

    @FXML
    private ToggleButton speed100Tb;

    @FXML
    private ToggleGroup bandwidthGroup;

    @FXML
    private ToggleGroup durationGroup;

    @FXML
    private HBox buttonsHbox;

    @FXML
    private TableView<InternetPackage> tableView;

    @FXML
    private TableColumn<InternetPackage, String> firstNameColumn;

    @FXML
    private TableColumn<InternetPackage, String> lastNameColumn;

    @FXML
    private TableColumn<InternetPackage, String> addressColumn;

    @FXML
    private TableColumn<InternetPackage, Integer> speedColumn;

    @FXML
    private TableColumn<InternetPackage, String> bandwidthColumn;

    @FXML
    private TableColumn<InternetPackage, String> durationColumn;

    @FXML
    private Button closeButton;

    private ObservableList<InternetPackage> internetPackages = MongoDBUtil.getAllInternetPackages();
    private InternetPackage internetPackage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        internetPackage = new InternetPackage();

        //show data in table
        tableView.setItems(internetPackages);

        firstNameTf.textProperty().bindBidirectional(internetPackage.firstNameProperty());
        lastNameTf.textProperty().bindBidirectional(internetPackage.lastNameProperty());
        addressTf.textProperty().bindBidirectional(internetPackage.addressProperty());

        speedGroup.selectedToggleProperty().addListener(((observableValue, oldValue, newValue) -> {
            //if user deselect speed, return it to default property
            if (oldValue != null && newValue == null) {
                internetPackage.speedProperty().set(0);
            }

            if (newValue != null) {
                ToggleButton toggleButton = (ToggleButton) newValue;
                internetPackage.speedProperty().set(Integer.parseInt(toggleButton.getText()));
            }
        }));

        bandwidthGroup.selectedToggleProperty().addListener(((observableValue, oldValue, newValue) -> {
            //if user deselect bandwidth, return it to default property
            if (oldValue != null && newValue == null) {
                internetPackage.bandwidthProperty().set("");
            }

            if (newValue != null) {
                ToggleButton toggleButton = (ToggleButton) newValue;
                internetPackage.bandwidthProperty().set(toggleButton.getText());
            }
        }));

        durationGroup.selectedToggleProperty().addListener(((observableValue, oldValue, newValue) -> {
            //if user deselect duration, return it to default property
            if (oldValue != null && newValue == null) {
                internetPackage.durationProperty().set("");
            }

            if (newValue != null) {
                ToggleButton toggleButton = (ToggleButton) newValue;
                internetPackage.durationProperty().set(toggleButton.getText());
            }
        }));

        initColumns();
    }

    @FXML
    void handleClearFormButton(ActionEvent event) {
        firstNameTf.setText("");
        lastNameTf.setText("");
        addressTf.setText("");
        if (speedGroup.getSelectedToggle() != null) {
            speedGroup.getSelectedToggle().setSelected(false);
        }
        if (bandwidthGroup.getSelectedToggle() != null) {
            bandwidthGroup.getSelectedToggle().setSelected(false);
        }
        if (durationGroup.getSelectedToggle() != null) {
            durationGroup.getSelectedToggle().setSelected(false);
        }
    }

    @FXML
    void handleCloseButton(ActionEvent event) {
        JFXButton yesButton = new JFXButton("Yes");
        JFXButton noButton = new JFXButton("No");
        yesButton.setPrefSize(80, 30);
        yesButton.setTranslateX(-100);
        yesButton.setTranslateY(-20);
        noButton.setPrefSize(80, 30);
        noButton.setTranslateX(-150);
        noButton.setTranslateY(-20);
        yesButton.setOnAction(event2 -> Platform.exit());
        AlertSelector.jfxPopOutDialog(rootPane, borderPane, Arrays.asList(noButton, yesButton),
                "Are you sure want to exit app?", "");
    }

    @FXML
    void handleSavePackageButton(ActionEvent event) {
        //check if user is sure
        JFXButton yesButton = new JFXButton("Yes");
        JFXButton noButton = new JFXButton("No");
        yesButton.setPrefSize(80, 30);
        yesButton.setTranslateX(-100);
        yesButton.setTranslateY(-20);
        noButton.setPrefSize(80, 30);
        noButton.setTranslateX(-150);
        noButton.setTranslateY(-20);
        yesButton.setOnAction(event2 -> {
            if (internetPackage.isValid()) {

                MongoDBUtil.insertInternetPackage(new InternetPackage(internetPackage.getFirstName(), internetPackage.getLastName(),
                        internetPackage.getAddress(), internetPackage.getSpeed(), internetPackage.getBandwidth(),
                        internetPackage.getDuration()));

                refreshTable();
            } else {
                StringBuilder errMsg = new StringBuilder();

                ArrayList<String> errList = internetPackage.errorsProperty().get();

                for (String errList1 : errList) {
                    errMsg.append(errList1).append(" ");
                }

                AlertSelector.errorAlert("Empty fields", errMsg.toString(), "");
                errList.clear();
            }
        });
        AlertSelector.jfxPopOutDialog(rootPane, borderPane, Arrays.asList(noButton, yesButton),
                "Are you want to sign this contract?", "");
    }

    @FXML
    void handleDeleteTableRowButton(ActionEvent event) {
        InternetPackage selectedInternetPackage = tableView.getSelectionModel().getSelectedItem();

        if (selectedInternetPackage == null) {
            AlertSelector.errorAlert("No Row Selected", null, "Please select a user you want to delete first!");
            return;
        }

        JFXButton yesButton = new JFXButton("Yes");
        JFXButton noButton = new JFXButton("No");
        yesButton.setPrefSize(80, 30);
        yesButton.setTranslateX(-100);
        yesButton.setTranslateY(-20);
        noButton.setPrefSize(80, 30);
        noButton.setTranslateX(-150);
        noButton.setTranslateY(-20);
        yesButton.setOnAction(event2 -> {
            //getting variable name from class
            Class internetPackageClass = InternetPackage.class;
            Field[] fields = internetPackageClass.getDeclaredFields();

            String fieldFirstName = fields[0].getName();
            String firstName = selectedInternetPackage.getFirstName();

            MongoDBUtil.delete(fieldFirstName, firstName);

            internetPackages.remove(selectedInternetPackage);

            refreshTable();
        });
        AlertSelector.jfxPopOutDialog(rootPane, borderPane, Arrays.asList(noButton, yesButton), "Are you sure want to delete this contract with this name: " + selectedInternetPackage.getFirstName() + " ?", "");
    }

    private void initColumns() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        speedColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));
        bandwidthColumn.setCellValueFactory(new PropertyValueFactory<>("bandwidth"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        //forcing tableView to use MAX WIDTH for nice column size
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void refreshTable() {
        internetPackages = MongoDBUtil.getAllInternetPackages();
        tableView.setItems(internetPackages);
    }

}
