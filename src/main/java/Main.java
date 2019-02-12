import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method is called automatically by JavaFX when the application is
     * launched
     *
     * @param primaryStage The main "stage" where the GUI will be rendered
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL r = getClass().getClassLoader().getResource("login.fxml");
        Parent root = FXMLLoader.load(r);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("styles.css");
        //scene.getStylesheets().add("styles/style.css");
        primaryStage.setTitle("Toggle Time Sheet - Login");
        primaryStage.setScene(scene);
        //Image anotherIcon = new Image();
        //primaryStage.getIcons().add(anotherIcon);
        primaryStage.show();
    }
}
