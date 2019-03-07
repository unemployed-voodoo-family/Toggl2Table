package UnemployedVoodooFamily.Data;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Logic.PropertiesLogic;
import ch.simas.jtoggl.TimeEntry;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class WeeklyFormattedTimeDataModelBuilder {
    private SimpleStringProperty weekDay;
    private SimpleDoubleProperty workedHours;
    private SimpleDoubleProperty supposedHours;
    private SimpleDoubleProperty overtime;

    private List<TimeEntry> weeklyTimeEntries = new ArrayList<>();

    public WeeklyFormattedTimeDataModelBuilder() {

    }

    public WeeklyFormattedTimeDataModelBuilder addTimeEntry(TimeEntry timeEntry) {
        weeklyTimeEntries.add(timeEntry);
        return this;
    }

    public WeeklyFormattedTimeDataModelBuilder setTimeEntries(List<TimeEntry> timeEntries) {
        this.weeklyTimeEntries = timeEntries;
        return this;
    }

    public WeeklyFormattedTimeDataModel build() {
        //this.supposedHours = FilePath.USER_HOME
        return null; //TODO finish
    }
}
