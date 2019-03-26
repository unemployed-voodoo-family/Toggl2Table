package UnemployedVoodooFamily.GUI;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.GUI.Content.SettingsController;
import UnemployedVoodooFamily.GUI.Content.TableViewController;
import UnemployedVoodooFamily.Logger;
import UnemployedVoodooFamily.Logic.Session;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GUIBaseController {

    @FXML
    private GridPane appRoot;

    @FXML
    private ImageView avatarView;

    @FXML
    private Label userNameLabel;

    @FXML
    private ToggleButton tableNavBtn;

    @FXML
    private ToggleButton settingsNavBtn;

    @FXML
    private ToggleButton profileNavBtn;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    @FXML
    private Button refreshBtn;

    @FXML
    private AnchorPane contentRoot;

    @FXML
    private MenuItem dumpDataMenuItem;

    @FXML
    private MenuItem viewDataMenuItem;

    @FXML
    private HBox progressBox;

    @FXML
    private Text progressMessage;

    @FXML
    private Label lastFetchedLabel;

    private Node settings;
    private Node table;
    private Thread t1;

    private ToggleGroup navButtons = new ToggleGroup();


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
        settingsNavBtn.setToggleGroup(navButtons);
        tableNavBtn.setToggleGroup(navButtons);
        profileNavBtn.setToggleGroup(navButtons);
        profileNavBtn.setGraphic(avatarView);
        setKeyAndClickListeners();
        loadContent();
        refreshData();
        dumpData();
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

        refreshBtn.setOnAction(event -> refreshData());
        settingsNavBtn.setOnAction(event -> switchContentView(settings));
        tableNavBtn.setOnAction(event -> switchContentView(table));
        dumpDataMenuItem.setOnAction(event -> dumpData());
        viewDataMenuItem.setOnAction(event -> {
            try {
                Desktop.getDesktop().open(new File(FilePath.LOGS_HOME.getPath()));
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            catch(IllegalArgumentException e) {
                //could not find path
            }
        });
    }


    public void refreshData() {
        progressBox.setVisible(true);
        DateTimeFormatter d = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

        t1 = new Thread(() -> {

            String prefix = "Fetching ";
            StringBuilder sb = new StringBuilder();
            sb.append("(1/5) ");

            Session session = Session.getInstance();
            Platform.runLater(() -> progressMessage.setText(sb + prefix + "work hours"));
            session.refreshWorkHours();
            Platform.runLater(() -> progressMessage.setText(sb.replace(1, 2, "2") + prefix + "time entries"));
            session.refreshTimeEntries();
            Platform.runLater(() -> progressMessage.setText(sb.replace(1, 2, "3") + prefix + "workspaces"));
            session.refreshWorkspaces();
            Platform.runLater(() -> progressMessage.setText(sb.replace(1, 2, "4") + prefix + "projects"));
            session.refreshProjects();
            Platform.runLater(() -> progressMessage.setText(sb.replace(1, 2, "5") + prefix + "tasks"));
            session.refreshTasks();
            Platform.runLater(() -> {
                progressBox.setVisible(false);
                lastFetchedLabel.setText(LocalDateTime.now().format(d));
            });
        });
        t1.start();
    }

    private void dumpData() {
        Thread t2 = new Thread(() -> {
            try {
                t1.join();
                Logger logger = Logger.getInstance();
                Session session = Session.getInstance();
                logger.dumpCollection(session.getProjects(), "projects");
                logger.dumpCollection(session.getTasks(), "tasks");
                logger.dumpCollection(session.getTimeEntries(), "timeentries");
                logger.dumpCollection(session.getWorkspaces(), "workspaces");
                logger.dumpCollection(session.getWorkHours().entrySet(), "workhours");
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        });
        t2.start();
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
