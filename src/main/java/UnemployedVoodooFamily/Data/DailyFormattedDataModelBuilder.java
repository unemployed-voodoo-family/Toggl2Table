package UnemployedVoodooFamily.Data;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Logic.FileLogic;
import UnemployedVoodooFamily.Logic.Session;
import ch.simas.jtoggl.TimeEntry;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DailyFormattedDataModelBuilder {
    private DayOfWeek weekDay;
    private Double workedHours;
    private Double supposedHours;
    private Double overtime;
    private LocalDate day;
    private FileLogic propsLogic = new FileLogic();

    private List<TimeEntry> dailyTimeEntries = new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private List<WorkHours> workHours = propsLogic.loadJson(FilePath.getCurrentUserWorkhours());

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

    public DailyFormattedDataModel build() {
        Double workedSeconds = sumDurations();
        this.weekDay = this.day.getDayOfWeek();
        this.supposedHours = findSupposedHours();
        this.workedHours = (workedSeconds % 86400) / 3600;
        return new DailyFormattedDataModel(this.workedHours, this.supposedHours, this.day);
    }

    private Double findSupposedHours() {
        this.supposedHours = 0.0;
        for(WorkHours key: workHours) {
            if(DateRange.of(key.getFrom(), key.getTo()).contains(this.day)) {
                return key.getHours();
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
}