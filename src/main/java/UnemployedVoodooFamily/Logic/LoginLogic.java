package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.GUI.GUIBaseController;
import UnemployedVoodooFamily.Utils.PasswordUtils;
import ch.simas.jtoggl.JToggl;
import ch.simas.jtoggl.User;
import javafx.application.Platform;
import javafx.scene.control.PasswordField;


import java.io.IOException;

import static UnemployedVoodooFamily.Utils.PasswordUtils.generateSecurePassword;

public class LoginLogic {

    private JToggl jToggl;


    public boolean attemptAuthentication(String username, String password, boolean rememberPassword) {
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
            if(rememberPassword){
                String salt = PasswordUtils.getSalt(30);
                String securePassword = PasswordUtils.generateSecurePassword(password, salt);
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
}
