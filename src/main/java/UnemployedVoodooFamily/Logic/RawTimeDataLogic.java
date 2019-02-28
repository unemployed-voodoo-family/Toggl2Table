package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.RawTimeDataModel;
import ch.simas.jtoggl.JToggl;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.TimeEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class RawTimeDataLogic {
    //TODO This class should be called to from the TableViewController
    // and is responsible for handling raw time data

    public ObservableList<RawTimeDataModel> buildObservableRawTimeData() {
        JToggl jToggl = Session.getInstance().getSession();

        ObservableList<RawTimeDataModel> data = FXCollections.observableArrayList();
        Iterator<TimeEntry> it = jToggl.getTimeEntries().iterator();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        while(it.hasNext()) {
            TimeEntry timeEntry = it.next();

            Project project = timeEntry.getProject(); //TODO: BUG: project is always null ? ? ?
            String projectName = "";
            if(project != null) {
                projectName = project.getName();
            }
            String description = timeEntry.getDescription();
            LocalDateTime start = timeEntry.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime stop = timeEntry.getStop().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            String startDate = start.toLocalDate().format(dateFormatter);
            String stopDate = stop.toLocalDate().format(dateFormatter);
            String startTime = start.toLocalTime().toString();
            String stopTime = stop.toLocalTime().toString();
            long duration = timeEntry.getDuration();

            RawTimeDataModel dataModel = new RawTimeDataModel(projectName, description, startDate, startTime, stopDate,
                                                              stopTime, String.valueOf(duration));
            data.add(dataModel);
        }
        return data;
    }
}
