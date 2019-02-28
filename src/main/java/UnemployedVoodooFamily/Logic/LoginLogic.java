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
            Platform.runLater(() -> {
                try {
                    new GUIBaseController().start();
                }
                catch(IOException ioe)  {
                    System.out.println(ioe.getMessage());
                }
            });
            loggedIn = true;
        }
        catch(RuntimeException e) {
            //Forbidden!
            Session.getInstance().terminateSession();
        }
        return loggedIn;
    }


    /*private boolean isLoggedIn() {
        String userString;
        boolean loggedIn = false;
        try{
            User user = jToggl.getCurrentUser();
            userString = user.toString();
            if(userString.contains("api_token")) {
                loggedIn = true;
            }
            else {
                loggedIn = false;
                System.out.println("\nWrong username or password");
            }
        }
        catch(RuntimeException t){
            System.out.println(t.getMessage());
        }
        return loggedIn;
    }*/

}
