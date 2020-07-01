package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.ProjectModel;
import ch.simas.jtoggl.TimeEntry;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProjectSummaryLogic {
    public static final int FIRST_TOGGL_YEAR = 2006;
    public static final int CURRENT_YEAR = LocalDate.now().getYear();
    private int year = CURRENT_YEAR;
    private final Map<String, ProjectModel> projects = new HashMap<>();

    public void setSelectedYear(int year) {
        this.year = year;
    }

    public List<ProjectModel> buildMasterData(List<TimeEntry> timeEntries) {
        buildProjectMonthTotals(timeEntries);
        return generateSummaryList();
    }

    private void buildProjectMonthTotals(List<TimeEntry> timeEntries) {
        projects.clear();
        for(TimeEntry te: timeEntries) {
            if(te.getStart().getYear() == year && te.getProject() != null) {
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
     * Generate a list containing project summary data
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
}
