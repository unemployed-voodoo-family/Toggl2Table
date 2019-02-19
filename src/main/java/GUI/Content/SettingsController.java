package GUI.Content;

import GUI.DateRange;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class SettingsController {

    @FXML
    private Button viewHoursBtn;
    @FXML
    private Button confirmHoursBtn;
    @FXML
    private DatePicker hoursFromField;
    @FXML
    private DatePicker hoursToField;
    @FXML
    private Button viewDataBtn;
    @FXML
    private Button dataPathBtn;
    @FXML
    private TextField dataPathField;
    @FXML
    private Button logoutBtn;
    @FXML
    private Pane contentRoot;
    @FXML
    private TextField hoursField;


    public Node loadFXML() throws IOException {
        URL r = getClass().getClassLoader().getResource("Settings.fxml");
        return FXMLLoader.load(r);
    }

    public void initialize() {
        setKeyAndClickListeners();

    }

    private void setKeyAndClickListeners() {

        confirmHoursBtn.setOnAction(event -> {
            try {
                setWorkHours();
            }
            catch(URISyntaxException e) {
                e.printStackTrace();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setWorkHours() throws URISyntaxException, IOException {
        //TODO: feedback
        //TODO: make hoursField only accept numbers
        //get saved work hours file and add to arraylist
        URL resourceUrl = getClass().getResource("/Settings/settings.properties");
        File filename = new File(resourceUrl.toURI().getPath());
        Properties props = new Properties();
        props.load(new FileInputStream(filename));

        //get values from datepicker
        LocalDate fromDate = hoursFromField.getValue();
        LocalDate toDate = hoursToField.getValue();
        Double hours = Double.parseDouble(hoursField.getText());
        if(fromDate != null && toDate != null && hours != null) {
            DateRange range = new DateRange(fromDate, toDate);
            fixHoursOverlap(props, range, hours);
            props.store(new FileOutputStream(filename), "Datez");
        }
        else {
            System.out.println("null");
        }
    }

    private void fixHoursOverlap(Properties props, DateRange newRange, Double newValue) {

        Set<String> hours = props.stringPropertyNames();
        if(! hours.isEmpty()) {
            Iterator it = hours.iterator();
            while(it.hasNext()) {
                String key = (String) it.next();
                String value = props.getProperty(key);
                boolean changed = false;
                String[] dates = key.split(" - ");
                System.out.println();
                DateRange oldRange = new DateRange(LocalDate.parse(dates[0]), LocalDate.parse(dates[1]));

                //if new "from" value overrides old "to" value
                if(newRange.getFrom().isAfter(oldRange.getTo())) {
                    oldRange.setTo(newRange.getFrom().minusDays(1));
                    changed = true;
                }
                //if new "to" value overrides old "from" value
                if(newRange.getTo().isAfter(oldRange.getFrom())) {
                    oldRange.setFrom(newRange.getTo().plusDays(1));
                    changed = true;
                }
                //if values are illogical, remove
                if(oldRange.getFrom().equals(oldRange.getTo()) || oldRange.getFrom().isAfter(oldRange.getTo())) {
                    props.remove(key);
                }
                if(changed) {
                    props.remove(key);
                    props.put(oldRange.getFrom().toString() + " - " + oldRange.getTo().toString(), value);
                }
                props.put(newRange.getFrom().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " - "
                                  + newRange.getTo().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), newValue.toString());
                System.out.println(props.stringPropertyNames().toString());
            }
        }
        else {
            props.put(newRange.getFrom().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " - "
                              + newRange.getTo().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), newValue.toString());

        }
    }
}

