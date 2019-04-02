package testGUI;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Logic.PropertiesLogic;
import UnemployedVoodooFamily.Utils.PasswordUtils;
import com.sun.javafx.stage.StageHelper;
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

import java.util.Objects;
import java.util.Properties;

import static org.junit.Assert.*;


public class LoginLogicGUITest extends ApplicationTest {

    public LoginLogicGUITest() {}

    @Override
    public void start(Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("login.fxml")));
        stage.setScene(new Scene(mainNode));
        stage.show();
    }


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    @Test
    public void testSuccessfulLogin() {
        boolean loginSuccessful;
        String apiToken = "a5f128064022cf3f6da6d4dab8bd7bd3";
        String password = "api_token";
        clickOn("#emailField");
        write(apiToken);
        clickOn("#passwordField");
        write(password);
        clickOn("#submitBtn");
        sleep(10000);
        String stages = StageHelper.getStages().toString();
        if(stages.contains("javafx.stage.Stage@32d992b2")) {
            loginSuccessful = true;
        }
        else {
            loginSuccessful = false;
        }
        assertTrue(loginSuccessful);
    }

    //Make sure the remember checkboxes are ticked before attempting this test
    @Test
    public void testRememberCredentials() {
        boolean rememberedCredentials;
        PropertiesLogic propertiesLogic = new PropertiesLogic();
        String apiToken = "a5f128064022cf3f6da6d4dab8bd7bd3";
        String password = "api_token";
        doubleClickOn("#emailField");
        write("");
        write(apiToken);
        doubleClickOn("#passwordField");
        write("");
        write(password);
        clickOn("#submitBtn");
        sleep(10000);
        String filepath = FilePath.APP_HOME.getPath() + "/credentials.properties";
        Properties prop = propertiesLogic.loadProps(filepath);
        String savedUsername = prop.getProperty("username");
        String savedPassword = PasswordUtils.decodeSecurePassword(prop.getProperty("password"));
        if(savedUsername.equals(apiToken) && savedPassword.equals(password)) {
            rememberedCredentials = true;
        }
        else {
            rememberedCredentials = false;
        }
        assertTrue(rememberedCredentials);
    }

}