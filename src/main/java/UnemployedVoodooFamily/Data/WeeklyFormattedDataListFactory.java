package UnemployedVoodooFamily.Data;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Logic.FileLogic;
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
    public List<DailyFormattedDataModel> buildWeeklyDataList(List<TimeEntry> timeEntries, YearWeek week,
                                                             Double accumulatedHours) {
        ArrayList<DailyFormattedDataModel> weeklyList = new ArrayList<>();
        FileLogic fileLogic = new FileLogic();
        List<WorkHours> workHours = fileLogic.loadJson(FilePath.getCurrentUserWorkhours());

        //finds the first day of the selected week
        LocalDate weeksFirstDate = week.atDay(DayOfWeek.MONDAY);

        //Create a sublist for that specific week containing time entries only from that week
        List<TimeEntry> weekSublist = new ArrayList<>();


        for(TimeEntry t: timeEntries) {
            if(null != t.getStop() && (t.getStart().toLocalDate().isEqual(weeksFirstDate) || (t.getStart().toLocalDate()
                                                                                               .isAfter(
                                                                                                       weeksFirstDate) && t
                    .getStop().toLocalDate().isBefore(weeksFirstDate.plusDays(6))))) {
                weekSublist.add(t);
            }
        }

        //Creates a list with the entire week summarised
        for(DayOfWeek weekday: DayOfWeek.values()) {

            //Create current date to process
            LocalDate currentDate = weeksFirstDate.plusDays(weekday.getValue() - 1L);
            double workedHours = 0d;
            double supposedHours = 0d;
            String note = "";

            WorkHours wh = getWorkHours(workHours, currentDate);
            //exclude saturdays and sundays
            if(weekday == DayOfWeek.SUNDAY || weekday == DayOfWeek.SATURDAY) {
                supposedHours = 0;
                if(wh != null) {
                    note = wh.getNote();
                }
            }
            else if(wh != null) {
                supposedHours = wh.getHours();
                note = wh.getNote();
            }

            if(! weekSublist.isEmpty()) {
                for(TimeEntry t: weekSublist) {
                    if(t.getStart().toLocalDate().isEqual(currentDate)) {
                        workedHours += (((double) t.getDuration()) / 3600);
                    }
                }
            }
            accumulatedHours += (workedHours - supposedHours);

            weeklyList.add(new DailyFormattedDataModel(workedHours, supposedHours, currentDate, accumulatedHours, note));
        }

        return weeklyList;
    }


    /**
     * Get the corresponding work hours object for the given date
     * Performs a search over the workhours list, until it finds an object which oontains the given date
     * Always ignores saturdays and sundays
     * @param list the list of WorkHours
     * @param date the date to check
     * @return a WorkHours object which is contains the given date, or null if no match is found.
     */
    private WorkHours getWorkHours(List<WorkHours> list, LocalDate date) {
        WorkHours wh = null;
        for(WorkHours workHours: list) {
            if(date.getDayOfWeek() != DayOfWeek.SATURDAY || date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                if(workHours.getRange().contains(date)) {
                    wh = workHours;
                    break;
                }
            }
        }
        return wh;
    }
}
