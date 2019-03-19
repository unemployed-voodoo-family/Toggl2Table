package UnemployedVoodooFamily.GUI;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Logic.LoginLogic;
import UnemployedVoodooFamily.Logic.PropertiesLogic;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;

import java.util.Properties;


public class LoginController {

    @FXML
    private Button submitBtn;
    @FXML
    private ImageView bufferImg;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox RememberPasswordCheck;
    @FXML
    private Label wrongCredentials;

    private LoginLogic loginLogic = new LoginLogic();
    private boolean isLoggedIn;
    private boolean loginInProgress;
    private PropertiesLogic propertiesLogic = new PropertiesLogic();

    public LoginController() {}

    public void initialize() {
        setKeyAndClickListeners();
    }

    private void setKeyAndClickListeners() {
        submitBtn.setOnAction(event -> loginWithCredentials());
    }

    private void loginWithCredentials() {
        boolean rememberUsername = RememberPasswordCheck.isSelected();
        boolean rememberPassword = RememberPasswordCheck.isSelected();
        if(rememberUsername || rememberPassword) {
            fillRememberedCredentials();
        }
        loginInProgress = true;
        submitBtn.setDisable(true);
        Thread loginCredThread = new Thread(() -> {
            bufferImg.setVisible(true);
            isLoggedIn = loginLogic.attemptAuthentication(emailField.getText(), passwordField.getText(), rememberUsername, rememberPassword);
            bufferImg.setVisible(false);
            Platform.runLater(() -> {
                loginInProgress = false;
                submitBtn.setDisable(false);
                if(!isLoggedIn) {
                    showWrongCredentialsError("Wrong email or password");
                    emailField.getStyleClass().add("error");
                    passwordField.getStyleClass().add("error");
                }
            });
        });
        loginCredThread.start();
    }

    public void buttonPressedListener(KeyEvent e) {
        if(!loginInProgress) {
            if(e.getCode().toString().equals("ENTER")) {
                loginWithCredentials();
            }
        }

    }

    private void showWrongCredentialsError(String errorMessage) {
        wrongCredentials.setText(errorMessage);
        wrongCredentials.getStyleClass().add("error");
    }

    private void fillRememberedCredentials() {
        String filepath = FilePath.USER_HOME.getProperty();
        Properties prop = propertiesLogic.loadProps(filepath);
        emailField.setText(prop.getProperty("username"));
        passwordField.setText("password");
    }
}

