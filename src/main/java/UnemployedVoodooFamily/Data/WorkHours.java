package UnemployedVoodooFamily.Data;

import java.time.LocalDate;

public class WorkHours {

    private LocalDate from;
    private LocalDate to;
    private Double hours;
    private String comment;

    public WorkHours(LocalDate from, LocalDate to, Double hours) {
        this.from = from;
        this.to = to;
        this.hours = hours;
        this.comment = "";
    }

    public WorkHours(LocalDate from, LocalDate to, Double hours, String comment) {
        this.from = from;
        this.to = to;
        this.hours = hours;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public DateRange getRange() {
        return DateRange.of(this.from, this.to);
    }
}
