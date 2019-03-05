package UnemployedVoodooFamily.Data;

import javafx.beans.property.SimpleStringProperty;

public class WeeklyFormattedTimeDataModel {

    private SimpleStringProperty weekDay;
    private SimpleStringProperty date;
    private SimpleStringProperty project;
    private SimpleStringProperty startTime;
    private SimpleStringProperty endTime;
    private SimpleStringProperty workedHours;
    private SimpleStringProperty supposedHours;
    private SimpleStringProperty overtime;

    /**
     * Creates a WeeklyTimeDataModel object
     * Used to structure data for the corresponding TableView
     * @param weekDay String with weekday
     * @param date String with date
     * @param project String with project name
     * @param startTime String with start time
     * @param endTime String with end time
     * @param workedHours String with hours worked
     * @param supposedHours String with supposed work hours
     * @param overtime String with overtime
     */
    public WeeklyFormattedTimeDataModel(String weekDay, String date, String project, String startTime, String endTime,
                                        String workedHours, String supposedHours, String overtime) {
        this.weekDay = new SimpleStringProperty(weekDay);
        this.date = new SimpleStringProperty(date);
        this.project = new SimpleStringProperty(project);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.workedHours = new SimpleStringProperty(workedHours);
        this.supposedHours = new SimpleStringProperty(supposedHours);
        this.overtime = new SimpleStringProperty(overtime);
    }

    /**
     * Returns the weekday
     * @return the weekday
     */
    public String getWeekDay() {
        return weekDay.get();
    }

    /**
     * Returns the date as a string
     * @return the date as a string
     */
    public String getDate() {
        return date.get();
    }

    /**
     * Returns the project name
     * @return the project name
     */
    public String getProject() {
        return project.get();
    }

    /**
     * Returns the start time as a string
     * @return the start time as a string
     */
    public String getStartTime() {
        return startTime.get();
    }

    /**
     * Returns the end time as a string
     * @return the end time as a string
     */
    public String getEndTime() {
        return endTime.get();
    }

    /**
     * Returns the amount worked as a string
     * @return the amount worked as a string
     */
    public String getWorkedHours() {
        return workedHours.get();
    }

    /**
     * Returns the supposed amount worked as a string
     * @return the supposed amount worked as a string
     */
    public String getSupposedHours() {
        return supposedHours.get();
    }

    /**
     * Returns the amount of overtime worked as a string
     * @return the amount of overtime worked as a string
     */
    public String getOvertime() {
        return overtime.get();
    }
}
