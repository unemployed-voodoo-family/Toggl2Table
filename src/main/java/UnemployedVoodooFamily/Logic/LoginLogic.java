package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Logic.Utils.PasswordUtils;
import ch.simas.jtoggl.JToggl;

import java.util.Properties;

public class LoginLogic {

    private FileLogic fileLogic = new FileLogic();

    public boolean attemptAuthentication(String username, String password, boolean rememberUsername,
                                         boolean rememberPassword) {
        Session session = Session.getInstance();
        boolean loggedIn = false;
        try {
            session.setSession(new JToggl(username, password));
            // Run this thread to avoid UnemployedVoodooFamily.GUI from freezing
            loggedIn = true;
            rememberWhich(username, password, rememberUsername, rememberPassword);
        }
        catch(RuntimeException e) {
            e.getMessage();
            Session.terminateSession();
        }

        return loggedIn;
    }


    private boolean saveUsernameAndPassword(String username, String securePassword) {
        String filepath = FilePath.APP_HOME.getPath() + "/credentials.properties";
        Properties prop = FileLogic.loadProps(filepath);
        prop.setProperty("username", username);
        prop.setProperty("password", securePassword);
        return FileLogic.saveProps(prop, filepath);
    }

    private void rememberWhich(String username, String password, boolean rememberUsername, boolean rememberPassword) {
        String securePassword = rememberPassword ? PasswordUtils.generateSecurePassword(password) : "";
        if(! rememberUsername) {
            username = "";
        }
        saveUsernameAndPassword(username, securePassword);
    }

}
