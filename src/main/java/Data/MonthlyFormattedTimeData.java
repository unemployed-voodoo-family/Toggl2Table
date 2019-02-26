package Data;

import javafx.beans.property.SimpleStringProperty;

public class MonthlyFormattedTimeData {

    private SimpleStringProperty month;
    private SimpleStringProperty weekNumber;
    private SimpleStringProperty workedHours;
    private SimpleStringProperty supposedHours;
    private SimpleStringProperty overtime;

    /**
     * Creates a MonthlyTimeDataModel object
     * Used to structure data for the corresponding TableView
     * @param month String with month
     * @param weekNumber String with week number
     * @param workedHours String with worked hours
     * @param supposedHours String with supposed work hours
     * @param overtime String with the amount of overtime
     */
    public MonthlyFormattedTimeData(String month, String weekNumber, String workedHours, String supposedHours, String overtime) {
        this.month = new SimpleStringProperty(month);
        this.weekNumber = new SimpleStringProperty(weekNumber);
        this.workedHours = new SimpleStringProperty(workedHours);
        this.supposedHours = new SimpleStringProperty(supposedHours);
        this.overtime = new SimpleStringProperty(overtime);
    }

    /**
     * Returns the month as a string
     * @return the month as a string
     */
    public String getMonth() {
        return month.get();
    }

    /**
     * Returns the week number as a string
     * @return the week number as a string
     */
    public String getWeekNumber() {
        return weekNumber.get();
    }

    /**
     * Returns the amount of hours worked as a string
     * @return the amount of hours worked as a string
     */
    public String getWorkedHours() {
        return workedHours.get();
    }

    /**
     * Returns the supposed amount of hours worked as a string
     * @return the supposed amount of hours worked as a string
     */
    public String getSupposedHours() {
        return supposedHours.get();
    }

    /**
     * Returns the amount of overtime worked
     * @return the amount of overtime worked
     */
    public String getOvertime() {
        return overtime.get();
    }

}
