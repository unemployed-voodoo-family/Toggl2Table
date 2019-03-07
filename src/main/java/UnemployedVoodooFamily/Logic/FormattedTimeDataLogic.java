package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.MonthlyFormattedTimeData;
import UnemployedVoodooFamily.Data.RawTimeDataModel;
import UnemployedVoodooFamily.Data.WeeklyFormattedTimeDataModel;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.TimeEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Time;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FormattedTimeDataLogic {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static List<ObservableList<MonthlyFormattedTimeData>> monthsList = new ArrayList<>(12);
    private static PropertiesLogic propsLogic = new PropertiesLogic();
    private static Properties props = new Properties();
    HashMap<Month, ObservableList<MonthlyFormattedTimeData>> monthsMap = new HashMap<>();


    public FormattedTimeDataLogic() {
        monthsMap.put(Month.JANUARY, null);
        monthsMap.put(Month.FEBRUARY, null);
        monthsMap.put(Month.MARCH, null);
        monthsMap.put(Month.APRIL, null);
        monthsMap.put(Month.MAY, null);
        monthsMap.put(Month.JUNE, null);
        monthsMap.put(Month.JULY, null);
        monthsMap.put(Month.OCTOBER, null);
        monthsMap.put(Month.NOVEMBER, null);
        monthsMap.put(Month.DECEMBER, null);
    }

    // List of the observable lists for all months.
    // Ranged from index 0 (January) to 11 (december)

    //TODO This class should be called to from the TableViewController
    // and is responsible for handling formatted time data
    public ObservableList<MonthlyFormattedTimeData> buildObservableMonthlyTimeData() {
        Session session = Session.getInstance();
        ObservableList<MonthlyFormattedTimeData> data = FXCollections.observableArrayList();
        Iterator<TimeEntry> it = session.getTimeEntries().iterator();
        List<Project> projects = session.getProjects();

        //MonthlyFormattedTimeData dataModel = new MonthlyFormattedTimeData();
        //data.add(dataModel);
        //TODO: unfinished
        return data;
    }

    public ObservableList<TimeEntry> buildObservableWeeklyTimeData() {
        Session session = Session.getInstance();
        ObservableList<TimeEntry> data = FXCollections.observableArrayList();
        Iterator<TimeEntry> it = session.getTimeEntries().iterator();

        while(it.hasNext()) {
            TimeEntry entry = it.next();
            Date startDate = entry.getStart();
            LocalDate date = Instant.ofEpochMilli(startDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            String weekDay = date.getDayOfWeek().name();
            Double supposedHours;
            Double workedHours;
            Double overTime;
        }
        //TODO: unfinished
        //WeeklyFormattedTimeDataModel dataModel = new WeeklyFormattedTimeDataModel();
        //data.add(dataModel);
        return data;
    }

    private void summarizeDay() {
    }

    private void summarizeWeek() {
    }
  
    //Called when the "export to excel" button is pressed
    public boolean buildExcelDocument()    {
        boolean exportSuccess = new ExcelWriter().generateExcelSheet();
        System.out.println(exportSuccess);
        return exportSuccess;
    }

    
}
