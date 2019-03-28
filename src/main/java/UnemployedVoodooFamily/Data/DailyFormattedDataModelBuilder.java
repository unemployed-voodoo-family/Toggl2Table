package UnemployedVoodooFamily.Data;

import UnemployedVoodooFamily.Logic.PropertiesLogic;
import UnemployedVoodooFamily.Logic.Session;
import ch.simas.jtoggl.TimeEntry;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class DailyFormattedDataModelBuilder {
    private DayOfWeek weekDay;
    private Double workedHours;
    private Double supposedHours;
    private Double overtime;
    private LocalDate day;
    private PropertiesLogic propsLogic = new PropertiesLogic();

    private List<TimeEntry> dailyTimeEntries = new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private Properties workHours = Session.getInstance().getWorkHours();

    public DailyFormattedDataModelBuilder(LocalDate day) {
        this.day = day;
    }

    public DailyFormattedDataModelBuilder addTimeEntry(TimeEntry timeEntry) {
        if(timeEntry != null) {
            LocalDate startDate = timeEntry.getStart().toLocalDate();
            if(startDate != null && startDate.equals(day)) {
                dailyTimeEntries.add(timeEntry);
            }
        }
        return this;
    }

    public DailyFormattedDataModelBuilder setDay(LocalDate day) {
        return this;
    }

    public DailyFormattedDataModel build() {
        Double workedSeconds = sumDurations();
        this.weekDay = this.day.getDayOfWeek();
        this.supposedHours = findSupposedHours();
        this.workedHours = (workedSeconds % 86400) / 3600;
        this.overtime = workedHours - supposedHours;
        return new DailyFormattedDataModel(this.workedHours, this.supposedHours, this.overtime, this.day);
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
        for(TimeEntry t: dailyTimeEntries) {
            workedSeconds += t.getDuration();
        }
        return workedSeconds;
    }

    public DayOfWeek getWeekDay(){
        return this.weekDay;
    }
}
