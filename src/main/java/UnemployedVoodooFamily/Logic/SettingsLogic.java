package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.DateRange;
import UnemployedVoodooFamily.Data.WorkHours;
import UnemployedVoodooFamily.Data.WorkHoursModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

/**
 * Class to handle the logic of the settings
 */
public class SettingsLogic {

    private FileLogic propsLogic = new FileLogic();
    private String path;
    private List<WorkHours> workHours;

    public SettingsLogic(String path) {
        this.workHours = propsLogic.loadJson(path);
        this.path = path;
    }

    /**
     * Writes the specified work hours to the "hours.properties" file.
     * @param fromDate the start of the period
     * @param toDate   the end of the period
     * @param hoursStr the standard work hours for this period
     */
    public void setWorkHours(LocalDate fromDate, LocalDate toDate, String hoursStr, String note) {

        this.workHours = propsLogic.loadJson(path);
        Double hours = Double.valueOf(hoursStr);
        WorkHours wh = new WorkHours(fromDate, toDate, hours, note);
        fixHoursOverlap(wh);
        propsLogic.saveJson(path, workHours);
    }

    public List<WorkHours> getWorkHours() {
        return this.workHours;
    }

    /**
     * Checks for overlap between the newly created period and the already existing ones.
     * Fixes overlap by overwriting old entries, and stitches
     * continous periods which have the same value.
     */
    private void fixHoursOverlap(WorkHours wh) {

        if(this.workHours == null) {
            this.workHours = new ArrayList<>();
        }
        if(! this.workHours.isEmpty()) {
            ListIterator<WorkHours> it = this.workHours.listIterator();
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
            this.workHours.add(wh);
        }
    }

    public void populateHoursTable(TableView table) {
        //load props file
        List<WorkHours> whList = propsLogic.loadJson(path);

        ObservableList<WorkHoursModel> data = FXCollections.observableArrayList();
        Iterator<WorkHours> it = sortWorkHoursData(whList).iterator();

        while(it.hasNext()) {
            WorkHours next = it.next();
            data.add(new WorkHoursModel(next));
        }
        table.getItems().clear();
        table.getItems().addAll(data);
    }

    private List<WorkHours> sortWorkHoursData(List<WorkHours> data) {

        data.sort((o1, o2) -> {
            LocalDate d1 = o1.getFrom();
            LocalDate d2 = o2.getFrom();
            return d1.compareTo(d2);
        });
        return data;
    }

    /**
     * This method's main function is to delete files in a specific directory,
     * files including files in subdirectories will also be deleted, disregarding it's extension.
     * @param path is the directory path from where all files will be removed.
     */
    public void deleteStoredData(String path) throws Exception{
        File directory = new File(path);

            for(File file: directory.listFiles()){
                if(file.isDirectory()) {
                    deleteStoredData(directory + File.separator + file.getName());
                }
                else {
                    file.delete();
                }
            }

    }

    public <T> void deleteWorkHours(T workhours) {
        if(workhours instanceof WorkHoursModel) {
            this.workHours = propsLogic.loadJson(path);
            workHours.remove(((WorkHoursModel) workhours).getWorkHours());
            propsLogic.saveJson(path, this.workHours);
        }
    }
}
