package UnemployedVoodooFamily.Data;

import UnemployedVoodooFamily.Logic.Session;
import ch.simas.jtoggl.TimeEntry;
import javafx.beans.property.SimpleStringProperty;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    private List<DailyFormattedDataModel> monthlyTimeEntries = new ArrayList<>();


    public WeeklyFormattedDataModelBuilder(int weekNumber) {
        this.weekNumber = weekNumber;

    }

    public WeeklyFormattedDataModelBuilder addDailyData(DailyFormattedDataModel dailyDataModel) {
        monthlyTimeEntries.add(dailyDataModel);
        return this;
    }

    public WeeklyFormattedDataModel build() {
        calculateAndSetHours();
        this.overtime = this.workedHours - this.supposedHours;
        return new WeeklyFormattedDataModel(this.weekNumber, this.workedHours, this.supposedHours, this.overtime);
    }

    private void calculateAndSetHours() {
        this.supposedHours = 0.0;
        this.workedHours = 0.0;
        for(DailyFormattedDataModel entry: monthlyTimeEntries) {
            DayOfWeek day = entry.getDay().getDayOfWeek();
            if(day.equals(DayOfWeek.SUNDAY) || day.equals(DayOfWeek.SATURDAY)) {
            }
            else {
                this.supposedHours += entry.getSupposedHours();
            }
            this.workedHours += entry.getWorkedHours();
            //TODO: Exclude saturdays, sundays, maybe holidays
        }
    }
}
