package UnemployedVoodooFamily.Data;

import org.threeten.extra.YearWeek;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Builder for the ExtendedDailyFormattedDataModel
 * Builds the summary for one week for use in the monthly table
 * Assumes all supplied data is from the same week.
 */
public class MonthlyFormattedDataListFactory {

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