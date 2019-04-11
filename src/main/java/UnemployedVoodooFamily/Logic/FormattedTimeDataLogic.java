package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.DailyFormattedDataModel;
import UnemployedVoodooFamily.Data.WeeklyFormattedDataListFactory;
import UnemployedVoodooFamily.Data.MonthlyFormattedDataListFactory;
import ch.simas.jtoggl.TimeEntry;
import org.threeten.extra.YearWeek;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

public class FormattedTimeDataLogic {

    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd. LLLL yyyy");

    private static Map<YearWeek, List<DailyFormattedDataModel>> weeklyMasterData;
    private static Map<YearMonth, List<DailyFormattedDataModel>> monthlyMasterData;

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

    }


    //Called when the "export to excel" button is pressed
    public boolean exportToExcelDocument(List<TimeEntry> timeEntries, int year) throws IOException {
        ExcelExportHandler exportHandler = new ExcelExportHandler(timeEntries, year);
        return exportHandler.makeExcelDocument();
    }

    public List<DailyFormattedDataModel> getWeeklyData(YearWeek yearWeek) {
        if(weeklyMasterData == null) {
            throw new RuntimeException("Weekly master data not yet initalized");
        }
        return weeklyMasterData.getOrDefault(yearWeek, Collections.emptyList());
    }

    public List<DailyFormattedDataModel> getMonthlyData(YearMonth yearMonth) {
        if(monthlyMasterData == null) {
            throw new RuntimeException("Monthly master data not yet initalized");
        }
        return monthlyMasterData.getOrDefault(yearMonth, Collections.emptyList());
    }

    /**
     * Build the masterdata for one full year, from scratch.
     * @param timeEntries
     * @param year
     */
    public void buildMasterData(List<TimeEntry> timeEntries, int year) {

        weeklyMasterData = new HashMap<>();
        monthlyMasterData = new HashMap<>();

        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        YearWeek startWeek = YearWeek.from(startDate);
        YearWeek endWeek = YearWeek.from(endDate);

        Double accumulatedOffset = 0d;

        for(YearWeek date = startWeek; date.isBefore(endWeek.plusWeeks(1)); date = date.plusWeeks(1)) {
            weeklyMasterData.put(date, new WeeklyFormattedDataListFactory().buildWeeklyDataList(timeEntries, date, accumulatedOffset));
            List<DailyFormattedDataModel> latestWeek = weeklyMasterData.get(date);
            accumulatedOffset = latestWeek.get(latestWeek.size() - 1).getAccumulatedHours();
        }

        for(Month month: Month.values()) {

            List<DailyFormattedDataModel> list = new MonthlyFormattedDataListFactory()
                    .buildMonthlyDataList(weeklyMasterData, month, year);
            YearMonth yearMonth = YearMonth.of(year, month);

            monthlyMasterData.put(yearMonth, list);
        }

    }

    public void setSelectedYear(int year) {
        this.selectedYear = year;
        System.out.println("Selected year is: " + this.selectedYear);
    }

    public int getSelectedYear() {
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

    public Map<YearWeek, List<DailyFormattedDataModel>> getWeeklyMasterData() {
        return weeklyMasterData;
    }

    public Map<YearMonth, List<DailyFormattedDataModel>> getMonthlyMasterData() {
        return monthlyMasterData;
    }
}
