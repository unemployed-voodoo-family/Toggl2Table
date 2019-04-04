package UnemployedVoodooFamily.Data;

import javafx.beans.property.SimpleIntegerProperty;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class WeeklyFormattedDataModel extends DailyFormattedDataModel {

    private SimpleIntegerProperty weekNumber;

    /**
     * Creates a MonthlyTimeDataModel object
     * Used to structure data for the corresponding TableView
     * @param workedHours String with worked hours
     * @param supposedHours String with supposed work hours
     */
    public WeeklyFormattedDataModel(Double workedHours, Double supposedHours, LocalDate date, String note) {

        super(workedHours, supposedHours, date, note);

        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        this.weekNumber = new SimpleIntegerProperty(getDate().get(woy));
    }


    /**
     * Returns the week number
     * @return the week number
     */
    public Integer getWeekNumber() {
        return weekNumber.get();
    }

}
