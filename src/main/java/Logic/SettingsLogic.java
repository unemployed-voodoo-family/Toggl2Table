package Logic;

import GUI.DateRange;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class SettingsLogic {

    private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public SettingsLogic() {
    }

    public void setWorkHours(LocalDate fromDate, LocalDate toDate,
                             String hoursStr) throws URISyntaxException, IOException {
        Double hours = Double.valueOf(hoursStr);

        //load props file
        URL resourceUrl = getClass().getResource("/Settings/hours.properties");
        File filename = new File(resourceUrl.toURI().getPath());
        Properties props = new Properties();
        props.load(new FileInputStream(filename));

        DateRange range = new DateRange(fromDate, toDate);
        fixHoursOverlap(props, range, hours);
        props.store(new FileOutputStream(filename), "Datez");

    }

    private void fixHoursOverlap(Properties props, DateRange newRange, Double newValue) {

        //TODO: sort stored properties properly
        //TODO: combine entries if value is the same and they are in same time ranges
        Set<String> hours = props.stringPropertyNames();
        if(!hours.isEmpty()) {
            Iterator it = hours.iterator();
            while(it.hasNext()) {
                String key = (String) it.next();
                String value = props.getProperty(key);
                boolean changed = false;
                String[] dates = key.split(" - ");
                System.out.println();
                DateRange oldRange = new DateRange(LocalDate.parse(dates[0], DATE_FORMAT),
                                                   LocalDate.parse(dates[1], DATE_FORMAT));

                //if new "from" value overrides old "to" value
                if(newRange.fromValueinRange(oldRange)) {
                    oldRange.setTo(newRange.getFrom().minusDays(1));
                    changed = true;
                }
                //if new "to" value overrides old "from" value
                if(newRange.toValueInRange(oldRange)) {
                    oldRange.setFrom(newRange.getTo().plusDays(1));
                    changed = true;
                }

                //if another value was changed, change it
                if(changed) {
                    props.remove(key);
                    props.put(oldRange.getFrom().format(DATE_FORMAT) + " - " + oldRange.getTo().format(DATE_FORMAT),
                              value);
                }

                //if values are illogical or overwritten by the new value, remove
                if(newRange.isEncapsulating(oldRange) || oldRange.getFrom().equals(oldRange.getTo()) || oldRange
                        .getFrom().isAfter(oldRange.getTo())) {
                    props.remove(key);
                }
            }
            // after checking against the other
            // entries, put the new entry
            props.put(newRange.getFrom().format(DATE_FORMAT) + " - " + newRange.getTo().format(DATE_FORMAT),
                      newValue.toString());
        }
        else {
            // There are no other values in the list,
            // and the new value can just be added
            props.put(newRange.getFrom().format(DATE_FORMAT) + " - " + newRange.getTo().format(DATE_FORMAT),
                      newValue.toString());
        }
        System.out.println(props.stringPropertyNames().toString());
    }

}
