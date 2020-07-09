package UnemployedVoodooFamily.Logic.Utils;

import ch.simas.jtoggl.TimeEntry;

import java.time.LocalDate;

/**
 * Utility class with helper methods for TimeEntry object processing
 */
public class TimeEntryUtils {

    /**
     * Check whether a time entry is within a given date range
     * @param t         Time entry to check
     * @param startDate Start of the range to check
     * @param stopDate  End of the range to check
     * @return True when time entry is within the range, false otherwise.
     */
    public static boolean isWithinRange(TimeEntry t, LocalDate startDate, LocalDate stopDate) {
        LocalDate start = t.getStart().toLocalDate();
        LocalDate stop = t.getStop().toLocalDate();
        if(start.isEqual(startDate)) {
            return true;
        }
        return start.isAfter(startDate) && (stop.isBefore(stopDate) || stop.isEqual(stopDate));
    }


    /**
     * Returns true if the time entry is closed (i.e., returns false for entries which are still in progress)
     * @param t Time entry to check
     * @return True when closed, false when still in progress
     */
    public static boolean isClosed(TimeEntry t) {
        return t != null && t.getStop() != null;
    }
}
