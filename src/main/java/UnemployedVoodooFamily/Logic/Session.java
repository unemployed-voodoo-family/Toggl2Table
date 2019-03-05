package UnemployedVoodooFamily.Logic;

import ch.simas.jtoggl.JToggl;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.TimeEntry;
import ch.simas.jtoggl.User;

import java.util.List;

public class Session {

    private static JToggl jToggl = null;
    private static List<TimeEntry> timeEntries;
    private static List<Project> projects;
    private static User user;

    private static Session ourInstance = new Session();

    public static Session getInstance() {
        return ourInstance;
    }

    private Session() {
    }

    public void setSession(JToggl jToggl) {
        if(this.jToggl == null) {
            this.jToggl = jToggl;
            refreshData();
        }
        else {
            //already logged in!
        }
    }

    public static void terminateSession() {
        jToggl = null;
    }

    public static List<TimeEntry> getTimeEntries() {
        return timeEntries;
    }

    public static List<Project> getProjects() {
        return projects;
    }

    public static User getUser() {
        return user;
    }

    public static void refreshUser() {
        user = jToggl.getCurrentUser();
    }

    public static void refreshTimeEntries() {
        timeEntries = jToggl.getTimeEntries();
    }

    public static void refreshProjects() {

        projects = jToggl.getProjects();
    }

    public static void refreshData() {
        user = jToggl.getCurrentUser();
        timeEntries = jToggl.getTimeEntries();
        projects = jToggl.getProjects();
    }
}
