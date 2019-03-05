package UnemployedVoodooFamily.GUI.Content;

import UnemployedVoodooFamily.Data.MonthlyFormattedTimeData;
import UnemployedVoodooFamily.Data.RawTimeDataModel;
import UnemployedVoodooFamily.Data.WeeklyFormattedTimeDataModel;
import UnemployedVoodooFamily.Data.DateRange;
import UnemployedVoodooFamily.Logic.FormattedTimeDataLogic;
import UnemployedVoodooFamily.Logic.RawTimeDataLogic;
import ch.simas.jtoggl.JToggl;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.TimeEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class TableViewController {

    @FXML
    private Tab rawDataTab;

    @FXML
    private Tab formattedDataTab;

    @FXML
    private TableView rawData;

    @FXML
    private TableView formattedData;

    @FXML
    private ToggleButton weeklyToggleBtn;

    @FXML
    private ToggleButton monthlyToggleBtn;

    private final ToggleGroup timeSpanToggleGroup = new ToggleGroup();

    private RawTimeDataLogic rawTimeDataLogic = new RawTimeDataLogic();
    private FormattedTimeDataLogic formattedTimeDataLogic = new FormattedTimeDataLogic();

    public Node loadFXML() throws IOException {
        URL r = getClass().getClassLoader().getResource("Table.fxml");
        return FXMLLoader.load(r);
    }

    // |##################################################|
    // |                GENERAL METHODS                   |
    // |##################################################|

    public void initialize() {
        setupUIElements();
        setKeyAndClickListeners();
        buildRawDataTable();
    }

    /**
     * Sets up the UI elements
     */
    private void setupUIElements() {
        weeklyToggleBtn.setToggleGroup(timeSpanToggleGroup);
        monthlyToggleBtn.setToggleGroup(timeSpanToggleGroup);
        weeklyToggleBtn.setSelected(true);
    }

    /**
     * Sets input actions on UI elements
     */
    private void setKeyAndClickListeners() {
        rawDataTab.setOnSelectionChanged(event -> {
            if(rawDataTab.isSelected()) {
                buildRawDataTable();
            }
        });

        formattedDataTab.setOnSelectionChanged(event -> {
            if(formattedDataTab.isSelected()) {
                buildFormattedDataTable();
            }
        });

        weeklyToggleBtn.setOnAction((ActionEvent e) -> buildFormattedDataTable());
        monthlyToggleBtn.setOnAction((ActionEvent e) -> buildFormattedDataTable());
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
        rawData.getItems().setAll(getObservableRawData());
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
    private void buildFormattedDataTable() {
        //Check which button is active and build corresponding table
        if(weeklyToggleBtn.isSelected()) {
            buildFormattedWeeklyTable();
        }
        else if(monthlyToggleBtn.isSelected()) {
            buildFormattedMonthlyTable();
        }
    }

    /**
     * Builds a formatted table with a weekly overview
     */
    @SuppressWarnings("Duplicates")
    private void buildFormattedWeeklyTable() {
        //Clears the already existing data in the table
        clearTable(formattedData);

        //Create all columns necessary
        TableColumn<WeeklyFormattedTimeDataModel, String> weekDayCol = new TableColumn<>("Week Day");
        weekDayCol.setCellValueFactory(new PropertyValueFactory<>("weekDay"));
        weekDayCol.setSortable(false);

        TableColumn<WeeklyFormattedTimeDataModel, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setSortable(false);

        TableColumn<WeeklyFormattedTimeDataModel, String> projectCol = new TableColumn<>("Project");
        projectCol.setCellValueFactory(new PropertyValueFactory<>("project"));
        projectCol.setSortable(false);

        TableColumn<WeeklyFormattedTimeDataModel, String> startTimeCol = new TableColumn<>("Start Time");
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        startTimeCol.setSortable(false);

        TableColumn<WeeklyFormattedTimeDataModel, String> endTimeCol = new TableColumn<>("End Time");
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        endTimeCol.setSortable(false);

        TableColumn<WeeklyFormattedTimeDataModel, String> workedHoursCol = new TableColumn<>("Worked Hours");
        workedHoursCol.setCellValueFactory(new PropertyValueFactory<>("workedHours"));
        workedHoursCol.setSortable(false);

        TableColumn<WeeklyFormattedTimeDataModel, String> supposedHoursCol = new TableColumn<>("Supposed Hours");
        supposedHoursCol.setCellValueFactory(new PropertyValueFactory<>("supposedHours"));
        supposedHoursCol.setSortable(false);

        TableColumn<WeeklyFormattedTimeDataModel, String> overtimeCol = new TableColumn<>("Overtime");
        overtimeCol.setCellValueFactory(new PropertyValueFactory<>("overtime"));
        overtimeCol.setSortable(false);

        //Adds the columns to the table and updates it
        formattedData.getColumns().addAll(weekDayCol, dateCol, projectCol, startTimeCol, endTimeCol, workedHoursCol,
                                          supposedHoursCol, overtimeCol);
        formattedData.getItems().setAll(getObservableWeeklyData());
        formattedData.setEditable(false);
    }

    /**
     * Builds a formatted table with monthly overview
     */
    @SuppressWarnings("Duplicates")
    private void buildFormattedMonthlyTable() {
        //Clears the already existing data in the table
        clearTable(formattedData);

        //Create all columns necessary
        TableColumn<MonthlyFormattedTimeData, String> monthCol = new TableColumn<>("Month");
        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        monthCol.setSortable(false);

        TableColumn<MonthlyFormattedTimeData, String> weekNumbCol = new TableColumn<>("Week Number");
        weekNumbCol.setCellValueFactory(new PropertyValueFactory<>("weekNumber"));
        weekNumbCol.setSortable(false);

        TableColumn<MonthlyFormattedTimeData, String> workedHoursCol = new TableColumn<>("Worked Hours");
        workedHoursCol.setCellValueFactory(new PropertyValueFactory<>("workedHours"));
        workedHoursCol.setSortable(false);

        TableColumn<MonthlyFormattedTimeData, String> supposedHoursCol = new TableColumn<>("Supposed Hours");
        supposedHoursCol.setCellValueFactory(new PropertyValueFactory<>("supposedHours"));
        supposedHoursCol.setSortable(false);

        TableColumn<MonthlyFormattedTimeData, String> overtimeCol = new TableColumn<>("Overtime");
        overtimeCol.setCellValueFactory(new PropertyValueFactory<>("overtime"));
        overtimeCol.setSortable(false);

        //Adds the columns to the table and updates it
        formattedData.getColumns().addAll(monthCol, weekNumbCol, workedHoursCol, supposedHoursCol, overtimeCol);
        formattedData.getItems().setAll(getObservableMonthlyData());
        formattedData.setEditable(false);
    }

    /**
     * Creates an observable list containing WeeklyTimeDataModel objects
     * @return an ObservableList containing WeeklyTimeDatModel objects
     */
    private ObservableList<WeeklyFormattedTimeDataModel> getObservableWeeklyData() {
        ObservableList<WeeklyFormattedTimeDataModel> observableList = FXCollections.observableArrayList();

        //TODO: - Remove the two lines below when formatted data import is implemented
        //      - Make FormattedTimeDataLogic return a list of the required information
        //This is here for testing purposes only
        formattedTimeDataLogic.buildObservableWeeklyTimeData();
        return observableList;
    }

    /**
     * Creates an observable list containing MonthlyTimeDataModel objects
     * @return an ObservableList containing MonthlyTimeDatModel objects
     */
    private ObservableList<MonthlyFormattedTimeData> getObservableMonthlyData() {
        ObservableList<MonthlyFormattedTimeData> observableList = FXCollections.observableArrayList();

        //TODO: - Remove the two lines below when formatted data import is implemented
        //      - Make FormattedTimeDataLogic return a list of the required information
        //This is here for testing purposes only
        observableList.add(new MonthlyFormattedTimeData("January", "1", "40", "37.5", "" + (40 - 37.5)));

        return observableList;
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
}
