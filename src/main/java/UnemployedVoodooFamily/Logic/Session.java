package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.Enums.Data;
import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Logic.Listeners.DataLoadListener;
import ch.simas.jtoggl.*;

import java.time.*;
import java.util.*;

public class Session {

    private static JToggl jToggl = null;
    private static List<TimeEntry> timeEntries;
    private static LinkedHashMap<Long, Workspace> workspaces;
    private static HashMap<Long, Project> projects;
    private static HashMap<Long, Task> tasks;
    private static HashMap<Long, Client> clients;
    private static User user;

    private static ZoneId zoneId;
    private static ZoneOffset zoneOffset;

    private static Properties workHours;
    private static FileLogic propsLogic;

    private static Session togglSession = new Session();

    private static List<DataLoadListener> loadListeners = new ArrayList<>();


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
        user = null;
        projects = null;
        tasks = null;
        clients = null;
        workspaces = null;
        timeEntries = null;
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


    /**
     * Fetch all the time-entries from the given period
     */
    public void refreshTimeEntries() {
        // fetch time entries
        fetchTimeEntries();
        this.notifyDataLoaded(Data.TIME_ENTRIES);
    }

    private void fetchTimeEntries() {
        OffsetDateTime start = OffsetDateTime.of(2007, 1, 1, 0, 0, 0, 0, zoneOffset);
        OffsetDateTime end = OffsetDateTime.of(LocalDate.now().getYear(), 12, 31, 0, 0, 0, 0, zoneOffset);
        List<TimeEntry> fetchedEntries = jToggl.getTimeEntries(start, end);
        this.timeEntries = fetchedEntries;

        // if the fetched time-entries size is over Toggl's limit of 1000
        // time entries per request, this will keep fetching until we have
        // all the time entries for the given period
        boolean finished = false;
        while(! finished) {
            if(fetchedEntries.size() > 999) {

                // fetch from new start date
                OffsetDateTime newStart = fetchedEntries.get(fetchedEntries.size() - 1).getStart();
                fetchedEntries = jToggl.getTimeEntries(newStart, end);

                //remove eventual duplicate time entry
                int duplicateCheckIndex = timeEntries.indexOf(fetchedEntries.get(0));
                if(! fetchedEntries.isEmpty() && duplicateCheckIndex != - 1) {
                    timeEntries.remove(duplicateCheckIndex);
                }
                timeEntries.addAll(fetchedEntries);

            }
            else {
                finished = true;
            }
        }
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

}