package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.Enums.Data;
import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Logic.Listeners.DataLoadListener;
import ch.simas.jtoggl.*;

import java.time.*;
import java.util.*;

public class Session {

    private static JToggl jToggl = null;
    private List<TimeEntry> timeEntries;
    private LinkedHashMap<Long, Workspace> workspaces;
    private HashMap<Long, Project> projects;
    private HashMap<Long, Task> tasks;
    private HashMap<Long, Client> clients;
    private User user;

    private ZoneId zoneId;
    private ZoneOffset zoneOffset;

    private Properties workHours;
    private FileLogic propsLogic;

    private static Session togglSession = new Session();

    private List<DataLoadListener> loadListeners = new ArrayList<>();

    private Session() {
        this.propsLogic = new FileLogic();
    }

    synchronized public static Session getInstance() {
        return togglSession;
    }

    public void setSession(JToggl newSession) {
        if(jToggl == null) {
            jToggl = newSession;
            refreshUser();
            this.zoneId = ZoneId.of(user.getTimeZone());
            zoneOffset = zoneId.getRules().getOffset(Instant.now());
            this.workHours = propsLogic.loadProps(FilePath.getCurrentUserWorkhours());
        }
        else {
            //already logged in!
        }
    }

    public void addListener(DataLoadListener listener) {
        loadListeners.add(listener);
    }

    public void notifyDataLoaded(Data e) {
        for(DataLoadListener l: loadListeners) {
            l.dataLoaded(e);
        }
    }

    public static void terminateSession() {
        jToggl = null;
    }

    public List<TimeEntry> getTimeEntries() {
        return timeEntries;
    }

    public HashMap<Long, Project> getProjects() {
        return projects;
    }

    public LinkedHashMap<Long, Workspace> getWorkspaces() {
        return workspaces;
    }

    public HashMap<Long, Task> getTasks() {
        return tasks;
    }

    public HashMap<Long, Client> getClients() {
        return clients;
    }

    public User getUser() {
        return user;
    }

    public ZoneOffset getZoneOffset() {
        return zoneOffset;
    }

    public void refreshTimeEntries() {
        //TODO - Implement reports api instead.
        //TODO - Get timezone from toggl user
        OffsetDateTime start = OffsetDateTime.of(2019, 1, 1, 0,0,0,0, ZoneOffset.ofHours(1));
        OffsetDateTime end = OffsetDateTime.of(2019, 12, 31, 0,0,0,0, ZoneOffset.ofHours(1));
        timeEntries = jToggl.getTimeEntries(start, end);
        this.notifyDataLoaded(Data.TIME_ENTRIES);

    }

    public void refreshUser() {
        this.user = jToggl.getCurrentUser();
        this.notifyDataLoaded(Data.USER);
    }

    public void refreshClient() {
        this.clients = jToggl.getClients();
        this.notifyDataLoaded(Data.CLIENT);
    }

    public void refreshProjects() {
        this.projects = jToggl.getProjects();
        this.notifyDataLoaded(Data.PROJECTS);

    }

    public void refreshWorkspaces() {
        this.workspaces = jToggl.getWorkspaces();
        this.notifyDataLoaded(Data.WORKSPACES);

    }

    public void refreshTasks() {
        this.tasks = jToggl.getTasks();
        this.notifyDataLoaded(Data.TASKS);

    }

    public void refreshWorkHours() {
        this.workHours = propsLogic.loadProps(FilePath.getCurrentUserWorkhours());
        this.notifyDataLoaded(Data.WORKHOURS);

    }

    public Properties getWorkHours() {
        refreshWorkHours();
        return workHours;
    }

    public void refreshClients() {

    }

    public void refreshReport() {
        /*PagedResult detailedReport = jToggl.getDetailedReport((PagedReportsParameter) new PagedReportsParameter(workspace.getId(), "jtoggl-integration-test")
                .setSince("2011-11-15")
                .setUntil("2011-11-15")
                .setProjectIds(Collections.singleton(project.getId())));*/
    }


    public void refreshTimeData() {
        this.refreshWorkHours();
        this.refreshTimeEntries();
        this.refreshProjects();
        this.refreshWorkspaces();
        this.refreshTasks();
    }
}