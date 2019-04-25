package UnemployedVoodooFamily.GUI.Content;

import UnemployedVoodooFamily.Logic.ProfileLogic;
import UnemployedVoodooFamily.Logic.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.Locale;

import java.io.IOException;
import java.net.URL;

public class ProfileController {

    @FXML
    private Label nameField;

    @FXML
    private Label emailField;

    @FXML
    private Label countryField;

    @FXML
    private Label timeZoneField;

    @FXML
    private Label durationDispField;

    @FXML
    private Label dateFormatField;

    @FXML
    private Label timeFormatField;

    @FXML
    private Label firstDayOfWeekField;


    private ProfileLogic profile;


    public Node loadFXML() throws IOException {
        URL r = getClass().getClassLoader().getResource("Profile.fxml");
        return FXMLLoader.load(r);
    }

    public void initialize() {
        this.profile = new ProfileLogic();
        nameField.setText(Session.getInstance().getUser().getFullname());
        emailField.setText(Session.getInstance().getUser().getEmail());
        countryField.setText(Session.getInstance().getUser().getLanguage());
        timeZoneField.setText(Session.getInstance().getUser().getTimeZone());
        durationDispField.setText(Session.getInstance().getUser().getTimeofday_format());
        dateFormatField.setText(Session.getInstance().getUser().getDate_format());
        timeFormatField.setText(profile.getTimeFormat(Session.getInstance().getUser().getTimeofday_format()));
        firstDayOfWeekField.setText(profile.getFirstDayOfWeek(Session.getInstance().getUser().getBeginning_of_week()));

    }

}
