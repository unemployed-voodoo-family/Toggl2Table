import ch.simas.jtoggl.JToggl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private Button SubmitBtn;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField PasswordField;
    private static JToggl jToggl;

    public void initialize() {
        setKeyAndClickListeners();
    }

    private void setKeyAndClickListeners() {
        SubmitBtn.setOnAction(event -> login());
    }

    private void login() {
        //TODO: verify login credentials
        String user = emailField.getText();
        String password = PasswordField.getText();
        jToggl = new JToggl(user, password);
        jToggl.switchLoggingOn();

        try {
            new GUIBaseController().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
