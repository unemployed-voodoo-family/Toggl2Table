package GUI;

import ch.simas.jtoggl.JToggl;
import ch.simas.jtoggl.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class LoginController {

    @FXML
    private Button submitBtn;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ImageView bufferImg;
    private JToggl jToggl;
    private boolean loggedIn = false;


    public void initialize() {
        setKeyAndClickListeners();
    }

    private void setKeyAndClickListeners() {
        submitBtn.setOnAction(event -> login());
    }

    private void login() {
        bufferImg.setVisible(true);
        attemptAuthentication(emailField.getText(), passwordField.getText());
    }


    private void attemptAuthentication(String username, String password){
        // Run this thread to avoid GUI freezing
        Thread toggleThread = new Thread(() -> {
            jToggl = new JToggl(username, password);
            jToggl.switchLoggingOn();

            if(isLoggedIn()){
                Platform.runLater(() -> {
                    try{
                        new GUIBaseController().start();
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            bufferImg.setVisible(false);
            });
        toggleThread.start();
    }


    //TODO: If username or password is wrong, make sure it doesn't crash
    private boolean isLoggedIn() {
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
