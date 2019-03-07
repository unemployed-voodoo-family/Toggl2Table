package UnemployedVoodooFamily.Data.Enums;

import UnemployedVoodooFamily.Logic.Session;

public enum FilePath {
    USER_HOME(System.getProperty("user.home")),
    APP_HOME(USER_HOME.getProperty() + "\\ToggleTimeSheet"),
    SETTINGS_HOME(APP_HOME.getProperty() + "\\Settings"),
    SAVED_WORKHOURS(SETTINGS_HOME.getProperty() + "\\hours.properties");

    private String property;

    FilePath(String property) {
        this.property = property;
    }

    public String getProperty() {
        return this.property;
    }

    public static String getCurrentUserWorkhours() {
        return SETTINGS_HOME.getProperty() + "\\" + Session.getInstance().getUser().getId() + "-hours.properties";
    }
}
