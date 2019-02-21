package GUI;

import ch.simas.jtoggl.JToggl;
import ch.simas.jtoggl.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.io.IOException;

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


    private boolean isLoggedIn() {
        String userString;
        boolean loggedIn = false;
            try{
                User user = jToggl.getCurrentUser();
                userString = user.toString();
                if(userString.contains("api_token")) {
                    loggedIn = true;
                }
                else {
                    loggedIn = false;
                }
            }
            catch(Throwable t){
                System.out.println("\nWrong username or password");
            }
        return loggedIn;
    }


    @FXML
    public void buttonPressedListener(KeyEvent e) {
        if(e.getCode().toString().equals("ENTER")) {
            login();
        }
    }
}
