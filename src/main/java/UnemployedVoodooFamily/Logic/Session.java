package UnemployedVoodooFamily.Logic;

import ch.simas.jtoggl.JToggl;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.TimeEntry;
import ch.simas.jtoggl.User;

import java.util.List;

public class Session {

    private JToggl jToggl = null;
    private List<TimeEntry> timeEntries;
    private List<Project> projects;
    private User user;

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

    public JToggl getSession() {
        return this.jToggl;
    }

    public void terminateSession() {
        jToggl = null;
    }

    public List<TimeEntry> getTimeEntries() {
        return timeEntries;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public User getUser() {
        return user;
    }

    public void refreshData() {
        this.user = jToggl.getCurrentUser();
        this.timeEntries = jToggl.getTimeEntries();
        this.projects = jToggl.getProjects();
    }
}
