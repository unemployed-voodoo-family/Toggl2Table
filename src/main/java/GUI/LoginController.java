package GUI;

import ch.simas.jtoggl.JToggl;
import ch.simas.jtoggl.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button submitBtn;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    private JToggl jToggl;


    public void initialize() {
        setKeyAndClickListeners();
    }

    private void setKeyAndClickListeners() {
        submitBtn.setOnAction(event -> login());
    }

    private void login() {
        //TODO: verify login credentials
        String user = emailField.getText();
        String password = passwordField.getText();
        jToggl = new JToggl(user, password);
        jToggl.switchLoggingOn();
        if(isLoggedIn()) {
            try {
                new GUIBaseController().start();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }


    }

    private boolean isLoggedIn() {
        boolean loggedIn;
        User user = jToggl.getCurrentUser();
        String userString = user.toString();
        if(userString.contains("api_token")) {
            loggedIn = true;
        }
        else {
            loggedIn = false;
        }
        return loggedIn;
    }

    @FXML
    public void enterButtonPressed(KeyEvent e) {
        if(e.getCode().toString().equals("ENTER")) {
            login();
        }
    }

}
