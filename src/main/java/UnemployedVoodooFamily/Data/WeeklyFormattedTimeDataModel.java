package UnemployedVoodooFamily.Data;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class WeeklyFormattedTimeDataModel {

    private SimpleStringProperty weekDay;
    private SimpleDoubleProperty workedHours;
    private SimpleDoubleProperty supposedHours;
    private SimpleDoubleProperty overtime;

    /**
     * Creates a WeeklyTimeDataModel object
     * Used to structure data for the corresponding TableView
     * @param weekDay       String with weekday
     * @param date          String with date
     * @param project       String with project name
     * @param startTime     String with start time
     * @param endTime       String with end time
     * @param workedHours   String with hours worked
     * @param supposedHours String with supposed work hours
     * @param overtime      String with overtime
     */
    public WeeklyFormattedTimeDataModel(String weekDay, Double workedHours, Double supposedHours,
                                        Double overtime) {
        this.weekDay = new SimpleStringProperty(weekDay);
        this.workedHours = new SimpleDoubleProperty(workedHours);
        this.supposedHours = new SimpleDoubleProperty(supposedHours);
        this.overtime = new SimpleDoubleProperty(overtime);
    }

    /**
     * Returns the weekday
     * @return the weekday
     */
    public String getWeekDay() {
        return weekDay.get();
    }


    /**
     * Returns the amount worked as a string
     * @return the amount worked as a string
     */
    public Double getWorkedHours() {
        return workedHours.get();
    }

    /**
     * Returns the supposed amount worked as a string
     * @return the supposed amount worked as a string
     */
    public Double getSupposedHours() {
        return supposedHours.get();
    }

    /**
     * Returns the amount of overtime worked as a string
     * @return the amount of overtime worked as a string
     */
    public Double getOvertime() {
        return overtime.get();
    }
}
