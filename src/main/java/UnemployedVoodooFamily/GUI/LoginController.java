package UnemployedVoodooFamily.GUI;

import UnemployedVoodooFamily.Logic.LoginLogic;
import UnemployedVoodooFamily.Logic.PropertiesLogic;
import UnemployedVoodooFamily.Utils.PasswordUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Properties;


public class LoginController {

    @FXML
    private Button submitBtn;
    @FXML
    private ProgressIndicator bufferImg;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox RememberEmailCheck;
    @FXML
    private CheckBox RememberPasswordCheck;
    @FXML
    private Label wrongCredentials;

    private LoginLogic loginLogic = new LoginLogic();
    private boolean isLoggedIn;
    private boolean loginInProgress;

    public void initialize() {
        bufferImg.setVisible(false);
        setKeyAndClickListeners();
        fillRememberedCredentials();
    }

    private void setKeyAndClickListeners() {
        submitBtn.setOnAction(event -> loginWithCredentials());
    }

    private void loginWithCredentials() {
        boolean rememberUsername = RememberEmailCheck.isSelected();
        boolean rememberPassword = RememberPasswordCheck.isSelected();
        loginInProgress = true;
        submitBtn.setDisable(true);
        RememberEmailCheck.setDisable(true);
        RememberPasswordCheck.setDisable(true);
        Thread loginCredThread = new Thread(() -> {
            bufferImg.setVisible(true);
            isLoggedIn = loginLogic
                    .attemptAuthentication(emailField.getText(), passwordField.getText(), rememberUsername,
                                           rememberPassword);
            bufferImg.setVisible(false);
            Platform.runLater(() -> {
                loginInProgress = false;
                submitBtn.setDisable(false);
                RememberEmailCheck.setDisable(false);
                RememberPasswordCheck.setDisable(false);
                if(! isLoggedIn) {
                    showWrongCredentialsError("Wrong email or password");
                    emailField.getStyleClass().add("error");
                    passwordField.getStyleClass().add("error");
                }
            });
        });
        loginCredThread.start();
    }

    public void buttonPressedListener(KeyEvent e) {
        if(! loginInProgress) {
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

        String filepath = FilePath.APP_HOME.getPath() + File.separator + "credentials.properties";
        Properties prop = propertiesLogic.loadProps(filepath);
        String securePassword = prop.getProperty("password");
        String decodedPassword = PasswordUtils.decodeSecurePassword(securePassword);
        String email = prop.getProperty("username");

        if(email == null) {
            email = "";
        }
        if(! email.isEmpty()) {
            emailField.setText(email);
            RememberEmailCheck.setSelected(true);
        }

        passwordField.setText(decodedPassword);
        if(! decodedPassword.equals("")) {
            RememberPasswordCheck.setSelected(true);
        }
    }
}
