package GUI;

import ch.simas.jtoggl.JToggl;
import ch.simas.jtoggl.User;
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
    boolean loggedIn = false;


    public void initialize() {
        setKeyAndClickListeners();
    }

    private void setKeyAndClickListeners() {
        submitBtn.setOnAction(event -> login());
    }

    private void login() {
        //TODO: verify login credentials
        bufferImg.setVisible(true);
        toggleThread.start();
    }



    // Run thread to avoid GUI freeze
    Thread toggleThread = new Thread(() -> {
        jToggl = new JToggl(emailField.getText(), passwordField.getText());
        jToggl.switchLoggingOn();
        if(isLoggedIn()) {
            try {
                new GUIBaseController().start();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            bufferImg.setVisible(false);
        }
    });



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
        //System.out.println("ok");
    }
}
