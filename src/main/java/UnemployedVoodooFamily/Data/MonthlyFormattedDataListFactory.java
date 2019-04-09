package UnemployedVoodooFamily.Data;

import ch.simas.jtoggl.TimeEntry;
import javafx.beans.property.SimpleIntegerProperty;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * Builder for the ExtendedDailyFormattedDataModel
 * Builds the summary for one week for use in the monthly table
 * Assumes all supplied data is from the same week.
 */
public class MonthlyFormattedDataListFactory {

    private ArrayList<ExtendedDailyFormattedDataModel> monthlyList;

    public List<ExtendedDailyFormattedDataModel> buildMonthlyDataList(List<TimeEntry> timeEntries, Month month, int year)   {
        monthlyList = new ArrayList<>();

        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate monthEnd = LocalDate.of(year, month, YearMonth.of(year, month).atEndOfMonth().getDayOfMonth());
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

        int previousWeekNumber = 0;
        int currentWeekNumber;

        //Create a list which is a sublist of all the time entries
        List<TimeEntry> monthSublist = new ArrayList<>();

        for(TimeEntry t : timeEntries)  {
            //exclude all time entries that have an ongoing timer and that are outside of the selected month
            if(null != t.getStop() && (t.getStart().toLocalDate().getMonth() == month)) {
                monthSublist.add(t);
            }
        }

        for (LocalDate d = monthStart; !d.isAfter(monthEnd); d = d.plusDays(1)) {
            currentWeekNumber = d.get(woy);
            double workedHours = 0;

            if(!monthSublist.isEmpty())  {
                for(TimeEntry t : monthSublist)  {
                        if(t.getStart().toLocalDate().isEqual(d) && t.getStop().toLocalDate().isEqual(d)) {
                            workedHours += ((double)t.getDuration() % 86400) / 3600;
                    }
                }
            }

            //TODO get actual supposed work hours
            if(currentWeekNumber != previousWeekNumber) {
                monthlyList.add(new ExtendedDailyFormattedDataModel(workedHours, 0.00, d, currentWeekNumber,"yay"));
            }
            else {
                monthlyList.add(new ExtendedDailyFormattedDataModel(workedHours, 0.00, d, 0,"yay"));
            }
            previousWeekNumber = currentWeekNumber;
        }

        return monthlyList;
    }

}