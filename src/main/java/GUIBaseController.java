import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class GUIBaseController {

    @FXML
    private GridPane appRoot;

    @FXML
    private ImageView avatarView;

    @FXML
    private Label userNameLabel;

    @FXML
    private Button tableNavBtn;

    @FXML
    private Button settingsNavBtn;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    public void start() throws Exception {
        Stage newStage = new Stage();
        URL r = getClass().getClassLoader().getResource("LayoutBase.fxml");
        Parent root = FXMLLoader.load(r);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("styles.css");
        newStage.setScene(scene);
        newStage.show();
    }


    public GUIBaseController() {

    }

    private void setKeyAndClickListeners() {
        settingsNavBtn.setOnAction(event -> showSettingsView());
    }



    private void showSettingsView() {
        //TODO: Make application show settings
    }


}
