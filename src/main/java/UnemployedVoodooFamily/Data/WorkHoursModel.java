package UnemployedVoodooFamily.Data;

import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Wrapper for WorkHours, where each field is wrapped in a Properties object,
 * for use with JavaFX components.
 */
public class WorkHoursModel {
    private SimpleObjectProperty<LocalDate> from;
    private SimpleObjectProperty<LocalDate> to;
    private SimpleObjectProperty<Double> hours;
    private SimpleObjectProperty<String> note;
    private WorkHours workHours;

    public WorkHoursModel(WorkHours wh) {
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
