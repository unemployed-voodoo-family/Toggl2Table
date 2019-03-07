package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.MonthlyFormattedTimeData;
import UnemployedVoodooFamily.Data.RawTimeDataModel;
import UnemployedVoodooFamily.Data.WeeklyFormattedTimeDataModel;
import UnemployedVoodooFamily.Data.WeeklyFormattedTimeDataModelBuilder;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.TimeEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Time;
import java.text.SimpleDateFormat;
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

    public ObservableList<WeeklyFormattedTimeDataModel> buildObservableWeeklyTimeData() {
        Session session = Session.getInstance();
        ObservableList<WeeklyFormattedTimeDataModel> data = FXCollections.observableArrayList();
        Iterator<TimeEntry> it = session.getTimeEntries().iterator();
        WeeklyFormattedTimeDataModelBuilder builder = new WeeklyFormattedTimeDataModelBuilder();

        for(DayOfWeek day : DayOfWeek.values()) {
            while(it.hasNext()) {
                TimeEntry t = it.next();
                if(isSameDay(t.getStart(), day.getValue())) {
                    builder.addTimeEntry(t);
                }
            }
            WeeklyFormattedTimeDataModel dayData = builder.build();
            data.add(dayData);
        }


        return data;
    }

    private boolean isSameDay(Date d1, int d2) {
        Calendar c = Calendar.getInstance();
        c.setTime(d1);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == d2;
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
