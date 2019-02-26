package UnemployedVoodooFamily.GUI.Content;

import UnemployedVoodooFamily.Logic.SettingsLogic;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

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
    TableView hoursView;

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
        this.logic = new SettingsLogic();
        initilizeFields();
        hoursView.setVisible(false);
        setKeyAndClickListeners();
    }

    private void initilizeFields() {
        hoursField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,9}([\\.]\\d{0,9})?")) {
                hoursField.setText(oldValue);
            }
        });
    }

    /**
     * Set key and click listeners
     */
    private void setKeyAndClickListeners() {
        confirmHoursBtn.setOnAction(event -> trySetWorkHours());
        viewHoursBtn.setOnAction(event -> toggleViewHoursList());
    }

    /**
     * Toggle visibility of hours table
     */
    private void toggleViewHoursList() {
        if(hoursView.isVisible()) {
            hoursView.setVisible(false);
            viewHoursBtn.setText("View hours");
        } else {
            hoursView.setVisible(true);
            viewHoursBtn.setText("Hide hours");
            populateHoursList();
        }
    }

    /**
     * WIP
     */
    private void populateHoursList() {
        URL resourceUrl = getClass().getResource("/Settings/hours.properties");
        File filename = null;
        try {
            filename = new File(resourceUrl.toURI().getPath());
        }
        catch(URISyntaxException e) {
            e.printStackTrace();
        }
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(filename));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets work hours if fields are not empty
     */
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
}

