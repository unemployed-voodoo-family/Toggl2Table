package UnemployedVoodooFamily.GUI.Content;

import UnemployedVoodooFamily.GUI.GUIHelper;
import UnemployedVoodooFamily.Logic.ProfileLogic;
import UnemployedVoodooFamily.Logic.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;

public class ProfileController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField countryField;

    @FXML
    private TextField timeZoneField;

    @FXML
    private TextField durationDispField;

    @FXML
    private TextField dateFormatField;

    @FXML
    private TextField timeFormatField;

    @FXML
    private TextField firstDayOfWeekField;

    @FXML private MenuItem editProfileBtn;


    public Node loadFXML() throws IOException {
        URL r = getClass().getClassLoader().getResource("Profile.fxml");
        return FXMLLoader.load(r);
    }

    public void initialize() {
        nameField.setText(Session.getInstance().getUser().getFullname());
        emailField.setText(Session.getInstance().getUser().getEmail());
        countryField.setText(Session.getInstance().getUser().getLanguage());
        timeZoneField.setText(Session.getInstance().getUser().getTimeZone());
        durationDispField.setText(Session.getInstance().getUser().getTimeofday_format());
        dateFormatField.setText(Session.getInstance().getUser().getDate_format());
        timeFormatField.setText(ProfileLogic.getTimeFormat(Session.getInstance().getUser().getTimeofday_format()));
        firstDayOfWeekField.setText(ProfileLogic.getFirstDayOfWeek(Session.getInstance().getUser().getBeginning_of_week()));
        editProfileBtn.setOnAction(e -> browseTogglProfile());

    }

    /**
     * Open profile section of Toggl website
     */
    public void browseTogglProfile() {
        GUIHelper.navigateToUrl("https://toggl.com/app/profile");
    }
}
