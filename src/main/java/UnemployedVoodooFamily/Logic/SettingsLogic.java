package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.WorkHoursData;
import UnemployedVoodooFamily.Data.DateRange;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Class to handle the logic of the settings
 */
public class SettingsLogic {

    private Properties props = new Properties();
    private static final String HOURS_PATH = "/Settings/hours.properties";
    private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    PropertiesLogic propsLogic = new PropertiesLogic();

    private TableColumn<WorkHoursData, String> fromCol;
    private TableColumn<WorkHoursData, String> toCol;
    private TableColumn<WorkHoursData, Double> hoursCol;

    /**
     * Writes the specified work hours to the "hours.properties" file.
     * @param fromDate the start of the period
     * @param toDate   the end of the period
     * @param hoursStr the standard work hours for this period
     * @throws URISyntaxException
     * @throws IOException
     */
    public void setWorkHours(LocalDate fromDate, LocalDate toDate,
                             String hoursStr) throws URISyntaxException, IOException {
        Double hours = Double.valueOf(hoursStr);

        //load props file
        props = propsLogic.loadProps(HOURS_PATH);

        DateRange range = new DateRange(fromDate, toDate, DATE_FORMAT);
        fixHoursOverlap(props, range, hours);
        propsLogic.saveProps(HOURS_PATH, props);

    }

    /**
     * Checks for overlap between the newly created period and the already existing ones.
     * Fixes overlap by overwriting old entries, and stitches
     * continous periods which have the same value.
     * @param props    the properties to write to
     * @param newRange the DateRange entered by the user
     * @param newValue the work hours entered by the user
     */
    private void fixHoursOverlap(Properties props, DateRange newRange, Double newValue) {

        //TODO: Check if user input is logical
        //TODO: sort stored properties properly
        Set<String> hours = props.stringPropertyNames();
        if(! hours.isEmpty()) {
            Iterator<String> it = hours.iterator();
            while(it.hasNext()) {
                String key = it.next();
                String valueStr = props.getProperty(key);
                Double value = Double.parseDouble(valueStr);
                boolean keyChanged = false;
                System.out.println();
                DateRange oldRange = DateRange.ofString(key, DATE_FORMAT);
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
                    props.remove(key);
                    props.put(oldRange.toString(), valueStr);
                }

                //if values are illogical or overwritten by the new value, remove
                if(newRange.isEncapsulating(oldRange) || oldRange.getFrom().equals(oldRange.getTo()) || oldRange
                        .getFrom().isAfter(oldRange.getTo())) {
                    props.remove(key);
                }
            }
            // after checking against the other
            // entries, put the new entry
            props.put(newRange.toString(), newValue.toString());
        }
        else {
            // There are no other values in the list,
            // and the new value can just be added
            props.put(newRange.toString(), newValue.toString());
        }
        System.out.println(props.stringPropertyNames().toString());
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
        props = propsLogic.loadProps(HOURS_PATH);
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
