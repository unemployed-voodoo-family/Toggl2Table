package UnemployedVoodooFamily.GUI;

import UnemployedVoodooFamily.GUI.Content.ProfileController;
import UnemployedVoodooFamily.GUI.Content.SettingsController;
import UnemployedVoodooFamily.GUI.Content.TableViewController;
import UnemployedVoodooFamily.Logger;
import UnemployedVoodooFamily.Logic.Session;
import UnemployedVoodooFamily.Main;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private ImageView refreshIcon;
    @FXML
    private ImageView logoutIcon;
    @FXML
    private ImageView helpIcon;

    private RotateTransition rotateTransition;

    @FXML
    private AnchorPane contentRoot;

    @FXML
    private HBox progressBox;

    @FXML
    private Text progressMessage;

    @FXML
    private Button logoutBtn;
    @FXML
    private Button helpBtn;

    @FXML
    private Label lastFetchedLabel;

    private Node settings;
    private Node table;
    private Node profile;
    private Thread t1;
    private static Stage loginStage;

    private AtomicBoolean active;

    private ToggleGroup navButtons = new ToggleGroup();


    @FXML
    public void start() throws IOException {
        Stage appStage = new Stage();
        URL r = getClass().getClassLoader().getResource("LayoutBase.fxml");
        Parent root = FXMLLoader.load(r);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("styles.css");
        appStage.setTitle("Toggl Time Sheet");
        appStage.getIcons().add(new Image("/icons/app_icon/96x96.png"));
        appStage.setScene(scene);
        appStage.show();
    }

    public void initialize() {
        settingsNavBtn.setToggleGroup(navButtons);
        tableNavBtn.setToggleGroup(navButtons);
        profileNavBtn.setToggleGroup(navButtons);
        profileNavBtn.setGraphic(avatarView);
        active = new AtomicBoolean(false);
        rotateSettings();
        applyStyles();
        setKeyAndClickListeners();
        loadContent();
        refreshData();
        dumpData();
        tableNavBtn.fire();
    }


    /**
     * Loads the other UnemployedVoodooFamily.GUI controllers and sets them as nodes
     */
    private void loadContent() {
        try {
            settings = new SettingsController().loadFXML();
            table = new TableViewController().loadFXML();
            profile = new ProfileController().loadFXML();
            profileNavBtn.setText(Session.getInstance().getUser().getFullname());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void applyStyles()  {
        ColorAdjust whiteout = new ColorAdjust();
        whiteout.setBrightness(1);
        avatarView.setEffect(whiteout);
        ColorAdjust lightgray = new ColorAdjust();
        lightgray.setBrightness(0.7);
        refreshIcon.setEffect(lightgray);
        logoutIcon.setEffect(lightgray);
        helpIcon.setEffect(lightgray);
    }

    /**
     * Sets input actions on UI elements
     */
    private void setKeyAndClickListeners() {

        refreshBtn.setOnAction(event -> {
            refreshData();
        });
        setToolbarImgColor(refreshBtn);
        setToolbarImgColor(refreshBtn);
        setToolbarImgColor(refreshBtn);

        logoutBtn.setOnAction(this :: logOutOfApplication);
        helpBtn.setOnAction(event -> this.openHelpPrompt());
        settingsNavBtn.setOnAction(event -> switchContentView(settings));
        tableNavBtn.setOnAction(event -> switchContentView(table));
        profileNavBtn.setOnAction(event -> switchContentView(profile));
    }


    private void setToolbarImgColor(Button button) {
        button.setOnMouseEntered(event ->   {
            System.out.println("hellp");
            ColorAdjust whiteout = new ColorAdjust();
            whiteout.setBrightness(1);
            button.setEffect(whiteout);
            System.out.println(button.getEffect());
        });
        button.setOnMouseExited(event -> {
            ColorAdjust whiteout = new ColorAdjust();
            whiteout.setBrightness(0.7);
            button.setEffect(whiteout);
        });
    }
    public void refreshData() {
        progressBox.setVisible(true);
        spinRefreshBtn(true);
        DateTimeFormatter d = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

        t1 = new Thread(() -> {

            String prefix = "Fetching ";
            StringBuilder sb = new StringBuilder();
            sb.append("(1/6) ");
            Session session = Session.getInstance();
            Platform.runLater(() -> progressMessage.setText(sb + prefix + "work hours"));
            session.refreshWorkHours();
            Platform.runLater(() -> progressMessage.setText(sb.replace(1, 2, "2") + prefix + "workspaces"));
            session.refreshWorkspaces();
            Platform.runLater(() -> progressMessage.setText(sb.replace(1, 2, "3") + prefix + "projects"));
            session.refreshProjects();
            Platform.runLater(() -> progressMessage.setText(sb.replace(1, 2, "4") + prefix + "tasks"));
            session.refreshTasks();
            Platform.runLater(() -> progressMessage.setText(sb.replace(1, 2, "5") + prefix + "clients"));
            session.refreshClient();
            Platform.runLater(() -> progressMessage.setText(sb.replace(1, 2, "6") + prefix + "time entries"));
            session.refreshTimeEntries();
            Platform.runLater(() -> {
                progressBox.setVisible(false);
                spinRefreshBtn(false);
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
                logger.dumpCollection(session.getProjects().values(), "projects");
                logger.dumpCollection(session.getTasks().values(), "tasks");
                logger.dumpCollection(session.getTimeEntries(), "timeentries");
                logger.dumpCollection(session.getWorkspaces().values(), "workspaces");
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

    private void spinRefreshBtn(boolean spinning) {
        active.set(spinning);
        Thread t3 = new Thread(() -> {

            while(active.get()) {
                try {
                    rotateTransition.play();
                }
                catch(NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
        if(active.get()) {
            t3.start();
        }
    }

    private void rotateSettings() {
        rotateTransition = new RotateTransition(Duration.seconds(1.50), refreshIcon);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(360);
        rotateTransition.setCycleCount(1);
    }

    /**
     * Logs out of the main GUI application, after the windows has been closed, it initializes and
     * lauches a new stage with the login FXML file
     * @param event that is used to fetch the Node from where the method was called.
     */
    private void logOutOfApplication(ActionEvent event){
        //Closes the current active Base GUI.
        Node  source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        Session.terminateSession();
        stage.close();

        //Start a new instance of the login stage
        loginStage = new Stage();
        URL r = getClass().getClassLoader().getResource("login.fxml");
        Parent root = null;
        try {
            root = FXMLLoader.load(r);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        scene.getStylesheets().add("styles.css");
        loginStage.setTitle("Toggl Time Sheet - Login");
        loginStage.setScene(scene);
        Main.initStage(loginStage);
        loginStage.show();
    }

    private void openHelpPrompt() {

    }

    public static boolean loginStageExists(){
        if(loginStage == null){
            return false;
        }
        else{
            return true;
        }
    }
    /**
     * A method that can be called from anywhere to terminate the login stage.
     */
    public static void closeLogin() {
        loginStage.close();
    }
}
