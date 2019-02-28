package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.GUI.GUIBaseController;
import ch.simas.jtoggl.JToggl;
import ch.simas.jtoggl.User;
import javafx.application.Platform;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginLogic {

    private JToggl jToggl;


    public boolean attemptAuthentication(String username, String password){
        // Run this thread to avoid UnemployedVoodooFamily.GUI freezing
        AtomicBoolean loginStatus = new AtomicBoolean(false);
        jToggl = new JToggl(username, password);
        Thread toggleThread = new Thread(() -> {
            jToggl.switchLoggingOn();
            loginStatus.set(isLoggedIn());
            if(loginStatus.get()){
                Platform.runLater(() -> {
                    try {
                        new GUIBaseController().start();
                    }
                    catch(IOException ioe)  {
                        System.out.println(ioe.getMessage());
                    }
                });
            }
        });
        toggleThread.start();
        Session.getInstance().setSession(jToggl);
        return loginStatus.get();
    }


    private boolean isLoggedIn() {
        String userString;
        boolean loggedIn = false;
        User user = jToggl.getCurrentUser();
        userString = user.toString();
        if(userString.contains("api_token")) {
            loggedIn = true;
        }
        else {
            loggedIn = false;
            System.out.println("\nWrong username or password");
        }
        return loggedIn;
    }

}
