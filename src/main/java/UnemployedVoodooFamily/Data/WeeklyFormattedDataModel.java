package UnemployedVoodooFamily.Data;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class WeeklyFormattedDataModel {

    private SimpleIntegerProperty weekNumber;
    private SimpleDoubleProperty workedHours;
    private SimpleDoubleProperty supposedHours;
    private SimpleDoubleProperty overtime;
    private LocalDate firstDateOfWeek;

    /**
     * Creates a MonthlyTimeDataModel object
     * Used to structure data for the corresponding TableView
     * @param firstDateOfWeek First date of the week being formatted
     * @param workedHours String with worked hours
     * @param supposedHours String with supposed work hours
     * @param overtime String with the amount of overtime
     */
    public WeeklyFormattedDataModel(LocalDate firstDateOfWeek, Double workedHours, Double supposedHours, Double overtime) {

        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = firstDateOfWeek.get(woy);
        this.weekNumber = new SimpleIntegerProperty(weekNumber);
        this.workedHours = new SimpleDoubleProperty(workedHours);
        this.supposedHours = new SimpleDoubleProperty(supposedHours);
        this.overtime = new SimpleDoubleProperty(overtime);
        this.firstDateOfWeek = firstDateOfWeek;
    }


    /**
     * Returns the week number as a string
     * @return the week number as a string
     */
    public Integer getWeekNumber() {
        return weekNumber.get();
    }

    /**
     * Returns the amount of hours worked as a string
     * @return the amount of hours worked as a string
     */
    public Double getWorkedHours() {
        return workedHours.get();
    }

    /**
     * Returns the supposed amount of hours worked as a string
     * @return the supposed amount of hours worked as a string
     */
    public Double getSupposedHours() {
        return supposedHours.get();
    }

    /**
     * Returns the amount of overtime worked
     * @return the amount of overtime worked
     */
    public Double getOvertime() {
        return overtime.get();
    }

}
