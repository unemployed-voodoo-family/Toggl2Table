package UnemployedVoodooFamily.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Class to store a period of time, denoted by a from and to date.
 * Includes helper methods to comapare with other DateRanges
 */
public class DateRange {
    private static final int STANDARD_YEAR = 2000;
    private LocalDate from;
    private LocalDate to;

    public DateRange(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
        normalize();
    }

    /**
     * Sets the same year value for every daterange
     */
    private void normalize() {
        this.from = LocalDate.of(STANDARD_YEAR, from.getMonth(), from.getDayOfMonth());
        this.to = LocalDate.of(STANDARD_YEAR, to.getMonth(), to.getDayOfMonth());
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


    private boolean isAfterOrEqual(LocalDate date, LocalDate otherDate) {
        boolean afterOrEqual = false;
        if(date.isAfter(otherDate) || date.equals(otherDate)) {
            afterOrEqual = true;
        }
        return afterOrEqual;
    }

    private boolean isBeforeOrEqual(LocalDate date, LocalDate otherDate) {
        boolean afterOrEqual = false;
        if(date.isBefore(otherDate) || date.equals(otherDate)) {
            afterOrEqual = true;
        }
        return afterOrEqual;
    }

    public static DateRange ofString(String rangeStr, DateTimeFormatter formatter) {
        String[] dates = rangeStr.split(" - ");
        return new DateRange(LocalDate.parse(dates[0], formatter), LocalDate.parse(dates[1], formatter));
    }
}
