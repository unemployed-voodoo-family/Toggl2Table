package UnemployedVoodooFamily.Data;

import org.threeten.extra.YearWeek;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Factory for the ExtendedDailyFormattedDataModel
 * Builds the summary for one each day for use in the monthly table
 */
public class MonthlyFormattedDataListFactory {

    /**
     * Get timeEntries for a whole year, return a list of entries for each day of a given month
     * @param timeEntries map, where keys are weeks and values are lists of 7 entries of formatted data corresponding
     *                    each day
     * @param month The month we are interested in (filter)
     * @param year  The year we are interested in (filter)
     * @return a list where each value is a formatted data for a single day of the corresponding month. Entries are
     * ordered by date.
     */
    public List<DailyFormattedDataModel> buildMonthlyDataList(Map<YearWeek, List<DailyFormattedDataModel>> timeEntries,
                                                              Month month, int year) {
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
        List<DailyFormattedDataModel> resultList = new ArrayList<>(filteredStart);
        for(YearWeek i = startYw.plusWeeks(1); i.isBefore(endYw); i = i.plusWeeks(1)) {
            resultList.addAll(timeEntries.get(i));
        }
        resultList.addAll(filteredEnd);

        return resultList;
    }

}