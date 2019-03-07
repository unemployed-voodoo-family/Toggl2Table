package UnemployedVoodooFamily.GUI;

import UnemployedVoodooFamily.GUI.Content.SettingsController;
import UnemployedVoodooFamily.GUI.Content.TableViewController;
import UnemployedVoodooFamily.Logic.Listeners.DataLoadedListener;
import UnemployedVoodooFamily.Logic.Session;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    private ImageView refreshBtn;

    @FXML
    private AnchorPane contentRoot;

    private Node settings;
    private Node table;



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
        loadContent();
    }

    /**
     * Loads the other UnemployedVoodooFamily.GUI controllers and sets them as nodes
     */
    private void loadContent() {
        try {
            settings = new SettingsController().loadFXML();
            table = new TableViewController().loadFXML();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets input actions on UI elements
     */
    private void setKeyAndClickListeners() {

        settingsNavBtn.setOnAction(event -> switchContentView(settings));
        tableNavBtn.setOnAction(event -> switchContentView(table));
    }

    public void refreshRawData(){
        Session instance = Session.getInstance();
        Thread t = new Thread(() -> instance.refreshTimeData());
        t.start();
    }

    /**
     * Switches between the different UnemployedVoodooFamily.GUI controllers
     * @param content the UnemployedVoodooFamily.GUI controller, as a node, to switch to
     */
    private void switchContentView(Node content) {
        ObservableList<Node> children = contentRoot.getChildren();
        if(children.isEmpty()) {
            children.addAll(content);
        }
        else if(! children.contains(content)) {
            children.clear();
            children.addAll(content);
        }
    }
}
