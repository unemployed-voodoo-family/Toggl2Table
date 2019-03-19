package UnemployedVoodooFamily.Data;

import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class MonthlySheetRowEntry {

    private int weekNumber;
    private DayOfWeek weekDay;
    private String date;
    private double supposedWorkHours;
    private double actualWorkedHours;

    public MonthlySheetRowEntry(int weekNumber, DayOfWeek weekDay, LocalDate date, double supposedWorkHours,
                                double actualWorkedHours) {
        this.weekNumber = weekNumber;
        this.weekDay = weekDay;
        this.date = date.toString();
        this.supposedWorkHours = supposedWorkHours;
        this.actualWorkedHours = actualWorkedHours;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public String getWeekDay() {
        return StringUtils.capitalize(weekDay.toString().toLowerCase());
    }

    public String getDate() {
        return date;
    }

    public double getSupposedWorkHours() {
        return supposedWorkHours;
    }

    public double getActualWorkedHours() {
        return actualWorkedHours;
    }
}
