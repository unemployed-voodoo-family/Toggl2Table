package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.RawTimeDataModel;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.TimeEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

public class RawTimeDataLogic {
    // and is responsible for handling raw time data
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd. LLLL yyyy");
    private static ObservableList<RawTimeDataModel> masterData;
    private ObservableList<RawTimeDataModel> filteredData;
    /**
     * Build an observable list with RawTimeDataModel, using time entries imported from Toggl.
     * @return the ObservableList
     */
    public ObservableList<RawTimeDataModel> buildObservableRawTimeData() {
        Session session = Session.getInstance();
        ObservableList<RawTimeDataModel> data = FXCollections.observableArrayList();
        Iterator<TimeEntry> it = session.getTimeEntries().iterator();
        List<Project> projects = session.getProjects();

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
        return masterData.get(0).getStartDate();
    }

    public String getDataEndTime() {
        return masterData.get(masterData.size()-1).getEndDate();
    }
}
