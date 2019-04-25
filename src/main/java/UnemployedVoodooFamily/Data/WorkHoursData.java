package UnemployedVoodooFamily.Data;

import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WorkHoursData{


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. LLLL yyyy");
    private SimpleObjectProperty<String> from;
    private SimpleObjectProperty<String> to;
    private SimpleObjectProperty<Double> hours;
    private SimpleObjectProperty<String> note;

    public WorkHoursData(LocalDate from, LocalDate to, Double hours, String note) {
        this.from = new SimpleObjectProperty<>(from.format(formatter));
        this.to = new SimpleObjectProperty<>(to.format(formatter));
        this.hours = new SimpleObjectProperty<>(hours);
        this.note = new SimpleObjectProperty<>(note);
    }

    public String getFrom() {
        return from.get();
    }

    public SimpleObjectProperty<String> fromProperty() {
        return from;
    }

    public String getTo() {
        return to.get();
    }

    public SimpleObjectProperty<String> toProperty() {
        return to;
    }

    public double getHours() {
        return hours.get();
    }

    public SimpleObjectProperty<Double> hoursProperty() {
        return hours;
    }

    public String getNote() {
        return note.get();
    }

    public SimpleObjectProperty<String> noteProperty() {
        return note;
    }
}
