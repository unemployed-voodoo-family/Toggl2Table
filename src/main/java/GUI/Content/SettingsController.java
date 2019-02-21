package GUI.Content;

import GUI.DateRange;
import Logic.SettingsLogic;
import com.sun.scenario.Settings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private SettingsLogic logic;

    public Node loadFXML() throws IOException {
        URL r = getClass().getClassLoader().getResource("Settings.fxml");
        return FXMLLoader.load(r);
    }

    public void initialize() {
        this.logic = new SettingsLogic();
        initilizeFields();
        setKeyAndClickListeners();
    }

    private void initilizeFields() {
        hoursField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,9}([\\.]\\d{0,9})?")) {
                hoursField.setText(oldValue);
            }
        });
    }

    private void setKeyAndClickListeners() {

        confirmHoursBtn.setOnAction(event -> trySetWorkHours());
    }

    private void trySetWorkHours() {
        try {
            boolean success = true;
            if(hoursFromField.getValue() == null) {
                success = false;
                //set error message
            }
            if(hoursToField.getValue() == null) {
                success = false;
                //set error message
            }
            if(hoursField.getText() == null) {
                success = false;
                //set error message
            }
            if(success) {
                logic.setWorkHours(hoursFromField.getValue(), hoursToField.getValue(), hoursField.getText());
            }
        }
        catch(URISyntaxException e) {
            //path not found
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    class NumberField extends TextField {
        @Override
        public void replaceText(int start, int end, String text) {
            if(text.matches("[0-9]*")) {
                super.replaceText(start, end, text);
            }
        }

        @Override
        public void replaceSelection(String text) {
            if(text.matches("[0-9]*")) {
                super.replaceSelection(text);
            }
        }

        ;
    }
}

