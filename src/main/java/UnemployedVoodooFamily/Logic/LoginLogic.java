package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.GUI.GUIBaseController;
import ch.simas.jtoggl.JToggl;
import ch.simas.jtoggl.User;
import javafx.application.Platform;


import java.io.IOException;

public class LoginLogic {

    private JToggl jToggl;


    public boolean attemptAuthentication(String username, String password) {
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
}
