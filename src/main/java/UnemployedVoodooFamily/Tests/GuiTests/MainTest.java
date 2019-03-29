package UnemployedVoodooFamily.Tests.GuiTests;

import UnemployedVoodooFamily.GUI.GUIBaseController;
import UnemployedVoodooFamily.Logic.LoginLogic;
import UnemployedVoodooFamily.Main;
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
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;
import static org.loadui.testfx.Assertions.verifyThat;


public class MainTest extends ApplicationTest {

    @Override
    public void start (Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Before
    public void setUp() throws Exception {
        //Do something...
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void testEnglishInput() {
        String apiToken =  "a5f128064022cf3f6da6d4dab8bd7bd3";
        String password = "api_token";
        clickOn("#emailField");
        write(apiToken);
        clickOn("#passwordField");
        write(password);
        clickOn("#submitBtn");
        sleep(10000);
    }
}