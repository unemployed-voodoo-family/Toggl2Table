package UnemployedVoodooFamily.Data;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Logic.FileLogic;
import ch.simas.jtoggl.TimeEntry;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import org.threeten.extra.YearWeek;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

public class WeeklyFormattedDataListFactory {

    private ArrayList<DailyFormattedDataModel> weeklyList;
    private FileLogic fileLogic;

    public List<DailyFormattedDataModel> buildWeeklyDataList(List<TimeEntry> timeEntries, YearWeek date,
                                                             Double accumulatedOffset) {
        weeklyList = new ArrayList<>();
        fileLogic = new FileLogic();
        List<WorkHours> workHours = fileLogic.loadJson(FilePath.getCurrentUserWorkhours());

        //finds the first day of the selected week
        LocalDate weeksFirstDate = date.atDay(DayOfWeek.MONDAY);

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

        Double accumulatedHours = accumulatedOffset;

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

        //TODO get correct supposed work hours
        weeklyList.add(new DailyFormattedDataModel(workedHours, supposedHours, currentDate, accumulatedHours, note));
    }

        return weeklyList;
}

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
