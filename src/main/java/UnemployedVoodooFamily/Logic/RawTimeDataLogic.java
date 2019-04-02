package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.RawTimeDataModel;
import ch.simas.jtoggl.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Time;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RawTimeDataLogic {
    // and is responsible for handling raw time data
    //TODO replace ObservableLists with ArrayLists
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");

    private LocalDate filterStartDate = LocalDate.now().minusWeeks(1);
    private LocalDate filterEndDate = LocalDate.now();

    private List<TimeEntry> masterTimeEntries;
    private List<TimeEntry> filteredTimeEntries;
    private ZoneOffset zoneOffset = Session.getInstance().getZoneOffset();

    /**
     * Build an observable list with RawTimeDataModel, using time entries imported from Toggl.
     * @return the ObservableList
     */
    public <T> List<RawTimeDataModel> buildRawMasterData(List<TimeEntry> timeEntries, Map<Long, Project> projects,
                                                         Map<Long, Workspace> workspaces, Map<Long, Client> clients,
                                                         Set<T> excludedData) {

        List<RawTimeDataModel> data = new ArrayList<>();
        this.masterTimeEntries = timeEntries;

        //fill timeentries with workspace and proejct objects
        assembleLooseData(projects, workspaces, clients, timeEntries);

        //filter timeentries
        filteredTimeEntries = new LinkedList<>(masterTimeEntries);
        if(excludedData != null && ! excludedData.isEmpty()) {
            List<TimeEntry> excludedEntries;
            excludedEntries = filteredTimeEntries.stream().filter(timeEntry -> {
                boolean result = false;
                Project p = timeEntry.getProject();
                Client c = p == null ? null : p.getClient();
                Workspace w = timeEntry.getWorkspace();
                result = excludedData.stream()
                                     .anyMatch(entry -> entry.equals(w) || entry.equals(c) || entry.equals(p));

                return result;
            }).collect(Collectors.toList());
            filteredTimeEntries.removeAll(excludedEntries);
        }

        DateTimeFormatter durationFormatter = DateTimeFormatter.ofPattern("HH:mm");
        Iterator<TimeEntry> it = filteredTimeEntries.iterator();
        while(it.hasNext()) {
            TimeEntry timeEntry = it.next();
            String description = timeEntry.getDescription();
            OffsetDateTime start = timeEntry.getStart().withOffsetSameInstant(zoneOffset);
            OffsetDateTime stop;
            if(null != timeEntry.getStop()) {
                stop = timeEntry.getStop().withOffsetSameInstant(zoneOffset);
            }
            else {
                stop = OffsetDateTime.now();
            }

            if((start.toLocalDate().isAfter(getFilteredDataStartDate()) || start.toLocalDate().isEqual(getFilteredDataStartDate()))
                && (stop.toLocalDate().isBefore(getFilteredDataEndDate()) || stop.toLocalDate().isEqual(getFilteredDataEndDate()))){
                String startDate = start.toLocalDate().format(dateFormatter);
                String startTime = start.toLocalTime().format(durationFormatter);

                String stopDate = stop.toLocalTime().format(durationFormatter);
                String stopTime = stop.toLocalDate().format(dateFormatter);

                long duration = timeEntry.getDuration();
                String durationStr = LocalTime.MIN.plusSeconds(duration).format(DateTimeFormatter.ISO_LOCAL_TIME);

                Project project = timeEntry.getProject();
                String projectName = "";
                String clientStr = "";
                if(project != null) {
                    projectName = project.getName();
                    Client client = project.getClient();
                    if(client != null) {
                        clientStr = client.getName();
                    }
                }

                RawTimeDataModel dataModel = new RawTimeDataModel(projectName, clientStr, description, startDate,
                                                                  startTime, stopDate, stopTime, durationStr);
                data.add(dataModel);
            }
        }
        return data;
    }

    private void assembleLooseData(Map<Long, Project> projects, Map<Long, Workspace> workspaces,
                                   Map<Long, Client> clients, List<TimeEntry> timeEntries) {

        for(Map.Entry<Long, Project> project: projects.entrySet()) {
            Project projectVal = project.getValue();
            Long cid = projectVal.getCid();
            if(cid != null) {
                projectVal.setClient(clients.get(projectVal.getCid()));
            }
        }

        for(TimeEntry timeEntry: timeEntries) {
            Long pid = timeEntry.getPid();
            Long wid = timeEntry.getWid();
            if(pid != null) {
                timeEntry.setProject(projects.get(timeEntry.getPid()));
            }
            if(wid != null) {
                timeEntry.setWorkspace(workspaces.get(timeEntry.getWid()));
            }
        }
    }


    public LocalDate getFilteredDataStartDate() {
        if(! masterTimeEntries.isEmpty()) {
            return filterStartDate;
        }
        return null;
    }

    public LocalDate getFilteredDataEndDate() {
        if(! masterTimeEntries.isEmpty()) {
            return filterEndDate;
        }
        return null;
    }

    public void setDataStartDate(LocalDate date) {
        this.filterStartDate = date;
        System.out.println("New raw start date is:" + filterStartDate.toString());
    }

    public void setDataEndDate(LocalDate date) {
        this.filterEndDate = date;
        System.out.println("New raw end date is:" + filterEndDate.toString());
    }

    public List<TimeEntry> getFilteredTimeEntries() {
        return this.filteredTimeEntries;
    }

    public List<TimeEntry> getMasterTimeEntries() {
        return masterTimeEntries;
    }


}
