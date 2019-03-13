package UnemployedVoodooFamily.Data;

import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WorkHoursData{

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private SimpleObjectProperty<String> from;
    private SimpleObjectProperty<String> to;
    private SimpleObjectProperty<Double> hours;

    public WorkHoursData(LocalDate from, LocalDate to, Double hours) {
        this.from = new SimpleObjectProperty<>(from.format(formatter));
        this.to = new SimpleObjectProperty<>(to.format(formatter));
        this.hours = new SimpleObjectProperty<>(hours);
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
}
