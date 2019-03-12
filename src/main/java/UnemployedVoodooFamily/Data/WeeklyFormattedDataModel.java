package UnemployedVoodooFamily.Data;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class WeeklyFormattedDataModel {

    private SimpleIntegerProperty weekNumber;
    private SimpleDoubleProperty workedHours;
    private SimpleDoubleProperty supposedHours;
    private SimpleDoubleProperty overtime;

    /**
     * Creates a MonthlyTimeDataModel object
     * Used to structure data for the corresponding TableView
     * @param weekNumber String with week number
     * @param workedHours String with worked hours
     * @param supposedHours String with supposed work hours
     * @param overtime String with the amount of overtime
     */
    public WeeklyFormattedDataModel(int weekNumber, Double workedHours, Double supposedHours, Double overtime) {
        this.weekNumber = new SimpleIntegerProperty(weekNumber);
        this.workedHours = new SimpleDoubleProperty(workedHours);
        this.supposedHours = new SimpleDoubleProperty(supposedHours);
        this.overtime = new SimpleDoubleProperty(overtime);
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
