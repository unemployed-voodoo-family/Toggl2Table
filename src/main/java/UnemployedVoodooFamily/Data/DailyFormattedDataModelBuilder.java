package UnemployedVoodooFamily.Data;

import UnemployedVoodooFamily.Logic.PropertiesLogic;
import UnemployedVoodooFamily.Logic.Session;
import ch.simas.jtoggl.TimeEntry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DailyFormattedDataModelBuilder {
    private String weekDay;
    private Double workedHours;
    private Double supposedHours;
    private Double overtime;
    private LocalDate day;
    private PropertiesLogic propsLogic = new PropertiesLogic();

    private List<TimeEntry> weeklyTimeEntries = new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private Properties workHours = Session.getInstance().getWorkHours();

    public DailyFormattedDataModelBuilder(LocalDate day) {
        this.day = day;
    }

    public DailyFormattedDataModelBuilder addTimeEntry(TimeEntry timeEntry) {
        weeklyTimeEntries.add(timeEntry);
        return this;
    }

    public DailyFormattedDataModelBuilder setTimeEntries(List<TimeEntry> timeEntries) {
        this.weeklyTimeEntries = timeEntries;
        return this;
    }

    public DailyFormattedDataModelBuilder setDay(LocalDate day) {
        return this;
    }

    public DailyFormattedDataModel build() {
        Double workedSeconds = sumDurations();
        this.weekDay = this.day.getDayOfWeek().name();
        this.supposedHours = findSupposedHours();
        this.workedHours = (workedSeconds % 86400) / 3600; //TODO: round the deciaml
        this.overtime = workedHours - supposedHours;
        return new DailyFormattedDataModel(this.weekDay, this.workedHours, this.supposedHours, this.overtime, this.day);
    }

    private Double findSupposedHours() {
        this.supposedHours = 0.0;
        for(String key: workHours.stringPropertyNames()) {
            if(DateRange.ofString(key, formatter).contains(this.day)) {
                return Double.parseDouble(workHours.getProperty(key));
            }
        }
        return 7.5; //TODO: get default value from somewhere
    }

    private Double sumDurations() {
        double workedSeconds = 0.0;
        for(TimeEntry t: weeklyTimeEntries) {
            workedSeconds += t.getDuration();
        }
        return workedSeconds;
    }
}
