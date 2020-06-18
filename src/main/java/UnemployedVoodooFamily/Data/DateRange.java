package UnemployedVoodooFamily.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Class to store a period of time, denoted by a from and to date.
 * Includes helper methods to compare with other DateRanges
 */
public class DateRange {
    private DateTimeFormatter formatter;
    private LocalDate from;
    private LocalDate to;

    public DateRange(LocalDate from, LocalDate to, DateTimeFormatter formatter) {
        this.formatter = formatter;
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
        boolean inRange = false;
        if(this.from.isAfter(otherRange.from) && this.from.isBefore(otherRange.to) || this.from.equals(otherRange.to)) {
            inRange = true;
        }
        return inRange;
    }

    /**
     * Check if "to" value overlaps with another DateRange
     * @param otherRange the DateRange to compare with
     * @return true/false
     */
    public boolean toValueInRange(DateRange otherRange) {
        boolean inRange = false;
        if(this.to.isAfter(otherRange.from) && this.to.isBefore(otherRange.to) || this.to.equals(otherRange.from)) {
            inRange = true;
        }
        return inRange;
    }

    /**
     * Check if this DateRange encapsulates another DateRange
     * @param otherRange the DateRange to compare with
     * @return true/false
     */
    public boolean isEncapsulating(DateRange otherRange) {
        boolean encapsulating = false;
        if(isBeforeOrEqual(this.from, otherRange.from) && isAfterOrEqual(this.to, otherRange.to)) {
            encapsulating = true;
        }
        return encapsulating;
    }


    /**
     * Check if this a LocalDate object is after or equal to another LocalDate
     * @param date the date you want to check if is after or equal
     * @param otherDate the date to check against
     * @return true or false
     */
    private boolean isAfterOrEqual(LocalDate date, LocalDate otherDate) {
        boolean afterOrEqual = false;
        if(date.isAfter(otherDate) || date.equals(otherDate)) {
            afterOrEqual = true;
        }
        return afterOrEqual;
    }

    /**
     * Check if this a LocalDate object is before or equal to another LocalDate
     * @param date the date you want to check if is before or equal
     * @param otherDate the date to check against
     * @return true or false
     */
    private boolean isBeforeOrEqual(LocalDate date, LocalDate otherDate) {
        boolean afterOrEqual = false;
        if(date.isBefore(otherDate) || date.equals(otherDate)) {
            afterOrEqual = true;
        }
        return afterOrEqual;
    }

    /**
     * Create a daterange from a string, using the supplied format
     * @param rangeStr the string to convert to a DateRange
     * @param formatter the formatter to use for conversion
     * @return a daterange object
     */
    public static DateRange ofString(String rangeStr, DateTimeFormatter formatter) {
        String[] dates = rangeStr.split(" - ");
        return new DateRange(LocalDate.parse(dates[0], formatter), LocalDate.parse(dates[1], formatter), formatter);
    }

    /**
     * Check if the given date is in the range of this daterange.
     * @param date the date to check
     * @return true or false
     */
    public boolean contains(LocalDate date) {
        boolean contains = false;
        if(isAfterOrEqual(date, from) && isBeforeOrEqual(date, to)) {
            contains = true;
        }
        return contains;
    }

    /**
     * Create a DateRange object from two localdates, with a default formatter
     * @param from the start date
     * @param to the end date
     * @return a daterange object with the supplied start and end dates
     */
    public static DateRange of(LocalDate from, LocalDate to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return new DateRange(from, to, formatter);
    }

    @Override
    public String toString() {
        return this.from.format(formatter) + " - " + this.to.format(formatter);
    }
}
