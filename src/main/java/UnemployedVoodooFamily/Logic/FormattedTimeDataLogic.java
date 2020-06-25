package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.*;
import ch.simas.jtoggl.TimeEntry;
import org.threeten.extra.YearWeek;

import java.io.IOException;
import java.time.*;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

public class FormattedTimeDataLogic {
    private static Map<YearWeek, List<DailyFormattedDataModel>> weeklyMasterData;
    private static Map<YearMonth, List<DailyFormattedDataModel>> monthlyMasterData;

    private int selectedYear;
    private int selectedWeek;
    private Month selectedMonth;
    private double hourBalance;

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
    public boolean exportToExcelDocument(Map<YearMonth, List<DailyFormattedDataModel>> timeEntries,
                                         int year) throws IOException {
        if(timeEntries == null) {
            return false;
        }
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
        if(timeEntries.isEmpty()) {
            return;
        }

        Iterator<TimeEntry> it = timeEntries.iterator();
        TimeEntry te = it.next();
        // Skip the time entries before the given year
        while(it.hasNext() && te.getStart().getYear() < year) {
            te = it.next();
        }

        // Look at the day of time entries. Sum up all entries in each day
        int day = te.getStart().getDayOfYear();
        LocalDate date = te.getStart().toLocalDate();
        double workedHours = 0;
        hourBalance = 0d;
        weeklyMasterData = new HashMap<>();
        monthlyMasterData = new HashMap<>();

        while(te != null && te.getStart().getYear() == year) {
            if(newDayStarts(te, day)) {
                DailyFormattedDataModel dayData = createDayData(date, workedHours);
                addDayToWeek(dayData);
                addDayToMonth(dayData);
                markEmptyWorkdaysAsZero(year, day, te.getStart().getDayOfYear());
                // Reset the state
                date = te.getStart().toLocalDate();
                day = date.getDayOfYear();
                workedHours = 0;
            }
            workedHours += ((float) te.getDuration()) / 3600.0;

            if(it.hasNext()) {
                te = it.next();
            }
            else {
                te = null;
                // No more time entries, add the unfinished day data to the lists
                if(workedHours > 0) {
                    DailyFormattedDataModel dayData = createDayData(date, workedHours);
                    addDayToWeek(dayData);
                    addDayToMonth(dayData);
                }
            }
        }

        // TODO - build project-wise report entries: for a chosen project, find hours for each of given tags
    }

    /**
     * Find all work days between the previous day and the current day, create zero-hour entries in those days.
     * @param year The year. Warning - both days must be in the same year!
     * @param prevDay Previous day
     * @param currentDay Current day
     */
    private void markEmptyWorkdaysAsZero(int year, int prevDay, int currentDay) {
        WorkHourConfig config = WorkHourConfig.getInstance();
        for (int day = prevDay + 1; day < currentDay; ++day) {
            LocalDate d = LocalDate.ofYearDay(year, day);
            WorkHours wh = config.getFor(d);
            if (wh != null && wh.getHours() > 0) {
                System.out.println("Expected to work " + wh.getHours() + " hours on " + d + ", took it free");
                DailyFormattedDataModel dayData = createDayData(d, 0);
                addDayToWeek(dayData);
                addDayToMonth(dayData);
            }
        }
    }

    /**
     * Check if the time entry is in a new day, compared to the day of the previous time entry
     * @param te      Tume entry
     * @param prevDay the day of the previous time entry
     * @return
     */
    private boolean newDayStarts(TimeEntry te, int prevDay) {
        return te.getStart().getDayOfYear() != prevDay;
    }

    /**
     * Summarize accumulated hour information, create a day summary
     * @param date        Date for the day
     * @param workedHours How many hours were worked that day
     * @return A formatted entry for day summary
     */
    private DailyFormattedDataModel createDayData(LocalDate date, double workedHours) {
        WorkHours wh = WorkHourConfig.getInstance().getFor(date);
        double supposedHours = wh != null ? wh.getHours() : 0;
        String note = wh != null ? wh.getNote() : "";
        hourBalance += (workedHours - supposedHours);
        return new DailyFormattedDataModel(workedHours, supposedHours, date, hourBalance, note);
    }

    /**
     * Add a day entry to weekly master data
     * @param dayData
     */
    private void addDayToWeek(DailyFormattedDataModel dayData) {
        // Update week and month data
        YearWeek week = dayData.getWeekNumber();
        if(weeklyMasterData.containsKey(week)) {
            weeklyMasterData.get(week).add(dayData);
        }
        else {
            List<DailyFormattedDataModel> weekData = new LinkedList<>();
            weekData.add(dayData);
            weeklyMasterData.put(week, weekData);
        }
    }

    /**
     * Add a day entry to monthly master data
     * @param dayData
     */
    public void addDayToMonth(DailyFormattedDataModel dayData) {
        LocalDate d = dayData.getDate();
        YearMonth month = YearMonth.of(d.getYear(), d.getMonthValue());
        if(monthlyMasterData.containsKey(month)) {
            monthlyMasterData.get(month).add(dayData);
        }
        else {
            List<DailyFormattedDataModel> monthData = new LinkedList<>();
            monthData.add(dayData);
            monthlyMasterData.put(month, monthData);
        }
    }

    public void setSelectedYear(int year) {
        this.selectedYear = year;
    }

    public int getSelectedYear() {
        return this.selectedYear;
    }

    public void setSelectedWeek(int selectedWeek) {
        this.selectedWeek = selectedWeek;
    }

    public int getSelectedWeek() {
        return selectedWeek;
    }

    public void setSelectedMonth(Month selectedMonth) {
        this.selectedMonth = selectedMonth;
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

    public static Map<YearMonth, List<DailyFormattedDataModel>> getMonthlyMaterData() {
        return monthlyMasterData;
    }
}
