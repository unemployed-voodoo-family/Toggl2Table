package UnemployedVoodooFamily.GUI.Content;

import UnemployedVoodooFamily.Data.ProjectModel;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * A class used to create column value formatters (factories) for TableView
 */
public class TableColumnFormatting {
    /**
     * Create formatter for "Project name" column in project-wise report table
     * @return Formatter for project name column
     */
    public static Callback<TableColumn.CellDataFeatures<ProjectModel, String>, ObservableValue<String>> createNameFormatter() {
        return param -> {
            ProjectModel project = param.getValue();
            if(project != null) {
                return new SimpleStringProperty(project.getName());
            }
            else {
                return null;
            }
        };
    }

    /**
     * Create formatter for one month-hour column in project-wise report table
     * @param month Column for this month will be used
     * @return Formatter for a column for the given month
     */
    public static Callback<TableColumn.CellDataFeatures<ProjectModel, Number>, ObservableValue<Number>> createMonthFormatter(
            String month) {
        return param -> {
            ProjectModel project = param.getValue();
            if(project != null) {
                return new SimpleDoubleProperty(project.getMonthHours(month));
            }
            else {
                return null;
            }
        };
    }
}
