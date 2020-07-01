package UnemployedVoodooFamily.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * A class representing a model for one row of a project-summary table
 */
public class ProjectModel {
    private final Map<String, Double> monthHours = new HashMap<>();
    private final String name;

    /**
     * Create a project model
     * @param name name of the project
     */
    public ProjectModel(String name) {
        this.name = name;
    }

    /**
     * Add hours to a specific month for the project
     * @param month
     * @param hours
     * @return currently accumulated hours for the given month
     */
    public double addHours(String month, double hours) {
        Double accumulatedHours = monthHours.get(month);
        if (accumulatedHours == null) {
            accumulatedHours = 0.0;
        }
        accumulatedHours += hours;
        monthHours.put(month, accumulatedHours);
        return accumulatedHours;
    }

    /**
     * Get name of the project
     * @return Name of the project
     */
    public String getName() {
        return name;
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
