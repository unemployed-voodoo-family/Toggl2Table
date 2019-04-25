package UnemployedVoodooFamily.Data.Enums;

import UnemployedVoodooFamily.Logic.Session;
import ch.simas.jtoggl.User;

import java.io.File;

public enum FilePath {
    USER_HOME(System.getProperty("user.home")), APP_HOME(USER_HOME.getPath() + File.separator + "TogglTimeSheet"),
    SETTINGS_HOME(APP_HOME.getPath() + File.separator + "Settings"),
    LOGS_HOME(APP_HOME.getPath() + File.separator + "logs"),
    SAVED_WORKHOURS(SETTINGS_HOME.getPath() + File.separator + "hours.json");

    private String path;

    FilePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public static String getCurrentUserWorkhours() {
        return SETTINGS_HOME.getPath() + File.separator + "supposed-hours.json";
    }
}

