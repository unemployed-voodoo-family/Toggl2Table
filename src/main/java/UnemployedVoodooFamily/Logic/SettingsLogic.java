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
    private WorkHourConfig workHourConfig;

    /**
     * Initialize Setting logic
     * @param path Path to the file where to store the settings
     */
    public SettingsLogic(String path) {
        this.workHourConfig = WorkHourConfig.loadFromJson(path);
        this.path = path;
    }

    /**
     * Writes the specified work hours to the  settings file.
     * @param fromDate the start of the period
     * @param toDate   the end of the period
     * @param hoursStr the standard work hours for this period
     */
    public void setWorkHours(LocalDate fromDate, LocalDate toDate, String hoursStr, String note) {
        // TODO - code smell - loading from file and saving to file happens in too many places?
        this.workHourConfig = WorkHourConfig.loadFromJson(path);
        Double hours = Double.valueOf(hoursStr);
        WorkHours wh = new WorkHours(fromDate, toDate, hours, note);
        fixHoursOverlap(wh);
        this.workHourConfig.saveToJson(path);
    }

    /**
     * Checks for overlap between the newly created period and the already existing ones.
     * Fixes overlap by overwriting old entries, and stitches
     * continuous periods which have the same value.
     */
    private void fixHoursOverlap(WorkHours wh) {
        // TODO - code smell - too long
        if(this.workHourConfig == null) {
            this.workHourConfig = new WorkHourConfig();
        }
        if(! this.workHourConfig.isEmpty()) {
            ListIterator<WorkHours> it = this.workHourConfig.listIterator();
            while(it.hasNext()) {
                WorkHours next = it.next();
                Double value = next.getHours();
                Double newValue = wh.getHours();
                DateRange oldRange = next.getRange();
                DateRange newRange = wh.getRange();
                boolean keyChanged = false;

                // if the new value is inside another value, split it
                if(oldRange.isEncapsulating(newRange) && ! value.equals(newValue)) {
                    it.remove();
                    WorkHours wh1 = new WorkHours(oldRange.getFrom(), newRange.getFrom().minusDays(1), value,
                                                  next.getNote());
                    WorkHours wh2 = new WorkHours(newRange.getTo().plusDays(1), oldRange.getTo(), value,
                                                  next.getNote());
                    it.add(wh1);
                    it.add(wh2);
                    continue;
                }

                //if new "from" value overrides old "to" value
                if(newRange.fromValueinRange(oldRange)) {
                    if(newValue.equals(value)) {
                        newRange.setFrom(oldRange.getFrom());
                    }
                    else {
                        oldRange.setTo(newRange.getFrom().minusDays(1));
                        keyChanged = true;
                    }
                }
                //check if values can be combined
                LocalDate fromValue = newRange.getFrom();
                if(newValue.equals(value) && fromValue.minusDays(1).equals(oldRange.getTo())) {
                    newRange.setFrom(oldRange.getFrom());
                }

                //if new "to" value overrides old "from" value
                if(newRange.toValueInRange(oldRange)) {
                    if(newValue.equals(value)) {
                        newRange.setTo(oldRange.getTo());
                    }
                    else {
                        oldRange.setFrom(newRange.getTo().plusDays(1));
                        keyChanged = true;
                    }
                }

                // check if entries can be combined
                LocalDate toValue = newRange.getTo();
                if(newValue.equals(value) && toValue.plusDays(1).equals(oldRange.getFrom())) {
                    newRange.setTo(oldRange.getTo());
                }

                //if a value in the list was changed, save the changes
                if(keyChanged) {
                    it.remove();
                    it.add(new WorkHours(oldRange.getFrom(), oldRange.getTo(), value));
                }

                //if values are illogical or overwritten by the new value, remove
                if(newRange.isEncapsulating(oldRange) || oldRange.getFrom().equals(oldRange.getTo()) || oldRange
                        .getFrom().isAfter(oldRange.getTo())) {
                    it.remove();
                }
                wh.setFrom(newRange.getFrom());
                wh.setTo(newRange.getTo());
            }
            // after checking against the other
            // entries, put the new entry
            it.add(wh);
        }
        else {
            // There are no other values in the list,
            // and the new value can just be added
            this.workHourConfig.add(wh);
        }
    }

    public void populateHoursTable(TableView table) {
        //load props file
        if (workHourConfig != null) {
            workHourConfig.sort();
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
        if(workhours instanceof WorkHoursModel) {
            workHourConfig.remove(((WorkHoursModel) workhours).getWorkHours());
            workHourConfig.saveToJson(path);
        }
    }
}
