package UnemployedVoodooFamily.Data;

import UnemployedVoodooFamily.Enums.WeekDay;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

public class MonthlySheetRowEntry {

    private String weekNumber;
    private WeekDay weekDay;
    private String date;
    private double supposedWorkHours;
    private double actualWorkedHours;

    public MonthlySheetRowEntry(String weekNumber, WeekDay weekDay, LocalDate date, double supposedWorkHours,
                                double actualWorkedHours) {
        this.weekNumber = weekNumber;
        this.weekDay = weekDay;
        this.date = date.toString();
        this.supposedWorkHours = supposedWorkHours;
        this.actualWorkedHours = actualWorkedHours;
    }

    public String getWeekNumber() {
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
