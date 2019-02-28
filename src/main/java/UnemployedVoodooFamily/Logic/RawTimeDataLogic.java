package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.DateRange;
import UnemployedVoodooFamily.Data.RawTimeDataModel;
import ch.simas.jtoggl.JToggl;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.TimeEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.http.impl.cookie.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RawTimeDataLogic {
    //TODO This class should be called to from the TableViewController
    // and is responsible for handling raw time data

    public ObservableList<RawTimeDataModel> buildObservableRawTimeData() {
        JToggl jToggl = Session.getInstance().getSession();

        ObservableList<RawTimeDataModel> data = FXCollections.observableArrayList();
        Iterator<TimeEntry> it = jToggl.getTimeEntries().iterator();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        List<Project> projects = jToggl.getProjects();

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
        return data;
    }
}
