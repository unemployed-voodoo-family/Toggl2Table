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
}
