package UnemployedVoodooFamily.Data;

import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WorkHoursData{


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. LLLL yyyy");
    private SimpleObjectProperty<LocalDate> from;
    private SimpleObjectProperty<LocalDate> to;
    private SimpleObjectProperty<Double> hours;
    private SimpleObjectProperty<String> note;
    private WorkHours workHours;

    public WorkHoursData(WorkHours wh) {
        this.workHours = wh;
        this.from = new SimpleObjectProperty<>(wh.getFrom());
        this.to = new SimpleObjectProperty<>(wh.getTo());
        this.hours = new SimpleObjectProperty<>(wh.getHours());
        this.note = new SimpleObjectProperty<>(wh.getNote());
    }

    public LocalDate getFrom() {
        return from.get();
    }

    public SimpleObjectProperty<LocalDate> fromProperty() {
        return from;
    }

    public LocalDate getTo() {
        return to.get();
    }

    public SimpleObjectProperty<LocalDate> toProperty() {
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

    public WorkHours getWorkHours() {
        return this.workHours;
    }
}
