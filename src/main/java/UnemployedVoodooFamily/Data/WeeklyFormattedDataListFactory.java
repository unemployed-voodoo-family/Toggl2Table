package UnemployedVoodooFamily.Data;

import UnemployedVoodooFamily.Logic.PropertiesLogic;
import UnemployedVoodooFamily.Logic.Session;
import ch.simas.jtoggl.TimeEntry;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class WeeklyFormattedDataListFactory {
    /*
    private DayOfWeek weekDay;
    private Double workedHours;
    private Double supposedHours;
    private Double overtime;
    private LocalDate day;
    private String note = "oppsie woopsie";
    private PropertiesLogic propsLogic = new PropertiesLogic();

    private List<TimeEntry> dailyTimeEntries = new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private Properties workHours = Session.getInstance().getWorkHours();

     */

    private ArrayList<DailyFormattedDataModel> weeklyList;

    public List<DailyFormattedDataModel> buildWeeklyDataList(List<TimeEntry> timeEntries, int weekNumber, int year)   {
        weeklyList = new ArrayList<>();
        setupWeeklyList(weekNumber, year);
        populateWeeklyList();
        int weeklyEntries = 0;
        return weeklyList;
    }

    private void setupWeeklyList(int weekNumber, int year)    {
        LocalDate weeksFirstDate = LocalDate.ofYearDay(year, 50)
                                            .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, weekNumber)
                                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        for(DayOfWeek weekday : DayOfWeek.values()) {
            LocalDate date = weeksFirstDate.plusDays(weekday.getValue()-1);
            weeklyList.add(new DailyFormattedDataModel(0.00,0.00, date, ""));
        }
    }

    private void populateWeeklyList()   {

    }

    /*
    public WeeklyFormattedDataListFactory(LocalDate day) {
        this.day = day;
    }

    public WeeklyFormattedDataListFactory addTimeEntry(TimeEntry timeEntry) {
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
        return new DailyFormattedDataModel(this.workedHours, this.supposedHours, this.day, this.note);
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

     */
}
