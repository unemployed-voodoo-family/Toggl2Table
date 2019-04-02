package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.RawTimeDataModel;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.TimeEntry;
import ch.simas.jtoggl.Workspace;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class RawTimeDataLogic {
    // and is responsible for handling raw time data
    //TODO replace ObservableLists with ArrayLists
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd. LLLL yyyy");
    private static List<RawTimeDataModel> masterData = new ArrayList<>();
    private ObservableList<RawTimeDataModel> filteredData;

    private LocalDate filterStartDate = LocalDate.now().minusWeeks(1);
    private LocalDate filterEndDate = LocalDate.now();

    private List<TimeEntry> masterTimeEntries;
    private List<TimeEntry> filteredList;

    /**
     * Build an observable list with RawTimeDataModel, using time entries imported from Toggl.
     * @return the ObservableList
     */
    public <T> ObservableList<RawTimeDataModel> buildObservableRawTimeData(List<TimeEntry> timeEntries,
                                                                           List<Project> projects,
                                                                           List<Workspace> workspaces,
                                                                           Set<T> excludedData) {

        ObservableList<RawTimeDataModel> data = FXCollections.observableArrayList();
        this.masterTimeEntries = timeEntries;

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

        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss");
        Iterator<TimeEntry> it = filteredList.iterator();
        while(it.hasNext()) {
            TimeEntry timeEntry = it.next();
            String description = timeEntry.getDescription();
            LocalDateTime start = timeEntry.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime stop = timeEntry.getStop().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            if(start.toLocalDate().isAfter(getFilteredDataStartDate())
                    && (stop.toLocalDate().isBefore(getFilteredDataEndDate()) || stop.toLocalDate().isEqual(getFilteredDataEndDate()))) {
                String startDate = start.toLocalDate().format(dateFormatter);
                String stopDate = stop.toLocalDate().format(dateFormatter);
                String startTime = start.toLocalTime().format(df);
                String stopTime = stop.toLocalTime().format(df);
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

                RawTimeDataModel dataModel = new RawTimeDataModel(projectName, description, startDate, startTime,
                                                                  stopDate, stopTime, durationStr);
                data.add(dataModel);
            }
        }
        masterData = data;
        return data;
    }

    public LocalDate getFilteredDataStartDate() {
        return filterStartDate;
    }

    public LocalDate getFilteredDataEndDate() {
        return filterEndDate;
    }

    public void setDataStartDate(LocalDate date)    {
        this.filterStartDate = date;
        System.out.println("New raw start date is:" + filterStartDate.toString());
    }

    public void setDataEndDate(LocalDate date)  {
        this.filterEndDate = date;
        System.out.println("New raw end date is:" + filterEndDate.toString());
    }

    public List<TimeEntry> getFilteredTimeEntries() {
        return this.filteredList;
    }

    public List<TimeEntry> getMasterTimeEntries() {
        return masterTimeEntries;
    }
}
