package UnemployedVoodooFamily.Data;

import ch.simas.jtoggl.TimeEntry;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class WeeklyFormattedDataListFactory {

    private ArrayList<DailyFormattedDataModel> weeklyList;

    public List<DailyFormattedDataModel> buildWeeklyDataList(List<TimeEntry> timeEntries, int weekNumber, int year)   {
        weeklyList = new ArrayList<>();

        //finds the first day of the selected week
        LocalDate weeksFirstDate = LocalDate.ofYearDay(year, 50)
                                            .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, weekNumber)
                                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        //Creates a list with the entire week
        for(DayOfWeek weekday : DayOfWeek.values()) {

            //Create current date to process
            LocalDate date = weeksFirstDate.plusDays(weekday.getValue()-1);
            double workedHours = 0.00;

            if(!timeEntries.isEmpty())  {
                for(TimeEntry t : timeEntries)  {
                    //Check if there is a timer running and ignore it if it is
                    if(null != t.getStop()) {
                        if(t.getStart().toLocalDate().isEqual(date) && t.getStop().toLocalDate().isEqual(date)) {
                            workedHours += ((double)t.getDuration() % 86400) / 3600;
                        }
                    }
                }
            }
                //TODO get correct supposed work hours
            weeklyList.add(new DailyFormattedDataModel(workedHours,7.5, date, ""));
        }

        return weeklyList;
    }
}
