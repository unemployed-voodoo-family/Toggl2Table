package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.WeeklyFormattedDataModel;
import UnemployedVoodooFamily.Data.DailyFormattedDataModel;
import UnemployedVoodooFamily.Data.DailyFormattedDataModelBuilder;
import UnemployedVoodooFamily.Data.WeeklyFormattedDataModelBuilder;
import ch.simas.jtoggl.TimeEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

public class FormattedTimeDataLogic {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private HashMap<Month, ObservableList<WeeklyFormattedDataModel>> monthsMap = new HashMap<>();

    private ObservableList<DailyFormattedDataModel> weeklyMasterData;
    private ObservableList<WeeklyFormattedDataModel> monthtlyMasterData;

    // The time range of the fetched toggl data
    // current values are temporary
    private LocalDate startDate = LocalDate.of(2019, 3, 4);
    private LocalDate endDate = LocalDate.of(2019, 12, 30);

    private static DayOfWeek LAST_DAY_OF_WEEK = DayOfWeek.SUNDAY;
    private static DayOfWeek FIRST_DAY_OF_WEEK = DayOfWeek.MONDAY;


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

    /**
     * Builds data for the monthly table using the daily formatted data
     * @return the ObservableList containing all summarized weeks
     */
    public ObservableList<WeeklyFormattedDataModel> buildObservableMonthlyTimeData() {

        ObservableList<WeeklyFormattedDataModel> data = FXCollections.observableArrayList();

        //iterate trough each week of the time period
        int i = 0;
        for(LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date
                .with(TemporalAdjusters.next(FIRST_DAY_OF_WEEK))) {
            boolean weekFormatted = false;
            TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
            int weekNumber = date.get(woy);
            WeeklyFormattedDataModelBuilder builder = new WeeklyFormattedDataModelBuilder(weekNumber);

            //format each time entry in the week
            while(i < weeklyMasterData.size() && ! weekFormatted) {
                DailyFormattedDataModel dailyData = weeklyMasterData.get(i);

                int entryWeekNumber = dailyData.getDay().get(woy);

                if(entryWeekNumber == weekNumber) {
                    builder.addDailyData(dailyData);
                    if(i >= weeklyMasterData.size()) {
                        weekFormatted = true;
                    }
                }
                else {
                    i--;
                    weekFormatted = true;
                }
                i++;
            }
            data.add(builder.build());
        }
        monthtlyMasterData = data;
        return data;
    }

    //Called when the "export to excel" button is pressed
    public boolean exportToExcelDocument() {
        ExcelExportHandler exportHandler = new ExcelExportHandler();
        return exportHandler.makeExcelDocument();
    }

    /**
     * Builds daily formatted data for use in the weekly table
     * @return the ObservableList containing each summarized day
     */
    public ObservableList<DailyFormattedDataModel> buildObservableWeeklyTimeData() {

        Session session = Session.getInstance();

        ObservableList<DailyFormattedDataModel> data = FXCollections.observableArrayList();
        List<TimeEntry> timeEntries = session.getTimeEntries();

        //TODO: implement date filtering


        //iterate over all the days in the given range
        int i = 0;
        for(LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            boolean dateFormatted = false;
            DailyFormattedDataModelBuilder builder = new DailyFormattedDataModelBuilder(date);
            //summarize the time entries for one day
            while(i < timeEntries.size() && ! dateFormatted) {
                TimeEntry t = timeEntries.get(i);
                LocalDate start = t.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if(start.equals(date)) {
                    builder.addTimeEntry(t);
                }
                else {
                    i--;
                    dateFormatted = true;
                }
                i++;
            }
            data.add(builder.build());
        }
        weeklyMasterData = data;
        System.out.println(weeklyMasterData.size());
        return data;
    }
}
