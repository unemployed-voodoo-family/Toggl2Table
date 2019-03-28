package UnemployedVoodooFamily.Data;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Logic.PropertiesLogic;
import UnemployedVoodooFamily.Logic.Session;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * Builder for tthe WeeklyFormattedDataModel
 * Builds the summary for one week for use in the monthly table
 * Assumes all supplied data is from the same week.
 */
public class WeeklyFormattedDataModelBuilder {

    private int weekNumber;
    private Double workedHours;
    private Double supposedHours;
    private Double overtime;
    private LocalDate firstDateOfWeek;

    private Integer year;

    private final Double STANDARD_WORK_HOURS = 7.5;

    PropertiesLogic propertiesLogic = new PropertiesLogic();

    private Map<DayOfWeek, DailyFormattedDataModel> weeklyTimeEntries = new HashMap<>();


    public WeeklyFormattedDataModelBuilder(LocalDate firstDateOfWeek) {
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = firstDateOfWeek.get(woy);
        this.firstDateOfWeek = firstDateOfWeek;
        this.weekNumber = weekNumber;
    }

    public WeeklyFormattedDataModelBuilder addDailyData(DailyFormattedDataModel dailyDataModel) {
        weeklyTimeEntries.put(dailyDataModel.getDay().getDayOfWeek(), dailyDataModel);
        return this;
    }

    public WeeklyFormattedDataModel build() {
        if(weeklyTimeEntries.size() < 7) {
            for(DayOfWeek dayOfWeek: DayOfWeek.values()) {
                System.out.println(dayOfWeek.getValue());
                weeklyTimeEntries.putIfAbsent(dayOfWeek, new DailyFormattedDataModelBuilder(
                        firstDateOfWeek.plusDays(dayOfWeek.getValue() - 1)).build());
            }
        }
        calculateAndSetHours();
        this.overtime = this.workedHours - this.supposedHours;
        return new WeeklyFormattedDataModel(this.weekNumber, this.workedHours, this.supposedHours, this.overtime);
    }

    private void calculateAndSetHours() {
        this.supposedHours = 0.0;
        this.workedHours = 0.0;
        for(DailyFormattedDataModel entry: weeklyTimeEntries.values()) {
            DayOfWeek day = entry.getDay().getDayOfWeek();
            if(day.equals(DayOfWeek.SUNDAY) || day.equals(DayOfWeek.SATURDAY)) {
            }
            else {
                this.supposedHours += entry.getSupposedHours();
            }
            this.workedHours += entry.getWorkedHours();
            //TODO: Exclude holidays
        }
    }
}
