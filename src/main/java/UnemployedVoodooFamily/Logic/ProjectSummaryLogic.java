package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.ProjectModel;
import ch.simas.jtoggl.TimeEntry;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A class for logic related to project summary data
 */
public class ProjectSummaryLogic {
    private static String[] MONTH_NAMES;

    // The year when Toggl was launched, year selection can't go below this one
    public static final int FIRST_TOGGL_YEAR = 2006;
    public static final int CURRENT_YEAR = LocalDate.now().getYear();
    private int selectedYear = CURRENT_YEAR;
    private final Map<String, ProjectModel> projects = new HashMap<>();

    /**
     * Set selected year which will be used by all following data selections etc
     * @param year
     */
    public void setSelectedYear(int year) {
        this.selectedYear = year;
    }

    /**
     * Take time entries, build project-wise data
     * @param timeEntries Time entries from Toggl
     * @return List with project-wise summaries
     */
    public List<ProjectModel> buildMasterData(List<TimeEntry> timeEntries) {
        buildProjectMonthTotals(timeEntries);
        return generateSummaryList();
    }

    /**
     * Build project-wise summary data, fill in data in the projects map
     * @param timeEntries
     */
    private void buildProjectMonthTotals(List<TimeEntry> timeEntries) {
        projects.clear();
        for(TimeEntry te: timeEntries) {
            if(te.getStart().getYear() == selectedYear && te.getProject() != null) {
                String projectName = te.getProject().getName();
                String month = formatMonthName(te.getStart().getMonth());
                ProjectModel projectData = projects.get(projectName);
                if(projectData == null) {
                    // First entry for the project
                    projectData = new ProjectModel(projectName);
                    projects.put(projectName, projectData);
                }
                projectData.addHours(month, (float) te.getDuration() / 3600.0);
            }
        }
    }

    /**
     * Generate a list containing project summary data (convert the projects map to a list)
     * @return A list with accumulated project summaries
     */
    private List<ProjectModel> generateSummaryList() {
        return new LinkedList<>(projects.values());
    }

    /**
     * Format a month in a unified way (capitalized name)
     * @param month A month object
     * @return String representation of the month, or null
     */
    public static String formatMonthName(Month month) {
        if(month == null) {
            return null;
        }

        String m = month.toString();
        return m.substring(0, 1).toUpperCase() + m.substring(1,3).toLowerCase();
    }

    /**
     * Get names of all 12 months
     * @return A list with "Jan" , "Feb", etc
     */
    public static String[] getMonthNames() {
        if (MONTH_NAMES == null) {
            MONTH_NAMES = new String[12];
            int i = 0;
            for(Month m: Month.values()) {
                MONTH_NAMES[i++] = ProjectSummaryLogic.formatMonthName(m);
            }
        }
        return MONTH_NAMES;
    }
}
