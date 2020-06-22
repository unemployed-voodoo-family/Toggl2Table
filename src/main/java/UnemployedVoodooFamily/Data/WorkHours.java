package UnemployedVoodooFamily.Data;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.time.LocalDate;
import java.util.Objects;

/**
 * A configuration of "ordinary work hours" for a given time period.
 */
public class WorkHours {

    private LocalDate from;
    private LocalDate to;
    private Double hours;
    private String note;

    /**
     * Construct the object.
     * @param from  Start of the period when the work hour config is valid
     * @param to    End of the period
     * @param hours How many work hours/day there are during this period
     */
    public WorkHours(LocalDate from, LocalDate to, Double hours) {
        if(from == null) {
            throw new IllegalArgumentException("FROM date can't be null!");
        }
        if(to == null) {
            throw new IllegalArgumentException("FROM date can't be null!");
        }
        this.from = from;
        this.to = to;
        this.hours = hours;
        this.note = "";
    }

    public WorkHours(LocalDate from, LocalDate to, Double hours, String note) {
        this.from = from;
        this.to = to;
        this.hours = hours;
        this.note = note;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public Double getHours() {
        return hours;
    }

    public String getNote() {
        return note;
    }

    public DateRange getRange() {
        return DateRange.of(this.from, this.to);
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkHours workHours = (WorkHours) o;
        return Objects.equals(from, workHours.from) && Objects.equals(to, workHours.to) && Objects
                .equals(hours, workHours.hours) && Objects.equals(note, workHours.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, hours, note);
    }

    @Override
    public String toString() {
        String date;
        if(to != null && ! to.equals(from)) {
            date = from.toString() + " - " + to.toString();
        }
        else {
            // A 1-day interval
            date = from.toString();
        }
        return date + ": " + hours + "h";
    }
}
