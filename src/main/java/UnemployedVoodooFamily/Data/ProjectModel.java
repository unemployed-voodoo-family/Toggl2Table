package UnemployedVoodooFamily.Data;

import javafx.beans.value.ObservableValue;

import java.util.HashMap;
import java.util.Map;

/**
 * A class representing a model for one row of a project-summary table
 */
public class ProjectModel {
    private final Map<String, Double> monthHours = new HashMap<>();
    private final String projectName;

    public ProjectModel(String projectName) {
        this.projectName = projectName;
    }

    public void addHours(String month, double hours) {
        Double accumulatedHours = monthHours.get(month);
        if (accumulatedHours == null) {
            accumulatedHours = 0.0;
        }
        accumulatedHours += hours;
        monthHours.put(month, accumulatedHours);
    }

    /**
     * Get name of the project
     * @return Name of the project
     */
    public String getName() {
        return projectName;
    }

    /**
     * Get accumulated hours for a particular month
     * @param month Month name
     * @return hours, or 0 if no information found for the given month
     */
    public double getMonthHours(String month) {
        Double hours = monthHours.get(month);
        return hours != null ? hours : 0;
    }
}
