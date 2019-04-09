package UnemployedVoodooFamily.Data;

import ch.simas.jtoggl.TimeEntry;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

public class WeeklyFormattedDataListFactory {

    private ArrayList<DailyFormattedDataModel> weeklyList;

    public List<DailyFormattedDataModel> buildWeeklyDataList(List<TimeEntry> timeEntries, LocalDate date)   {
        weeklyList = new ArrayList<>();


        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int year = date.getYear();
        int weekNumber = date.get(woy);
        //finds the first day of the selected week
        LocalDate weeksFirstDate = LocalDate.ofYearDay(year, 50)
                                            .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, weekNumber)
                                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        //Create a sublist for that specific week containing time entries only from that week
        List<TimeEntry> weekSublist = new ArrayList<>();

        for(TimeEntry t : timeEntries)  {
            if(null != t.getStop()
                    && (t.getStart().toLocalDate().isEqual(weeksFirstDate)
                        || (t.getStart().toLocalDate().isAfter(weeksFirstDate)
                            && t.getStop().toLocalDate().isBefore(weeksFirstDate.plusDays(6))))) {
                    weekSublist.add(t);
            }
        }

        //Creates a list with the entire week summarised
        for(DayOfWeek weekday : DayOfWeek.values()) {

            //Create current date to process
            LocalDate currentDate = weeksFirstDate.plusDays(weekday.getValue()-1);
            double workedHours = 0.00;

            if(!weekSublist.isEmpty())  {
                for(TimeEntry t : weekSublist)  {
                    if(t.getStart().toLocalDate().isEqual(currentDate) && t.getStop().toLocalDate().isEqual(currentDate)) {
                        workedHours += ((double)t.getDuration() % 86400) / 3600;
                    }
                }
            }
                //TODO increase performance
                //TODO get correct supposed work hours
            weeklyList.add(new DailyFormattedDataModel(workedHours,0.00, date,  0d, ""));
        }

        return weeklyList;
    }
}
