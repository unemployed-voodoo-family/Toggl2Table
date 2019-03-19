package UnemployedVoodooFamily.GUI;

import UnemployedVoodooFamily.Logic.LoginLogic;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

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
    private Label wrongCredentials;

    private LoginLogic loginLogic = new LoginLogic();
    private boolean isLoggedIn;
    private boolean loginInProgress;

    public void initialize() {
        bufferImg.setVisible(false);
        setKeyAndClickListeners();
    }

    private void setKeyAndClickListeners() {
        submitBtn.setOnAction(event -> loginWithCredentials());
    }

    public void loginWithCredentials() {
        loginInProgress = true;
        submitBtn.setDisable(true);
        Thread loginCredThread = new Thread(() -> {
            bufferImg.setVisible(true);
            isLoggedIn = loginLogic.attemptAuthentication(emailField.getText(), passwordField.getText());
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

    private void disableSubmit() {

    }
}

