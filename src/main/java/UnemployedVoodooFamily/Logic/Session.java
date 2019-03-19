package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.Enums.Data;
import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Logic.Listeners.DataLoadedListener;
import ch.simas.jtoggl.*;

import java.time.LocalDate;
import java.util.*;

public class Session {

    private static JToggl jToggl = null;
    private List<TimeEntry> timeEntries;
    private List<Project> projects;
    private List<Workspace> workspaces;
    private List<Task> tasks;
    private User user;

    private Properties workHours;
    private PropertiesLogic propsLogic;

    private static Session togglSession = new Session();

    private List<DataLoadedListener> listeners = new ArrayList<>();

    private Session() {
        this.propsLogic = new PropertiesLogic();
    }

    synchronized public static Session getInstance() {
        return togglSession;
    }

    public void setSession(JToggl newSession) {
        if(jToggl == null) {
            jToggl = newSession;
            refreshUser();
            this.workHours = propsLogic.loadProps(FilePath.getCurrentUserWorkhours());
        }
        else {
            //already logged in!
        }
    }

    public void addListener(DataLoadedListener listener) {
        listeners.add(listener);
    }

    public void notifyDataLoaded(Data e) {
        for(DataLoadedListener l: listeners) {
            l.dataLoaded(e);
        }
    }

    public static void terminateSession() {
        jToggl = null;
    }

    public List<TimeEntry> getTimeEntries() {
        return timeEntries;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public List<Workspace> getWorkspaces() {
        return workspaces;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public User getUser() {
        return user;
    }

    public void refreshUser() {
        user = jToggl.getCurrentUser();
        this.notifyDataLoaded(Data.USER);
    }

    public void refreshTimeEntries() {
        Calendar cal = Calendar.getInstance();
        cal.set(2019, Calendar.JANUARY, 1);
        Date start = cal.getTime();
        cal.set(2019, Calendar.DECEMBER, 31); //TODO: this is a temporary fix
        Date end = cal.getTime();
        timeEntries = jToggl.getTimeEntries(start, end);
        this.notifyDataLoaded(Data.TIME_ENTRIES);
    }

    public void refreshProjects() {
        projects = jToggl.getProjects();
        this.notifyDataLoaded(Data.PROJECTS);
    }

    public void refreshWorkspaces() {
        workspaces = jToggl.getWorkspaces();
        this.notifyDataLoaded(Data.WORKSPACES);
    }

    public void refreshTasks() {
        tasks = jToggl.getTasks();
        this.notifyDataLoaded(Data.TASKS);
    }

    public void refreshTimeData() {
        propsLogic.loadProps(FilePath.getCurrentUserWorkhours());
        this.refreshTimeEntries();
        this.refreshProjects();
        this.refreshWorkspaces();
        this.refreshTasks();
    }

    public Properties getWorkHours() {
        this.workHours = propsLogic.loadProps(FilePath.getCurrentUserWorkhours());
        return workHours;
    }
}
