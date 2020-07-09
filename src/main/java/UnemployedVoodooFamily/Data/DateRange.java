package UnemployedVoodooFamily.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Class to store a period of time, denoted by a from and to date.
 * Includes helper methods to compare with other DateRanges
 */
public class DateRange {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private LocalDate from;
    private LocalDate to;

    public DateRange(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    /**
     * Check if "from" value overlaps with another DateRange
     * @param otherRange the DateRange to compare with
     * @return true/false
     */
    public boolean fromValueinRange(DateRange otherRange) {
        return otherRange.contains(this.from);
    }

    /**
     * Check if "to" value overlaps with another DateRange
     * @param otherRange the DateRange to compare with
     * @return true/false
     */
    public boolean toValueInRange(DateRange otherRange) {
       return otherRange.contains(this.to);
    }

    /**
     * Check if this DateRange encapsulates another DateRange
     * @param otherRange the DateRange to compare with
     * @return true/false
     */
    public boolean isEncapsulating(DateRange otherRange) {
        return isBeforeOrEqual(this.from, otherRange.from) && isAfterOrEqual(this.to, otherRange.to);
    }


    /**
     * Check if this a LocalDate object is after or equal to another LocalDate
     * @param date the date you want to check if is after or equal
     * @param otherDate the date to check against
     * @return true or false
     */
    private static boolean isAfterOrEqual(LocalDate date, LocalDate otherDate) {
        return date.isAfter(otherDate) || date.equals(otherDate);
    }

    /**
     * Check if this a LocalDate object is before or equal to another LocalDate
     * @param date the date you want to check if is before or equal
     * @param otherDate the date to check against
     * @return true or false
     */
    private static boolean isBeforeOrEqual(LocalDate date, LocalDate otherDate) {
        return date.isBefore(otherDate) || date.equals(otherDate);
    }

    /**
     * Check if the given date is in this range.
     * @param date the date to check
     * @return true when date is within the range
     */
    public boolean contains(LocalDate date) {
        return isAfterOrEqual(date, this.from) && isBeforeOrEqual(date, this.to);
    }

    /**
     * Create a DateRange object from two localdates, with a default formatter
     * @param from the start date
     * @param to the end date
     * @return a daterange object with the supplied start and end dates
     */
    public static DateRange of(LocalDate from, LocalDate to) {
        return new DateRange(from, to);
    }

    @Override
    public String toString() {
        return this.from.format(formatter) + " - " + this.to.format(formatter);
    }
}
