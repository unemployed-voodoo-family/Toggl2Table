package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.DateRange;
import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Data.WorkHours;
import UnemployedVoodooFamily.Data.WorkHoursData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Class to handle the logic of the settings
 */
public class SettingsLogic {

    private Properties props = new Properties();
    private FileLogic propsLogic = new FileLogic();
    private String path;
    private List<WorkHours> workHours;

    private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private TableColumn<WorkHoursData, String> fromCol;
    private TableColumn<WorkHoursData, String> toCol;
    private TableColumn<WorkHoursData, Double> hoursCol;
    private TableColumn<WorkHoursData, String> noteCol;


    public SettingsLogic(String path) {
        this.workHours = propsLogic.loadJson(path);
        this.path = path;
    }

    /**
     * Writes the specified work hours to the "hours.properties" file.
     * @param fromDate the start of the period
     * @param toDate   the end of the period
     * @param hoursStr the standard work hours for this period
     * @throws URISyntaxException
     * @throws IOException
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
     * @param props    the properties to write to
     * @param newRange the DateRange entered by the user
     * @param newValue the work hours entered by the user
     */
    private void fixHoursOverlap(WorkHours wh) {

        //TODO: Check if user input is logical
        //TODO: sort stored properties properly
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
        fromCol = new TableColumn<>("From");
        toCol = new TableColumn<>("To");
        hoursCol = new TableColumn<>("Hours");
        noteCol = new TableColumn<>("Note");

        table.getColumns().clear();
        table.getColumns().addAll(fromCol, toCol, hoursCol, noteCol);
        fromCol.setCellValueFactory(param -> param.getValue().fromProperty());
        toCol.setCellValueFactory(param -> param.getValue().toProperty());
        hoursCol.setCellValueFactory(param -> param.getValue().hoursProperty());
        noteCol.setCellValueFactory(param -> param.getValue().noteProperty());

        //load props file
        props = propsLogic.loadProps(FilePath.getCurrentUserWorkhours());
        propsLogic.loadJson(path);

        ObservableList<WorkHoursData> data = FXCollections.observableArrayList();
        Iterator<WorkHours> it = sortWorkHoursData(propsLogic.loadJson(path)).iterator();

        while(it.hasNext()) {
            WorkHours next = it.next();
            Double hours = next.getHours();
            data.add(new WorkHoursData(next.getFrom(), next.getTo(), hours, next.getNote()));
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
    public void deleteStoredData(String path){
        File directory = new File(path);

        try{
            for(File file: directory.listFiles()){
                if(file.isDirectory()) {
                    deleteStoredData(directory + File.separator + file.getName());
                }
                else {
                    file.delete();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
