package GUI.Content;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

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


    public Node loadFXML() throws IOException {
        URL r = getClass().getClassLoader().getResource("Settings.fxml");
        return FXMLLoader.load(r);
    }


}
