package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.GUI.GUIBaseController;
import UnemployedVoodooFamily.Utils.PasswordUtils;
import ch.simas.jtoggl.JToggl;
import javafx.application.Platform;

import java.io.*;
import java.util.Properties;



public class LoginLogic {

    private FileLogic fileLogic = new FileLogic();

    public boolean attemptAuthentication(String username, String password, boolean rememberUsername, boolean rememberPassword) {
        Session session = Session.getInstance();
        boolean loggedIn = false;
        try {
            session.setSession(new JToggl(username, password));
            // Run this thread to avoid UnemployedVoodooFamily.GUI from freezing
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
            rememberWhich(username, password, rememberUsername, rememberPassword);
            togglThread.join();
        }
        catch(RuntimeException e) {
            e.getMessage();
            Session.getInstance().terminateSession();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }


        return loggedIn;
    }


    private boolean saveUsernameAndPassword(String username, String securePassword) {
        boolean storeSuccessful = false;
        OutputStream output;
        String filepath = FilePath.APP_HOME.getPath() + "/credentials.properties";
        Properties prop = fileLogic.loadProps(filepath);
        prop.setProperty("username", username);
        prop.setProperty("password", securePassword);
        try {
            output = new FileOutputStream(filepath);
            prop.store(output, null);
            storeSuccessful = true;
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return storeSuccessful;
    }

    private void rememberWhich(String username, String password, boolean rememberUsername, boolean rememberPassword) {
        if(rememberUsername && rememberPassword) {
            String securePassword = PasswordUtils.generateSecurePassword(password);
            saveUsernameAndPassword(username, securePassword);
        }
        else if(!rememberUsername && rememberPassword) {
            String securePassword = PasswordUtils.generateSecurePassword(password);
            saveUsernameAndPassword("", securePassword);
        }
        else if(rememberUsername && !rememberPassword) {
            saveUsernameAndPassword(username, "");
        }
        else {
            saveUsernameAndPassword("", "");
        }
    }
}
