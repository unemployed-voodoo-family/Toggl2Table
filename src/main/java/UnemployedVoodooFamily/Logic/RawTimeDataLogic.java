package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.RawTimeDataModel;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.TimeEntry;
import ch.simas.jtoggl.Workspace;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class RawTimeDataLogic {
    // and is responsible for handling raw time data
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd. LLLL yyyy");
    private static ObservableList<RawTimeDataModel> masterData;
    private ObservableList<RawTimeDataModel> filteredData;

    private List<TimeEntry> masterTimeEntries;
    private List<TimeEntry> filteredList;

    /**
     * Build an observable list with RawTimeDataModel, using time entries imported from Toggl.
     * @return the ObservableList
     */
    public <T> ObservableList<RawTimeDataModel> buildObservableRawTimeData(Set<T> excludedData) {
        Session session = Session.getInstance();
        ObservableList<RawTimeDataModel> data = FXCollections.observableArrayList();
        this.masterTimeEntries = session.getTimeEntries();

        List<Project> projects = session.getProjects();
        List<Workspace> workspaces = session.getWorkspaces();

        //fill timeentries with workspace and proejct objects
        for(TimeEntry timeEntry: masterTimeEntries) {
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
        }

        //filter timeentries
        filteredList = new ArrayList<>(masterTimeEntries);
        if(! excludedData.isEmpty()) {
            List<TimeEntry> excludedEntries;
             excludedEntries = filteredList.stream().filter(timeEntry -> excludedData
                    .contains(timeEntry.getWorkspace()) || excludedData.contains(timeEntry.getProject()))
                                                         .collect(Collectors.toList());
            filteredList.removeAll(excludedEntries);
        }

        Iterator<TimeEntry> it = filteredList.iterator();
        while(it.hasNext()) {
            TimeEntry timeEntry = it.next();
            String description = timeEntry.getDescription();
            LocalDateTime start = timeEntry.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime stop = timeEntry.getStop().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            String startDate = start.toLocalDate().format(dateFormatter);
            String stopDate = stop.toLocalDate().format(dateFormatter);
            String startTime = start.toLocalTime().toString();
            String stopTime = stop.toLocalTime().toString();
            long duration = timeEntry.getDuration();
            String durationStr = LocalTime.MIN.plusSeconds(duration).format(DateTimeFormatter.ISO_LOCAL_TIME);
            Long pid = timeEntry.getPid();
            String projectName = "";
            for(Project project: projects) {
                if(project.getId().equals(pid)) {
                    projectName = project.getName();
                    break;
                }
            }

            RawTimeDataModel dataModel = new RawTimeDataModel(projectName, description, startDate, startTime, stopDate,
                                                              stopTime, String.valueOf(durationStr));
            data.add(dataModel);
        }
        masterData = data;
        return data;
    }

    public String getDataStartTime() {
        if(!masterData.isEmpty()) {
            return masterData.get(0).getStartDate();
        }
        return null;
    }

    public String getDataEndTime() {
        if(!masterData.isEmpty()) {
            return masterData.get(masterData.size() -1).getEndDate();
        }
        return null;
    }

    public List<TimeEntry> getFilteredTimeEntries() {
        return this.filteredList;
    }
}
