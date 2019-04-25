package testGUI;

import UnemployedVoodooFamily.Logic.Session;
import ch.simas.jtoggl.JToggl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import java.net.URL;


import static org.junit.Assert.*;

public class GUIBaseControllerTest extends ApplicationTest {


    @Override
    public void start(Stage stage) throws Exception {
        Stage newStage = new Stage();
        URL r = getClass().getClassLoader().getResource("LayoutBase.fxml");
        Session session = Session.getInstance();
        session.setSession(new JToggl("a5f128064022cf3f6da6d4dab8bd7bd3", "api_token"));
        System.out.println(Session.getInstance());
        Parent root = FXMLLoader.load(r);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("styles.css");
        newStage.setTitle("Toggl Time Sheet - Main");
        newStage.setScene(scene);
        newStage.show();
    }


    @Before
    public void setUp() throws Exception {
        //Do something...
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }


    @Test
    public void refreshData() {
    }

    //Does not test the displaying of raw data in table.
    @Test
    public void testRawData() {
        boolean assertionValue = false;
        Session session = Session.getInstance();
        session.refreshTimeEntries();
        System.out.println(session.getTimeEntries());
        if(!session.getTimeEntries().contains(null)) {
            assertionValue = true;
        }
        else {
            System.out.println("The test did not find any time entries.");
        }
        assertTrue(assertionValue);
    }
}