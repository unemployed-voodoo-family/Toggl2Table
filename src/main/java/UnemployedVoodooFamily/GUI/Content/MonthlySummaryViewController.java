package UnemployedVoodooFamily.GUI.Content;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MonthlySummaryViewController {

    @FXML
    private Label headerLabel;
    @FXML
    private Label hoursWorkedLabel;
    @FXML
    private Label overtimeWorkedLabel;

    private String title;

    public MonthlySummaryViewController() {
        this.title = "Monthly report";
    }

    public <T extends Pane> T loadFXML() throws IOException {
        URL r = getClass().getClassLoader().getResource("view\\" + "monthlyTableSummary.fxml");
        return FXMLLoader.load(r);
    }

    public void initialize() {
        headerLabel.setText(this.title);
    }
}
