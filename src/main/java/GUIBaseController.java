import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    private Pane contentRoot;

    @FXML
    public void start() throws IOException {
        Stage newStage = new Stage();
        URL r = getClass().getClassLoader().getResource("LayoutBase.fxml");
        Parent root = FXMLLoader.load(r);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("styles.css");
        newStage.setScene(scene);
        newStage.show();
    }

    public void initialize() {
        setKeyAndClickListeners();
    }


    public GUIBaseController() {
    }

    private void setKeyAndClickListeners() {

        settingsNavBtn.setOnAction(event -> showSettingsView());
        tableNavBtn.setOnAction(event -> showTableView());
    }

    private void showTableView() {
        TableViewController settings = new TableViewController();
        try {
            Node content = (TabPane) settings.loadFXML();
            contentRoot.getChildren().addAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSettingsView() {
        //TODO: Find the scroll-bar
        SettingsController settings = new SettingsController();
        try {
            ScrollPane content = (ScrollPane) settings.loadFXML();
            content.setPrefWidth(contentRoot.getWidth());
            contentRoot.getChildren().addAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
