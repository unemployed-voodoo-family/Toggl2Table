package UnemployedVoodooFamily.Data;

import UnemployedVoodooFamily.Logic.Session;
import ch.simas.jtoggl.TimeEntry;
import javafx.beans.property.SimpleIntegerProperty;
import org.threeten.extra.YearWeek;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Builder for the ExtendedDailyFormattedDataModel
 * Builds the summary for one week for use in the monthly table
 * Assumes all supplied data is from the same week.
 */
public class MonthlyFormattedDataListFactory {

    private List<DailyFormattedDataModel> monthlyList;

    public List<DailyFormattedDataModel> buildMonthlyDataList(List<TimeEntry> timeEntries, Month month, int year) {
        monthlyList = new ArrayList<>();

        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate monthEnd = LocalDate.of(year, month, YearMonth.of(year, month).atEndOfMonth().getDayOfMonth());
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();

        int previousWeekNumber = 0;
        int currentWeekNumber;

        //Create a list which is a sublist of all the time entries
        List<TimeEntry> monthSublist = new ArrayList<>();

        for(TimeEntry t: timeEntries) {
            //exclude all time entries that have an ongoing timer and that are outside of the selected month
            if(null != t.getStop() && (t.getStart().toLocalDate().getMonth() == month)) {
                monthSublist.add(t);
            }
        }

        for(LocalDate d = monthStart; ! d.isAfter(monthEnd); d = d.plusDays(1)) {
            currentWeekNumber = d.get(woy);
            double workedHours = 0;
            if(! monthSublist.isEmpty()) {
                for(TimeEntry t: monthSublist) {
                    if(t.getStart().toLocalDate().isEqual(d)) {
                        workedHours += ((double) t.getDuration() % 86400) / 3600;
                    }
                }
            }

            //TODO get actual supposed work hours
            if(currentWeekNumber != previousWeekNumber) {
                monthlyList.add(new DailyFormattedDataModel(workedHours, 0.00, d, 0d, "yay"));
            }
            else {
                monthlyList.add(new DailyFormattedDataModel(workedHours, 0.00, d, 0d, "yay"));
            }
            previousWeekNumber = currentWeekNumber;
        }

        return monthlyList;
    }

    public List<DailyFormattedDataModel> buildMonthlyDataList(Map<YearWeek, List<DailyFormattedDataModel>> timeEntries,
                                                              Month month, int year) {
        if(timeEntries.size() < 1) {

        }
        // create new list
        List<DailyFormattedDataModel> resultList = new ArrayList<>();

        // start and end dates of the month
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = ym.atEndOfMonth();

        // fix edge cases by removing dates not in this month
        YearWeek startYw = YearWeek.from(start);
        YearWeek endYw = YearWeek.from(end);
        List<DailyFormattedDataModel> filteredStart = timeEntries.get(YearWeek.from(start)).stream()
                                                                 .filter(entry -> ! entry.getDate().isBefore(start))
                                                                 .collect(Collectors.toList());

        List<DailyFormattedDataModel> filteredEnd = timeEntries.get(endYw).stream()
                                                               .filter(entry -> ! entry.getDate().isAfter(end))
                                                               .collect(Collectors.toList());

        //add all the lists from this month to the result-list
        resultList.addAll(filteredStart);
        for(YearWeek i = startYw.plusWeeks(1); i.isBefore(endYw); i = i.plusWeeks(1)) {
            resultList.addAll(timeEntries.get(i));
        }
        resultList.addAll(filteredEnd);

        return resultList;
    }

}