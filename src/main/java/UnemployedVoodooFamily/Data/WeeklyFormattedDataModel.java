package UnemployedVoodooFamily.Data;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class WeeklyFormattedDataModel {

    private SimpleIntegerProperty weekNumber;
    private SimpleStringProperty weekday;
    private SimpleStringProperty date;
    private SimpleDoubleProperty workedHours;
    private SimpleDoubleProperty supposedHours;
    private SimpleDoubleProperty extraTime;
    private SimpleStringProperty note;
    private LocalDate firstDateOfWeek;

    /**
     * Creates a MonthlyTimeDataModel object
     * Used to structure data for the corresponding TableView
     * @param firstDateOfWeek First date of the week being formatted
     * @param workedHours String with worked hours
     * @param supposedHours String with supposed work hours
     * @param extraTime String with the amount of extraTime
     */
    public WeeklyFormattedDataModel(LocalDate firstDateOfWeek, Double workedHours, Double supposedHours,
                                    Double extraTime, LocalDate date, DayOfWeek weekday, String note) {

        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = firstDateOfWeek.get(woy);
        this.weekNumber = new SimpleIntegerProperty(weekNumber);
        this.workedHours = new SimpleDoubleProperty(workedHours);
        this.supposedHours = new SimpleDoubleProperty(supposedHours);
        this.extraTime = new SimpleDoubleProperty(extraTime);
        this.firstDateOfWeek = firstDateOfWeek;
        this.date = new SimpleStringProperty(date.toString());
        this.weekday = new SimpleStringProperty(weekday.toString());
        this.note = new SimpleStringProperty(note);
    }


    /**
     * Returns the week number
     * @return the week number
     */
    public Integer getWeekNumber() {
        return weekNumber.get();
    }

    /**
     * Returns the amount of hours worked
     * @return the amount of hours worked
     */
    public Double getWorkedHours() {
        return workedHours.get();
    }

    /**
     * Returns the supposed amount of hours worked
     * @return the supposed amount of hours worked
     */
    public Double getSupposedHours() {
        return supposedHours.get();
    }

    /**
     * Returns the amount of extraTime worked
     * @return the amount of extraTime worked
     */
    public Double getExtraTime() {
        return extraTime.get();
    }

    /**
     * Returns the weekday as a string
     * @return the weekday as a string
     */
    public String getWeekday() {
        return weekday.get();
    }

    /**
     * Returns the date as a string
     * @return the date as a string
     */
    public String getDate() {
        return date.get();
    }

    /**
     * Returns a string with a note
     * @return a string with a note
     */
    public String getNote() {
        return note.get();
    }

}
