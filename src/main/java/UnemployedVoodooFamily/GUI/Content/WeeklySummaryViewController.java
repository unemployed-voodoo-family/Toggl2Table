package UnemployedVoodooFamily.GUI.Content;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public class WeeklySummaryViewController{

    @FXML
    private Label headerLabel;
    @FXML
    private Label hoursWorkedLabel;
    @FXML
    private Label overtimeWorkedLabel;
    @FXML
    private GridPane root;

    private String title;
    public WeeklySummaryViewController() {
        this.title = "Weekly report";
    }

    public <T extends Pane> T loadFXML() throws IOException {
        URL r = getClass().getClassLoader().getResource("weeklyTableSummary.fxml");
        return FXMLLoader.load(r);
    }
    public void initialize() {
        headerLabel.setText(this.title);
    }
}
