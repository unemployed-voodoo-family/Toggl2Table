package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.DateRange;
import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Data.WorkHours;
import UnemployedVoodooFamily.Data.WorkHoursData;
import com.google.gson.JsonObject;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
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
    private PropertiesLogic propsLogic = new PropertiesLogic();
    private String path;
    private List<WorkHours> workHours;

    private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private TableColumn<WorkHoursData, String> fromCol;
    private TableColumn<WorkHoursData, String> toCol;
    private TableColumn<WorkHoursData, Double> hoursCol;


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
    public void setWorkHours(LocalDate fromDate, LocalDate toDate,
                             String hoursStr){

        this.workHours = propsLogic.loadJson(path);
        Double hours = Double.valueOf(hoursStr);
        WorkHours wh = new WorkHours(fromDate, toDate, hours);
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
            Iterator<WorkHours> it = this.workHours.iterator();
            while(it.hasNext()) {
                WorkHours next = it.next();
                Double value = next.getHours();
                Double newValue = wh.getHours();
                DateRange oldRange = next.getRange();
                DateRange newRange = wh.getRange();
                boolean keyChanged = false;

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
                LocalDate toValue = newRange.getTo();
                if(newValue.equals(value) && toValue.plusDays(1).equals(oldRange.getFrom())) {
                    newRange.setTo(oldRange.getTo());
                }

                //if another value was changed, change it
                if(keyChanged) {
                    it.remove();
                    this.workHours.add(new WorkHours(oldRange.getFrom(), oldRange.getTo(), value));
                }

                //if values are illogical or overwritten by the new value, remove
                if(newRange.isEncapsulating(oldRange) || oldRange.getFrom().equals(oldRange.getTo()) || oldRange
                        .getFrom().isAfter(oldRange.getTo())) {
                    it.remove();
                }
            }
            // after checking against the other
            // entries, put the new entry
            this.workHours.add(wh);
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

        table.getColumns().clear();
        table.getColumns().addAll(fromCol, toCol, hoursCol);
        fromCol.setCellValueFactory(param -> param.getValue().fromProperty());
        toCol.setCellValueFactory(param -> param.getValue().toProperty());
        hoursCol.setCellValueFactory(param -> param.getValue().hoursProperty());

        //load props file
        props = propsLogic.loadProps(FilePath.getCurrentUserWorkhours());
        Set<String> periods = props.stringPropertyNames();

        ObservableList<WorkHoursData> data = FXCollections.observableArrayList();
        Iterator<String> it = sortWorkHoursData(periods).iterator();

        while(it.hasNext()) {
            String key = it.next();
            String value = props.getProperty(key);
            DateRange range = DateRange.ofString(key, DATE_FORMAT);
            data.add(new WorkHoursData(range.getFrom(), range.getTo(), Double.valueOf(value)));
        }
        table.getItems().clear();
        table.getItems().addAll(data);
    }

    private List<String> sortWorkHoursData(Set<String> data) {
        List<String> dataSorted = new ArrayList<>(data);
        dataSorted.sort((o1, o2) -> {
            LocalDate d1 = DateRange.ofString(o1, DATE_FORMAT).getFrom();
            LocalDate d2 = DateRange.ofString(o2, DATE_FORMAT).getFrom();
            return d1.compareTo(d2);
        });
        return dataSorted;
    }

}
