package UnemployedVoodooFamily.Data;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Logic.Utils.TimeEntryUtils;
import ch.simas.jtoggl.TimeEntry;
import org.threeten.extra.YearWeek;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class WeeklyFormattedDataListFactory {

    /**
     * Formats the given timeEntries for the specified week.
     * @param timeEntries      the timeEntries to format
     * @param week             the week to format the timeEntries in
     * @param accumulatedHours The total accumulated hours throughout the year, up to the given week.
     * @return A list with seven entries, monday to sunday, with formatted timeEntries.
     */
    public static List<DailyFormattedDataModel> buildWeeklyDataList(List<TimeEntry> timeEntries, YearWeek week,
                                                             Double accumulatedHours) {
        ArrayList<DailyFormattedDataModel> weeklyList = new ArrayList<>();
        WorkHourConfig workHourConfig = WorkHourConfig.getInstance();

        if (workHourConfig == null) return weeklyList;

        // Finds Monday and Sunday of the selected week
        LocalDate monday = week.atDay(DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);

        //Create a sublist for that specific week containing time entries only from that week
        List<TimeEntry> weekEntries = new ArrayList<>();
        for(TimeEntry t: timeEntries) {
            if (TimeEntryUtils.isClosed(t) && TimeEntryUtils.isWithinRange(t, monday, sunday)) {
                weekEntries.add(t);
            }
        }

        //Creates a list with the entire week summarised
        for(DayOfWeek weekday: DayOfWeek.values()) {

            //Create current date to process
            LocalDate currentDate = monday.plusDays(weekday.getValue() - 1L);
            double workedHours = 0d;

            WorkHours wh = workHourConfig.getFor(currentDate);
            String note = wh != null ? wh.getNote() : "";
            double supposedHours = wh != null ? wh.getHours() : 0;

            for(TimeEntry t: weekEntries) {
                if(t.getStart().toLocalDate().isEqual(currentDate)) {
                    workedHours += (((double) t.getDuration()) / 3600);
                }
            }
            accumulatedHours += (workedHours - supposedHours);

            weeklyList
                    .add(new DailyFormattedDataModel(workedHours, supposedHours, currentDate, accumulatedHours, note));
        }

        return weeklyList;
    }

}
