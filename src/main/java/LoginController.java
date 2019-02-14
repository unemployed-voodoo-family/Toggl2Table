import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.xml.soap.Text;
import java.io.IOException;

public class LoginController {

    @FXML
    private Button SubmitBtn;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField PasswordField;


    public void initialize() {
        setKeyAndClickListeners();
    }

    private void setKeyAndClickListeners() {
        SubmitBtn.setOnAction(event -> login());
    }

    private void login() {
        //TODO: verify login credentials
        try {

            new GUIBaseController().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
