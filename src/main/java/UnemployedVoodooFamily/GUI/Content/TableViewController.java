package UnemployedVoodooFamily.GUI.Content;

import UnemployedVoodooFamily.Data.Enums.Data;
import UnemployedVoodooFamily.Data.WeeklyFormattedDataModel;
import UnemployedVoodooFamily.Data.RawTimeDataModel;
import UnemployedVoodooFamily.Data.DailyFormattedDataModel;
import UnemployedVoodooFamily.Logic.FormattedTimeDataLogic;
import UnemployedVoodooFamily.Logic.Listeners.DataLoadedListener;

import UnemployedVoodooFamily.Logic.RawTimeDataLogic;
import UnemployedVoodooFamily.Logic.Session;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class TableViewController implements DataLoadedListener {

    @FXML
    private Tab rawDataTab;

    @FXML
    private Tab formattedDataTab;

    @FXML
    private TableView rawData;

    @FXML
    private TableView weeklyTable;

    @FXML
    private ToggleButton weeklyToggleBtn;

    @FXML
    private ToggleButton monthlyToggleBtn;

    @FXML
    private Button exportBtn;

    @FXML
    private GridPane tableRoot;

    @FXML
    private Label rawStartDate;
    @FXML
    private Label rawEndDate;

    private TableView monthlyTable;


    private final ToggleGroup timeSpanToggleGroup = new ToggleGroup();

    private RawTimeDataLogic rawTimeDataLogic = new RawTimeDataLogic();
    private FormattedTimeDataLogic formattedTimeDataLogic = new FormattedTimeDataLogic();
    private EnumSet<Data> loadedData = EnumSet.noneOf(Data.class);

    public Node loadFXML() throws IOException {
        URL r = getClass().getClassLoader().getResource("Table.fxml");
        return FXMLLoader.load(r);
    }

    // |##################################################|
    // |                GENERAL METHODS                   |
    // |##################################################|

    public void initialize() {
        Session.getInstance().addListener(this);
        setupUIElements();
        setKeyAndClickListeners();
    }

    /**
     * Sets up the UI elements
     */
    private void setupUIElements() {
        buildFormattedMonthlyTable();
        buildFormattedWeeklyTable();
        buildRawDataTable();
        weeklyToggleBtn.setToggleGroup(timeSpanToggleGroup);
        monthlyToggleBtn.setToggleGroup(timeSpanToggleGroup);
        weeklyToggleBtn.setSelected(true);
    }

    /**
     * Sets input actions on UI elements
     */
    private void setKeyAndClickListeners() {
        exportBtn.setOnAction(event -> {
            formattedTimeDataLogic.exportToExcelDocument();
        });


        formattedDataTab.setOnSelectionChanged(event -> {
            if(formattedDataTab.isSelected()) {
                buildWeeklyTable();
            }
        });


        weeklyToggleBtn.setOnAction((ActionEvent e) -> switchTableView(weeklyTable));
        monthlyToggleBtn.setOnAction((ActionEvent e) -> switchTableView(monthlyTable));
    }

    // |##################################################|
    // |               RAW DATA TABLE METHODS             |
    // |##################################################|

    /**
     * Builds the viewable table of all the raw from the Toggl user's data
     */
    private void buildRawDataTable() {
        //Clears the already existing data in the table
        clearTable(rawData);

        //Create all columns necessary
        TableColumn<RawTimeDataModel, String> projectCol = new TableColumn<>("Project");
        projectCol.setCellValueFactory(new PropertyValueFactory<>("project"));

        TableColumn<RawTimeDataModel, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<RawTimeDataModel, String> startDateCol = new TableColumn<>("Start Date");
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<RawTimeDataModel, String> startTimeCol = new TableColumn<>("Start Time");
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        TableColumn<RawTimeDataModel, String> endDateCol = new TableColumn<>("End Date");
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        TableColumn<RawTimeDataModel, String> endTimeCol = new TableColumn<>("End Time");
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        TableColumn<RawTimeDataModel, String> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));

        //Adds the columns to the table and updates it
        rawData.setEditable(true);
        rawData.getColumns()
               .addAll(projectCol, descCol, startDateCol, startTimeCol, endDateCol, endTimeCol, durationCol);
    }

    private void setRawDataTableData() {
        rawData.getItems().setAll(getObservableRawData());
        Platform.runLater(() -> {
            rawEndDate.setText(rawTimeDataLogic.getDataEndTime());
            rawStartDate.setText(rawTimeDataLogic.getDataStartTime());
        });

    }

    private void setFormattedTableData() {
        weeklyTable.getItems().setAll(getObservableWeeklyData());
        monthlyTable.getItems().setAll(getObservableMonthlyData());
    }

    /**
     * Creates an observable list containing RawTimeDataModel objects
     * @return an ObservableList containing RawTimeDatModel objects
     */
    private ObservableList<RawTimeDataModel> getObservableRawData() {
        return rawTimeDataLogic.buildObservableRawTimeData();
    }

    // |##################################################|
    // |           FORMATTED DATA TABLE METHODS           |
    // |##################################################|

    /**
     * Builds the table for the formatted table tab. Builds either weekly or monthly depending on which is selected by
     * the toggle buttons
     */
    private void buildWeeklyTable() {
        buildFormattedWeeklyTable();
    }

    private void buildMonthlyTable() {
        buildFormattedMonthlyTable();
    }

    /**
     * Builds a formatted table with a weekly overview
     */
    @SuppressWarnings("Duplicates")
    private void buildFormattedWeeklyTable() {
        //Clears the already existing data in the table
        clearTable(weeklyTable);

        //Create all columns necessary
        TableColumn<DailyFormattedDataModel, String> weekDayCol = new TableColumn<>("Week Day");
        weekDayCol.setCellValueFactory(new PropertyValueFactory<>("weekDay"));
        weekDayCol.setSortable(false);

        TableColumn<DailyFormattedDataModel, String> workedHoursCol = new TableColumn<>("Worked Hours");
        workedHoursCol.setCellValueFactory(new PropertyValueFactory<>("workedHours"));
        workedHoursCol.setSortable(false);

        TableColumn<DailyFormattedDataModel, String> supposedHoursCol = new TableColumn<>("Supposed Hours");
        supposedHoursCol.setCellValueFactory(new PropertyValueFactory<>("supposedHours"));
        supposedHoursCol.setSortable(false);

        TableColumn<DailyFormattedDataModel, String> overtimeCol = new TableColumn<>("Overtime");
        overtimeCol.setCellValueFactory(new PropertyValueFactory<>("overtime"));
        overtimeCol.setSortable(false);

        //Adds the columns to the table and updates it
        weeklyTable.getColumns().addAll(weekDayCol, workedHoursCol, supposedHoursCol, overtimeCol);
        weeklyTable.setEditable(false);
    }

    /**
     * Builds a formatted table with monthly overview
     */
    @SuppressWarnings("Duplicates")
    private void buildFormattedMonthlyTable() {
        //Clears the already existing data in the table
        this.monthlyTable = new TableView();
        clearTable(weeklyTable);

        //Create all columns necessary

        TableColumn<WeeklyFormattedDataModel, String> weekNumbCol = new TableColumn<>("Week Number");
        weekNumbCol.setCellValueFactory(new PropertyValueFactory<>("weekNumber"));
        weekNumbCol.setSortable(false);

        TableColumn<WeeklyFormattedDataModel, String> workedHoursCol = new TableColumn<>("Worked Hours");
        workedHoursCol.setCellValueFactory(new PropertyValueFactory<>("workedHours"));
        workedHoursCol.setSortable(false);

        TableColumn<WeeklyFormattedDataModel, String> supposedHoursCol = new TableColumn<>("Supposed Hours");
        supposedHoursCol.setCellValueFactory(new PropertyValueFactory<>("supposedHours"));
        supposedHoursCol.setSortable(false);

        TableColumn<WeeklyFormattedDataModel, String> overtimeCol = new TableColumn<>("Overtime");
        overtimeCol.setCellValueFactory(new PropertyValueFactory<>("overtime"));
        overtimeCol.setSortable(false);

        //Adds the columns to the table and updates it
        monthlyTable.getColumns().addAll(weekNumbCol, workedHoursCol, supposedHoursCol, overtimeCol);
        monthlyTable.setEditable(false);
    }

    /**
     * Creates an observable list containing WeeklyTimeDataModel objects
     * @return an ObservableList containing WeeklyTimeDatModel objects
     */
    private ObservableList<DailyFormattedDataModel> getObservableWeeklyData() {
        return formattedTimeDataLogic.buildObservableWeeklyTimeData();
    }

    /**
     * Creates an observable list containing MonthlyTimeDataModel objects
     * @return an ObservableList containing MonthlyTimeDatModel objects
     */
    private ObservableList<WeeklyFormattedDataModel> getObservableMonthlyData() {
        return formattedTimeDataLogic.buildObservableMonthlyTimeData();
    }

    // |##################################################|
    // |                  OTHER METHODS                   |
    // |##################################################|

    /**
     * Clears the selected TableView
     * @param table the TableView to clear
     */
    private void clearTable(TableView table) {
        table.getColumns().clear();
        table.getItems().clear();
    }

    private void switchTableView(Node content) {
        ObservableList<Node> children = tableRoot.getChildren();
        if(children.isEmpty()) {
            children.addAll(content);
        }
        else if(! children.contains(content)) {
            children.clear();
            children.addAll(content);
        }
    }


    /**
     * Set newly loaded data to the tables, only when
     * all the necessary data has been loaded
     * @param e
     */
    @Override
    public void dataLoaded(Data e) {
        loadedData.add(e);
        //check if necassary data is loaded
        if(loadedData.containsAll(EnumSet.of(Data.TIME_ENTRIES))) {
            setFormattedTableData();
            if(loadedData.containsAll(EnumSet.of(Data.TIME_ENTRIES, Data.PROJECTS, Data.TASKS))) {
                setRawDataTableData();
                loadedData = EnumSet.noneOf(Data.class); //empty the set, readying it for next
            }
        }

    }
}
