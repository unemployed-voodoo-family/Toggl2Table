package UnemployedVoodooFamily.GUI.Content;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Data.WorkHoursData;
import UnemployedVoodooFamily.Logic.SettingsLogic;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

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
    @FXML
    private Button deleteWhBtn;

    @FXML
    private VBox workHoursViewRoot;

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
        this.workHoursViewRoot.setVisible(false);
        toggleViewHoursList();
        setKeyAndClickListeners();

        // load success and error images
        URL successUrl = getClass().getClassLoader()
                                   .getResource("icons/baseline_check_circle_black_24dp.png");
        if(successUrl != null) {
            Image success = new Image(successUrl.toExternalForm());
            successImg = new ImageView(success);
        }
        successImg.setFitWidth(24);
        successImg.setFitHeight(24);
        URL errorUrl = getClass().getClassLoader()
                                 .getResource("icons/baseline_error_black_24dp.png");

        if(errorUrl != null) {
            Image error = new Image(errorUrl.toString());
            errorImg = new ImageView(error);
        }
        errorImg.setFitWidth(24);
        errorImg.setFitHeight(24);


        TableColumn<WorkHoursData, LocalDate> fromCol;
        TableColumn<WorkHoursData, LocalDate> toCol;
        TableColumn<WorkHoursData, Double> hoursCol;
        TableColumn<WorkHoursData, String> noteCol;

        fromCol = new TableColumn<>("From");
        toCol = new TableColumn<>("To");
        hoursCol = new TableColumn<>("Hours");
        noteCol = new TableColumn<>("Note");

        hoursView.getColumns().clear();
        hoursView.getColumns().addAll(fromCol, toCol, hoursCol, noteCol);
        System.out.println(fromCol.isEditable());

        fromCol.setCellValueFactory(param -> param.getValue().fromProperty());

        toCol.setCellValueFactory(param -> param.getValue().toProperty());
        hoursCol.setCellValueFactory(param -> param.getValue().hoursProperty());
        noteCol.setCellValueFactory(param -> param.getValue().noteProperty());

    }

    @SuppressWarnings("Duplicates")
    /**
     * Set key and click listeners
     */ private void setKeyAndClickListeners() {
        confirmHoursBtn.setOnAction(event -> trySetWorkHours());
        viewHoursBtn.setOnAction(event -> toggleViewHoursList());

        deleteWhBtn.setOnAction(event -> {
            logic.deleteWorkHours(hoursView.getSelectionModel().getSelectedItem());
            if(workHoursViewRoot.isVisible()) {
                logic.populateHoursTable(hoursView);
            }
        });
        deleteDataBtn.setOnAction(event -> {
            Thread fileDeleteThread = new Thread(() -> {
                try {
                    logic.deleteStoredData(FilePath.APP_HOME.getPath());

                    Platform.runLater(() -> {
                        showSuccessLabel(fileRemoveFeedbackLabel, "Locally stored files have been removed");
                    });
                }
                catch(Exception e) {
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
                if(! hoursField.getText()
                               .matches("[0-1]?[0-9](\\.[0-9][0-9]?)?|2[0-3](\\.[0-9][0-9]?)?|24(\\.[0][0]?)?")) {
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
        viewDataBtn.setOnAction(a -> {
            try {
                Desktop.getDesktop().open(new File(FilePath.APP_HOME.getPath()));
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            catch(IllegalArgumentException e) {
                //could not find path
            }
        });
    }

    /**
     * Toggle visibility of hours table
     */
    private void toggleViewHoursList() {
        if(workHoursViewRoot.isVisible()) {
            workHoursViewRoot.setVisible(false);
            viewHoursBtn.setText("View hours");
        }
        else {
            workHoursViewRoot.setVisible(true);
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
            logic.setWorkHours(hoursFromField.getValue(), hoursToField.getValue(), hoursField.getText(),
                               noteField.getText());

            if(workHoursViewRoot.isVisible()) {
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

    private void showWorkHoursInputSuccess() {
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

