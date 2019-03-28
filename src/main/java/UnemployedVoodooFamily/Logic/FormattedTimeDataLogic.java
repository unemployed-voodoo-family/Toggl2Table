package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.DailyFormattedDataModel;
import UnemployedVoodooFamily.Data.DailyFormattedDataModelBuilder;
import UnemployedVoodooFamily.Data.WeeklyFormattedDataModel;
import UnemployedVoodooFamily.Data.WeeklyFormattedDataModelBuilder;
import ch.simas.jtoggl.TimeEntry;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.control.skin.ContextMenuContent;
import com.sun.javafx.scene.control.skin.ContextMenuSkin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Spinner;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FormattedTimeDataLogic {

    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd. LLLL yyyy");
    private static HashMap<Month, ObservableList<WeeklyFormattedDataModel>> monthsMap = new HashMap<>();

    private static ObservableList<DailyFormattedDataModel> weeklyMasterData;
    private static ObservableList<WeeklyFormattedDataModel> monthtlyMasterData;

    // The time range of the fetched toggl data
    // current values are temporary
    // the eventual values should be spanning a whole year
    private LocalDate startDate = LocalDate.of(2019, 1, 1);
    private LocalDate endDate = LocalDate.of(2019, 12, 31);

    private int selectedYear;
    private int selectedWeek;
    private Month selectedMonth;

    private static DayOfWeek LAST_DAY_OF_WEEK = DayOfWeek.SUNDAY;
    private static DayOfWeek FIRST_DAY_OF_WEEK = DayOfWeek.MONDAY;


    public FormattedTimeDataLogic() {
        //Get current year
        selectedYear = LocalDate.now().getYear();

        //Get current week
        LocalDate date = LocalDate.now();
        TemporalField weekFields = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        selectedWeek = date.get(weekFields);

        selectedMonth = LocalDate.now().getMonth();

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
    public ObservableList<WeeklyFormattedDataModel> buildMonthlyMasterDataList() {

        ObservableList<WeeklyFormattedDataModel> data = FXCollections.observableArrayList();

        //iterate trough each week in the time period
        if(weeklyMasterData == null) {
            throw new RuntimeException("Tried building monthly data with empty weekly data");
        }

        int i = 0;
        for(LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date
                .with(TemporalAdjusters.next(FIRST_DAY_OF_WEEK))) {

            boolean weekFormatted = false;

            //get week number
            TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
            int weekNumber = date.get(woy);

            //builder
            WeeklyFormattedDataModelBuilder builder = new WeeklyFormattedDataModelBuilder(date);

            //format each time entry in the week
            while(i < weeklyMasterData.size() && ! weekFormatted) {

                DailyFormattedDataModel dailyData = weeklyMasterData.get(i);
                int entryWeekNumber = dailyData.getDay().get(woy);

                // check if entry is suitable and add it to list
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
            //week formatted, build the data
            try {
                data.add(builder.build());
            }
            catch(RuntimeException e) {
            }
        }
        monthtlyMasterData = data;
        return data;
    }

    public ObservableList<WeeklyFormattedDataModel> buildMonthlySortedData() {
        ObservableList<WeeklyFormattedDataModel> data = FXCollections.observableArrayList();

        int i = 0;
        for(LocalDate date = LocalDate.of(selectedYear, selectedMonth.getValue(), 1);
                date.isBefore(LocalDate.of(selectedYear, selectedMonth, YearMonth.of(selectedYear, selectedMonth).lengthOfMonth()).plusDays(1)); date = date
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
        this.monthtlyMasterData = data;
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
    public ObservableList<DailyFormattedDataModel> buildObservableWeeklyTimeData(List<TimeEntry> timeEntries) {

        ObservableList<DailyFormattedDataModel> data = FXCollections.observableArrayList();
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

            DailyFormattedDataModel databBuild = builder.build();
            data.add(databBuild);
        }
        weeklyMasterData = data;
        return data;
    }

    public void setSelectedYear(int year)   {
        this.selectedYear = year;
        System.out.println("Selected year is: " + this.selectedYear);
    }

    public int getSelectedYear()    {
        return this.selectedYear;
    }

    public void setSelectedWeek(int selectedWeek) {
        this.selectedWeek = selectedWeek;
        System.out.println("Selected week is: " + this.selectedWeek);
    }

    public int getSelectedWeek() {
        return selectedWeek;
    }

    public void setSelectedMonth(Month selectedMonth) {
        this.selectedMonth = selectedMonth;
        System.out.println("Selected month: " + this.selectedMonth);
    }

    public Month getSelectedMonth() {
        return selectedMonth;
    }

    public ObservableList<DailyFormattedDataModel> getWeeklyMasterData() {
        return weeklyMasterData;
    }

    public ObservableList<WeeklyFormattedDataModel> getMonthtlyMasterData() {
        return monthtlyMasterData;
    }
}
