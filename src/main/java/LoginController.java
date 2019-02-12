import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LoginController {

    @FXML
    private Button SubmitBtn;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
