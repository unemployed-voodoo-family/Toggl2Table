package UnemployedVoodooFamily.GUI.Content;

import UnemployedVoodooFamily.Data.ProjectModel;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ProjectFormatter {
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
