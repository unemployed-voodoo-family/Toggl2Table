package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.MonthlyFormattedTimeData;
import UnemployedVoodooFamily.Data.DailySummarizedDataModel;
import UnemployedVoodooFamily.Data.DailySummarizedDataModelBuilder;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.TimeEntry;
import io.rocketbase.toggl.api.TogglReportApi;
import io.rocketbase.toggl.api.TogglReportApiBuilder;
import io.rocketbase.toggl.api.model.WeeklyUsersTimeResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.*;

public class FormattedTimeDataLogic {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private HashMap<Month, ObservableList<MonthlyFormattedTimeData>> monthsMap = new HashMap<>();


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


        //MonthlyFormattedTimeData dataModel = new MonthlyFormattedTimeData();
        //data.add(dataModel);
        //TODO: unfinished
        return data;
    }

    public ObservableList<DailySummarizedDataModel> buildObservableWeeklyTimeData() {

        Session session = Session.getInstance();

        ObservableList<DailySummarizedDataModel> data = FXCollections.observableArrayList();
        List<TimeEntry> timeEntries = session.getTimeEntries();

        //TODO: implement date filtering
        LocalDate startDate = LocalDate.of(2019, 1, 1);
        LocalDate endDate = LocalDate.of(2019, 4, 30);

        //iterate over all the days in the given range
        int i = 0;
        for(LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            boolean done = false;
            DailySummarizedDataModelBuilder builder = new DailySummarizedDataModelBuilder(date);
            //summarize the time entries for one daylars
            while(i < timeEntries.size() && !done) {
                TimeEntry t = timeEntries.get(i);
                LocalDate start = t.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if(start.equals(date)) {
                    builder.addTimeEntry(t);
                }
                else {
                    data.add(builder.build());
                    i--;
                    done = true;
                }
                i++;
            }
        }
        return data;
    }

    //Called when the "export to excel" button is pressed
    public boolean buildExcelDocument() {
        boolean exportSuccess = new ExcelWriter().generateExcelSheet();
        System.out.println(exportSuccess);
        return exportSuccess;
    }


}
