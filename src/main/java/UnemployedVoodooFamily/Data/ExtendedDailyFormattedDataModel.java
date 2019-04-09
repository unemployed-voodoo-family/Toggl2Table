package UnemployedVoodooFamily.Data;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class ExtendedDailyFormattedDataModel extends DailyFormattedDataModel {

    private SimpleStringProperty weekNumber;

    /**
     * Creates a MonthlyTimeDataModel object
     * Used to structure data for the corresponding TableView
     * @param workedHours String with worked hours
     * @param supposedHours String with supposed work hours
     */
    public ExtendedDailyFormattedDataModel(Double workedHours, Double supposedHours, LocalDate date, int weekNumber, String note) {

        super(workedHours, supposedHours, date, note);

        if(weekNumber <= 0) {
            this.weekNumber = new SimpleStringProperty("");
        }
        else {
            this.weekNumber = new SimpleStringProperty(String.valueOf(weekNumber));
        }
    }


    /**
     * Returns the week number
     * @return the week number
     */
    public String getWeek() {
        if(weekNumber.get().equals("")) {
            return weekNumber.get();
        }
        else {
            return "Week " + weekNumber.get();
        }
    }

}
