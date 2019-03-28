package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.RawTimeDataModel;
import ch.simas.jtoggl.Client;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.TimeEntry;
import ch.simas.jtoggl.Workspace;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class RawTimeDataLogic {
    // and is responsible for handling raw time data
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd. LLLL yyyy");
    private static ArrayList<RawTimeDataModel> masterData;
    private ObservableList<RawTimeDataModel> filteredData;

    private List<TimeEntry> masterTimeEntries;
    private List<TimeEntry> filteredTimeEntries;

    /**
     * Build an observable list with RawTimeDataModel, using time entries imported from Toggl.
     * @return the ObservableList
     */
    public <T> List<RawTimeDataModel> buildRawMasterData(List<TimeEntry> timeEntries, Set<T> excludedData) {
        List<RawTimeDataModel> data = new ArrayList<>();
        this.masterTimeEntries = timeEntries;

        //fill timeentries with workspace and proejct objects
        /*for(TimeEntry timeEntry: masterTimeEntries) {
            for(Project project: projects) {
                if(project.getId().equals(timeEntry.getPid())) {
                    timeEntry.setProject(project);
                }
            }
            for(Workspace workspace: workspaces) {
                if(workspace.getId().equals(timeEntry.getWid())) {
                    timeEntry.setWorkspace(workspace);
                }
            }
        }*/
        Session session = Session.getInstance();
        assembleLooseData(session.getProjects(), session.getWorkspaces(), session.getClients(), timeEntries);

        //filter timeentries
        filteredTimeEntries = new ArrayList<>(masterTimeEntries);
        if(excludedData != null && !excludedData.isEmpty()) {
            List<TimeEntry> excludedEntries;
            excludedEntries = filteredTimeEntries.stream().filter(timeEntry -> excludedData
                    .contains(timeEntry.getWorkspace()) || excludedData.contains(timeEntry.getProject()))
                                                 .collect(Collectors.toList());
            filteredTimeEntries.removeAll(excludedEntries);
        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss");
        Iterator<TimeEntry> it = filteredTimeEntries.iterator();
        while(it.hasNext()) {
            TimeEntry timeEntry = it.next();
            String description = timeEntry.getDescription();
            OffsetDateTime start = timeEntry.getStart();
            OffsetDateTime stop = timeEntry.getStop();
            String startDate = start.toLocalDate().format(dateFormatter);
            String stopDate = stop.toLocalDate().format(dateFormatter);
            String startTime = start.toLocalTime().format(df);
            String stopTime = stop.toLocalTime().format(df);
            long duration = timeEntry.getDuration();
            String durationStr = LocalTime.MIN.plusSeconds(duration).format(DateTimeFormatter.ISO_LOCAL_TIME);
            String projectName = "";

            RawTimeDataModel dataModel = new RawTimeDataModel(projectName, description, startDate, startTime, stopDate,
                                                              stopTime, durationStr);
            data.add(dataModel);
        }
        return data;
    }

    private void assembleLooseData(Map<Long, Project> projects, Map<Long, Workspace> workspaces,
                                   Map<Long, Client> clients, List<TimeEntry> timeEntries) {

        for(Map.Entry<Long, Project> project: projects.entrySet()) {
            Project projectVal = project.getValue();
            projectVal.setClient(clients.get(projectVal.getCid()));
        }

        for(TimeEntry timeEntry: timeEntries) {
            timeEntry.setProject(projects.get(timeEntry.getPid()));
            timeEntry.setWorkspace(workspaces.get(timeEntry.getWid()));
        }
    }

    public String getDataStartTime() {
        if(! masterData.isEmpty()) {
            return masterData.get(0).getStartDate();
        }
        return null;
    }

    public String getDataEndTime() {
        if(! masterData.isEmpty()) {
            return masterData.get(masterData.size() - 1).getEndDate();
        }
        return null;
    }

    public List<TimeEntry> getFilteredTimeEntries() {
        return this.filteredTimeEntries;
    }

    public List<TimeEntry> getMasterTimeEntries() {
        return masterTimeEntries;
    }


}
