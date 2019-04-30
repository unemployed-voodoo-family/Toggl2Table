package UnemployedVoodooFamily.GUI.Content;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Logic.SettingsLogic;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SettingsController {

    @FXML
    private Button viewHoursBtn;
    @FXML
    private Button confirmHoursBtn;
    @FXML
    private DatePicker hoursFromField;
    @FXML
    private DatePicker hoursToField;
    @FXML
    private Button viewDataBtn;
    @FXML
    private Button dataPathBtn;
    @FXML
    private TextField dataPathField;
    @FXML
    private Button logoutBtn;
    @FXML
    private Pane contentRoot;
    @FXML
    private TextField hoursField;
    @FXML
    private TextField noteField;
    @FXML
    private TableView hoursView;
    @FXML
    private Label inputFeedbackLabel;

    @FXML
    private Label fileRemoveFeedbackLabel;

    @FXML
    private Tooltip errorTooltip;

    @FXML
    private Button deleteDataBtn;

    private ImageView successImg;
    private ImageView errorImg;

    private SettingsLogic logic;

    /**
     * Load and return the root node of Settings.fxml
     * @return the root node of Settings.fxml
     * @throws IOException
     */
    public Node loadFXML() throws IOException {
        URL r = getClass().getClassLoader().getResource("Settings.fxml");
        return FXMLLoader.load(r);
    }

    public void initialize() {
        this.logic = new SettingsLogic(FilePath.getCurrentUserWorkhours());
        this.hoursView.setVisible(false);
        toggleViewHoursList();
        setKeyAndClickListeners();

        // load success and error images
        URL successUrl = getClass().getClassLoader()
                                   .getResource("icons" + File.separator + "baseline_check_circle_black_24dp.png");
        Image success = new Image(successUrl.toString());
        successImg = new ImageView(success);
        successImg.setFitWidth(24);
        successImg.setFitHeight(24);
        URL errorUrl = getClass().getClassLoader()
                                 .getResource("icons" + File.separator + "baseline_error_black_24dp.png");
        Image error = new Image(errorUrl.toString());
        errorImg = new ImageView(error);
        errorImg.setFitWidth(24);
        errorImg.setFitHeight(24);
    }

    @SuppressWarnings("Duplicates")
    /**
     * Set key and click listeners
     */ private void setKeyAndClickListeners() {
        confirmHoursBtn.setOnAction(event -> trySetWorkHours());
        viewHoursBtn.setOnAction(event -> toggleViewHoursList());
        deleteDataBtn.setOnAction(event -> {
            Thread fileDeleteThread = new Thread(() -> {
                try{
                    logic.deleteStoredData(FilePath.APP_HOME.getPath());

                    Platform.runLater(() -> {
                        showSuccessLabel(fileRemoveFeedbackLabel, "Locally stored files have been removed");
                    });
                }
                catch(Exception e){
                    Platform.runLater(() -> {
                        showErrorLabel(fileRemoveFeedbackLabel, "Error deleting files");
                        errorTooltip.setText(e.getMessage());
                        errorTooltip.setOpacity(.9);
                    });
                }
                });
            fileDeleteThread.start();
        });

        hoursField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            hoursField.getStyleClass().remove("error");
            if(! newValue) {
                if(! hoursField.getText().matches("[0-1]?[0-9](\\.[0-9][0-9]?)?|2[0-3](\\.[0-9][0-9]?)?|24(\\.[0][0]?)?")) {
                    hoursField.setText("");
                    hoursField.getStyleClass().add("error");
                }
            }
        });

        hoursFromField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                hoursFromField.getStyleClass().remove("error");
            }
            else if(hoursFromField.getValue() == null) {
                hoursFromField.getStyleClass().add("error");
            }
            else if(oldValue) {
                hoursFromField.getStyleClass().remove("error");
            }
        });

        hoursToField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                hoursToField.getStyleClass().remove("error");
            }
            else if(hoursToField.getValue() == null) {
                hoursToField.getStyleClass().add("error");
            }
            else if(oldValue) {
                hoursToField.getStyleClass().remove("error");
            }
        });

        //deleteDataBtn.getStyleClass().add("delete");
    }

    /**
     * Toggle visibility of hours table
     */
    private void toggleViewHoursList() {
        if(hoursView.isVisible()) {
            hoursView.setVisible(false);
            viewHoursBtn.setText("View hours");
        }
        else {
            hoursView.setVisible(true);
            viewHoursBtn.setText("Hide hours");
            populateHoursList();
        }
    }

    /**
     * WIP
     */
    private void populateHoursList() {
        logic.populateHoursTable(hoursView);
    }

    /**
     * Sets work hours if fields are not empty
     */
    private void trySetWorkHours() {
        boolean success = false;
        if(hoursFromField.getValue() == null) {
            hoursFromField.getStyleClass().add("error");
            showWorkHourInputErrorMessage("From date not specified");
        }
        else if(hoursToField.getValue() == null) {
            hoursToField.getStyleClass().add("error");
            showWorkHourInputErrorMessage("To date not specified");
        }
        else if(hoursField.getText().equals("")) {
            hoursField.getStyleClass().add("error");
            showWorkHourInputErrorMessage("Amount of work hours not specified");
        }
        else if(hoursFromField.getValue().isAfter(hoursToField.getValue())) {
            hoursFromField.getStyleClass().add("error");
            hoursToField.getStyleClass().add("error");
            showWorkHourInputErrorMessage("\"From\" date cannot come before \"to\" date");
        }
        else {
            success = true;
        }
        if(success) {
            logic.setWorkHours(hoursFromField.getValue(), hoursToField.getValue(), hoursField.getText(), noteField.getText());

            if(hoursView.isVisible()) {
                logic.populateHoursTable(hoursView);
            }

            clearWorkHourInputFields();
            showWorkHoursInputSuccess();
        }

    }

    private void showWorkHourInputErrorMessage(String errorMessage) {
        inputFeedbackLabel.getStyleClass().remove("success");
        inputFeedbackLabel.getStyleClass().add("error");
        inputFeedbackLabel.setText(errorMessage);
    }

    private void showWorkHoursInputSuccess()    {
        inputFeedbackLabel.getStyleClass().remove("error");
        inputFeedbackLabel.getStyleClass().add("success");
        inputFeedbackLabel.setText("Work hours added");
    }

    private void clearWorkHourInputFields() {
        hoursFromField.setValue(null);
        hoursFromField.getStyleClass().remove("error");
        hoursToField.setValue(null);
        hoursToField.getStyleClass().remove("error");
        hoursField.setText("");
        hoursField.getStyleClass().remove("error");
        noteField.setText("");
        noteField.getStyleClass().remove("error");
        inputFeedbackLabel.setText("");
        inputFeedbackLabel.getStyleClass().remove("error");
    }

    private void showSuccessLabel(Label label, String message) {
        label.setVisible(true);
        label.setGraphic(successImg);
        label.setText(message);
        label.getStyleClass().remove("error");
        label.getStyleClass().add("success");
    }


    private void showErrorLabel(Label label, String message) {
        label.setVisible(true);
        label.setGraphic(errorImg);
        label.setText(message);
        label.getStyleClass().remove("success");
        label.getStyleClass().add("error");
    }
}

