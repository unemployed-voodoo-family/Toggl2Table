package UnemployedVoodooFamily.GUI;

import UnemployedVoodooFamily.Logic.LoginLogic;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

public class LoginController {

    @FXML
    private Button submitBtn;
    @FXML
    private ImageView bufferImg;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private LoginLogic loginLogic = new LoginLogic();


    public void initialize() {
        setKeyAndClickListeners();
    }

    private void setKeyAndClickListeners() {
        submitBtn.setOnAction(event -> loginWithCredentials());
    }

    public void loginWithCredentials() {
        Thread loginCredThread = new Thread(() -> {
            if(!emailField.getText().isEmpty() && !passwordField.getText().isEmpty()){
                bufferImg.setVisible(true);
                loginLogic.attemptAuthentication(emailField.getText(), passwordField.getText());
            }
            if(!loginLogic.isLoggedIn()){
                emailField.getStyleClass().add("error");
                passwordField.getStyleClass().add("error");
            }
            bufferImg.setVisible(false);
            });
        loginCredThread.start();
    }

    public void buttonPressedListener(KeyEvent e) {
        if(e.getCode().toString().equals("ENTER")) {
            loginWithCredentials();
        }
    }
}