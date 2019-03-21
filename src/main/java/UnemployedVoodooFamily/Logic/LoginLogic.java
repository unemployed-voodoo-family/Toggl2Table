package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.GUI.GUIBaseController;
import UnemployedVoodooFamily.Utils.PasswordUtils;
import ch.simas.jtoggl.JToggl;
import javafx.application.Platform;
import javafx.scene.control.PasswordField;
import java.io.*;
import java.util.Properties;


import static UnemployedVoodooFamily.Utils.PasswordUtils.generateSecurePassword;

public class LoginLogic {

    private PropertiesLogic propertiesLogic = new PropertiesLogic();

    public boolean attemptAuthentication(String username, String password, boolean rememberUsername, boolean rememberPassword) {
        // Run this thread to avoid UnemployedVoodooFamily.GUI freezing
        Session session = Session.getInstance();
        boolean loggedIn = false;
        try {
            session.setSession(new JToggl(username, password));
            Thread togglThread = new Thread(() -> Platform.runLater(() -> {
                try {
                    new GUIBaseController().start();
                }
                catch(IOException ioe) {
                    System.out.println(ioe.getMessage());
                }
            }));
            togglThread.start();
            loggedIn = true;
            if(rememberUsername && rememberPassword) {
                String salt = PasswordUtils.getSalt(30);
                String securePassword = PasswordUtils.generateSecurePassword(password, salt);
                saveUsernameAndPassword(username, securePassword);
            }
            else if(!rememberUsername && rememberPassword) {
                String salt = PasswordUtils.getSalt(30);
                String securePassword = PasswordUtils.generateSecurePassword(password, salt);
                saveUsernameAndPassword(null, securePassword);
            }
            else if(rememberUsername && !rememberPassword) {
                saveUsernameAndPassword(username, null);
            }
            Thread timeDataThread = new Thread(() -> Session.getInstance().refreshTimeData());
            timeDataThread.start();
            togglThread.join();
            timeDataThread.join();
        }
        catch(RuntimeException e) {
            //Forbidden!
            Session.getInstance().terminateSession();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }


        return loggedIn;
    }

    private static void verifyProvidedPassword(String password) {
        int passwordLength = password.length();
        generateSecurePassword(password, PasswordUtils.getSalt(passwordLength));
    }

    private void saveUsernameAndPassword(String username, String securePassword) {
        OutputStream output = null;
        String filepath = FilePath.USER_HOME.getPath();
        Properties prop = propertiesLogic.loadProps(filepath);
        prop.setProperty("username", username);
        prop.setProperty("password", securePassword);
        try {
            prop.store(output, null);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
