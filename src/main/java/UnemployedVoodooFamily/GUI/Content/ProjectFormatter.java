package UnemployedVoodooFamily.GUI.Content;

import UnemployedVoodooFamily.Data.ProjectModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ProjectFormatter {
    public static Callback<TableColumn.CellDataFeatures<ProjectModel, String>, ObservableValue<String>> getNameInstance() {
        return param -> {
            ProjectModel project = param.getValue();
            if (project != null) {
                return new SimpleStringProperty(project.getName());
            } else {
                return null;
            }
        };
    }
}
