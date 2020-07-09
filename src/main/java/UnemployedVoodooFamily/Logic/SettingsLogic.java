package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.DateRange;
import UnemployedVoodooFamily.Data.WorkHourConfig;
import UnemployedVoodooFamily.Data.WorkHours;
import UnemployedVoodooFamily.Data.WorkHoursModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.util.*;

/**
 * Class to handle the logic of the settings
 */
public class SettingsLogic {

    private String path;

    /**
     * Set path to JSON file.
     * @param path Path to the file where to store the settings
     */
    public void setJsonFilePath(String path) {
        this.path = path;
    }

    /**
     * Load settings from JSON file
     */
    public void loadFromJson() {
        WorkHourConfig.loadFromJson(path);
    }

    /**
     * Add the specified work-hour entry to the config, store it in settings file.
     * @param fromDate the start of the period
     * @param toDate   the end of the period
     * @param hoursStr the standard work hours for this period
     */
    public void addWorkHours(LocalDate fromDate, LocalDate toDate, String hoursStr, String note) {
        Double hours = Double.valueOf(hoursStr);
        WorkHours wh = new WorkHours(fromDate, toDate, hours, note);
        WorkHourConfig config = WorkHourConfig.getInstance();
        config.add(wh);
        config.sort();
        WorkHourConfig.saveToJson(path);
    }


    public void populateHoursTable(TableView table) {
        WorkHourConfig workHourConfig = WorkHourConfig.getInstance();
        if(workHourConfig != null) {
            ObservableList<WorkHoursModel> data = FXCollections.observableArrayList();
            ListIterator<WorkHours> it = workHourConfig.listIterator();

            while(it.hasNext()) {
                WorkHours next = it.next();
                data.add(new WorkHoursModel(next));
            }
            ObservableList tableItems = table.getItems();
            tableItems.clear();
            tableItems.addAll(data);
        }

    }


    public <T> void deleteWorkHours(T workhours) {
        WorkHourConfig workHourConfig = WorkHourConfig.getInstance();
        if(workhours instanceof WorkHoursModel) {
            workHourConfig.remove(((WorkHoursModel) workhours).getWorkHours());
            WorkHourConfig.saveToJson(path);
        }
    }
}
