package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.GUI.GUIBaseController;
import ch.simas.jtoggl.JToggl;
import ch.simas.jtoggl.User;
import javafx.application.Platform;


import java.io.IOException;

public class LoginLogic {

    private JToggl jToggl;


    public boolean attemptAuthentication(String username, String password){
        // Run this thread to avoid UnemployedVoodooFamily.GUI freezing
        jToggl = new JToggl(username, password);
        Session session = Session.getInstance();
        boolean loggedIn = false;
        try {
            session.setSession(jToggl);
            Thread togglThread = new Thread(() -> {
                Platform.runLater(() -> {
                    try {
                        new GUIBaseController().start();
                    }
                    catch(IOException ioe) {
                        System.out.println(ioe.getMessage());
                    }
                });
            });
            togglThread.start();
            loggedIn = true;
        }
        catch(RuntimeException e) {
            //Forbidden!
            Session.getInstance().terminateSession();
        }

        return loggedIn;
    }
}
