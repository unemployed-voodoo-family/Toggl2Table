package UnemployedVoodooFamily.Data;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Logic.PropertiesLogic;
import UnemployedVoodooFamily.Logic.Session;
import ch.simas.jtoggl.TimeEntry;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class WeeklyFormattedTimeDataModelBuilder {
    private String weekDay;
    private Double workedHours;
    private Double supposedHours;
    private Double overtime;
    private PropertiesLogic propsLogic = new PropertiesLogic();

    private List<TimeEntry> weeklyTimeEntries = new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public WeeklyFormattedTimeDataModelBuilder() {

    }

    public WeeklyFormattedTimeDataModelBuilder addTimeEntry(TimeEntry timeEntry) {
        weeklyTimeEntries.add(timeEntry);
        return this;
    }

    public WeeklyFormattedTimeDataModelBuilder setTimeEntries(List<TimeEntry> timeEntries) {
        this.weeklyTimeEntries = timeEntries;
        return this;
    }

    public WeeklyFormattedTimeDataModel build() {
        Properties workHours = Session.getInstance().getWorkHours();
        Iterator<String> it = workHours.stringPropertyNames().iterator();
        Map<DateRange, Double> ranges = new HashMap<>();

        LocalDate date = Instant.ofEpochMilli(weeklyTimeEntries.get(0).getStart().getTime())
                                .atZone(ZoneId.systemDefault()).toLocalDate();
        this.weekDay = date.getDayOfWeek().name();

        this.supposedHours = 0.0;
        while(it.hasNext()) {
            String key = it.next();
            if(DateRange.ofString(key, formatter).contains(date)) {
                this.supposedHours = Double.parseDouble(workHours.getProperty(key));
                break;
            }
        }
        Double workedSeconds = 0.0;
        for(TimeEntry t : weeklyTimeEntries) {
            workedSeconds += t.getDuration();
        }


        this.workedHours = (workedSeconds % 86400) / 3600;
        this.overtime = workedHours - supposedHours;

        return new WeeklyFormattedTimeDataModel(this.weekDay, this.workedHours, this.supposedHours, this.overtime);
    }
}
