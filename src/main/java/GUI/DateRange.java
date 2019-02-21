package GUI;

import java.time.LocalDate;

public class DateRange {
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

    public boolean fromValueinRange(DateRange otherRange) {
        boolean inRange = false;
        if(this.from.isAfter(otherRange.from) && this.from.isBefore(otherRange.to) || this.from.equals(otherRange.to)) {
            inRange = true;
        }
        return inRange;
    }

    public boolean toValueInRange(DateRange otherRange) {
        boolean inRange = false;
        if(this.to.isAfter(otherRange.from) && this.to.isBefore(otherRange.to) || this.to.equals(otherRange.from)) {
            inRange = true;
        }
        return inRange;
    }

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
}
