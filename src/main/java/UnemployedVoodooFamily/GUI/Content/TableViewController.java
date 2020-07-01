package UnemployedVoodooFamily.GUI.Content;

import UnemployedVoodooFamily.Data.DailyFormattedDataModel;
import UnemployedVoodooFamily.Data.Enums.Data;
import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Data.ProjectModel;
import UnemployedVoodooFamily.Data.RawTimeDataModel;
import UnemployedVoodooFamily.Logic.FormattedTimeDataLogic;
import UnemployedVoodooFamily.Logic.Listeners.DataLoadListener;
import UnemployedVoodooFamily.Logic.ProjectSummaryLogic;
import UnemployedVoodooFamily.Logic.RawTimeDataLogic;
import UnemployedVoodooFamily.Logic.Session;
import ch.simas.jtoggl.Client;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.Workspace;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;
import org.threeten.extra.YearWeek;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.*;

public class TableViewController<Content extends Pane> implements DataLoadListener {

    @FXML
    public Label projectYearSpinnerLabel;

    @FXML
    public Spinner<Integer> projectYearSpinner;

    @FXML
    public ComboBox<Integer> projectYearDropdown;

    @FXML
    private Tab rawDataTab;

    @FXML
    private ProgressIndicator exportProgressIndicator;

    @FXML
    private Tab formattedDataTab;

    @FXML
    private TableView<RawTimeDataModel> rawData;

    @FXML
    private Label excelFeedbackLabel;

    @FXML
    private MenuItem weeklyToggleBtn;

    @FXML
    private MenuItem monthlyToggleBtn;

    @FXML
    private Button exportBtn;

    @FXML
    private GridPane tableRoot;

    @FXML
    private GridPane projectRoot;

    @FXML
    private DatePicker rawStartDate;
    @FXML
    private DatePicker rawEndDate;

    @FXML
    private VBox filterBox;
    @FXML
    private MenuButton projectFilterBtn;
    @FXML
    private MenuButton workspaceFilterBtn;
    @FXML
    private MenuButton clientFilterBtn;
    @FXML
    private Button applyFilterBtn;
    @FXML
    private HBox root;
    @FXML
    private MenuButton summarySelectionBtn;

    @FXML
    private Spinner<Integer> yearSpinner;
    @FXML
    private Spinner<Integer> weekSpinner;
    @FXML
    private Spinner<SimpleObjectProperty<Month>> monthSpinner;

    @FXML
    private ComboBox<Integer> yearlyDropdown;
    @FXML
    private ComboBox<Integer> weeklyDropdown;
    @FXML
    private ComboBox<SimpleObjectProperty<Month>> monthlyDropdown;

    @FXML
    private ImageView feedbackImg;

    @FXML
    private TableView<DailyFormattedDataModel> monthlyTable;
    @FXML
    private TableView<DailyFormattedDataModel> weeklyTable;
    @FXML
    public TableView<ProjectModel> projectDataTable;

    @FXML
    private Label yearSpinnerLabel;
    @FXML
    private Label timePeriodSpinnerLabel;

    @FXML
    private Tooltip errorTooltip;
    @FXML
    private Button explorerBtn;

    @FXML
    private Label hoursWorkedLabel;
    @FXML
    private Label extraTimeWorkedLabel;

    private ImageView successImg;
    private ImageView errorImg;

    private final RawTimeDataLogic rawTimeDataLogic = new RawTimeDataLogic();
    private final FormattedTimeDataLogic formattedTimeDataLogic = new FormattedTimeDataLogic();
    private final ProjectSummaryLogic projectLogic = new ProjectSummaryLogic();
    private final EnumSet<Data> loadedData = EnumSet.noneOf(Data.class);

    private final Set<Object> filterOptions = new HashSet<>();

    private static final DecimalFormat df = new DecimalFormat("#0.00");



    public Node loadFXML() throws IOException {
        URL r = getClass().getClassLoader().getResource("Table.fxml");
        if (r == null) {
            throw new IOException("Could not load the FXML file");
        }
        return FXMLLoader.load(r);
    }

    //region Initialization

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

        // set up table views
        prepareFormattedWeeklyTable();
        prepareFormattedMonthlyTable();
        prepareRawDataTable();
        prepareProjectSummaryTable();

        // set uo UI elements for each table
        setupFormattedTableUIElements();
        setupRawTableUIElements();
        setupProjectTableUIElements();

        //initialize the filter buttons
        initializeFilterButton(clientFilterBtn);
        initializeFilterButton(projectFilterBtn);
        initializeFilterButton(workspaceFilterBtn);

        // load success and error images
        URL successUrl = getClass().getClassLoader()
                                   .getResource("icons/baseline_check_circle_black_24dp.png");
        if(successUrl != null) {
            Image success = new Image(successUrl.toString());
            successImg = new ImageView(success);
        }

        successImg.setFitWidth(24);
        successImg.setFitHeight(24);
        URL errorUrl = getClass().getClassLoader()
                                 .getResource("icons/baseline_error_black_24dp.png");
        if(errorUrl != null) {
            Image error = new Image(errorUrl.toString());
            errorImg = new ImageView(error);
        }

        errorImg.setFitWidth(24);
        errorImg.setFitHeight(24);

    }

    private void setupFormattedTableUIElements() {
        weeklyToggleBtn.fire();

        yearSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        weekSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        monthSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);

        int firstTogglYear = 2006;
        //Sets default values for the spinners
        //Change WEEKLY in case default formatted view changes
        yearSpinner.setValueFactory(ValueFactory.getYearValueFactory());
        weekSpinner.setValueFactory(ValueFactory.getWeekValueFactory(formattedTimeDataLogic.getSelectedWeek()));

        //Creates a list with all months for the monthly spinner to use
        monthSpinner.setValueFactory(ValueFactory.getMonthValueFactory());
        monthSpinner.getValueFactory().setValue(ValueFactory.getCurrentMonth());

        StringConverter<SimpleObjectProperty<Month>> converter = new StringConverter<SimpleObjectProperty<Month>>() {
            @Override
            public String toString(SimpleObjectProperty<Month> object) {
                return object.getValue().getDisplayName(TextStyle.FULL, Locale.getDefault());
            }

            @Override
            public SimpleObjectProperty<Month> fromString(String string) {
                return new SimpleObjectProperty<>(Month.valueOf(string));
            }
        };

        // change how objects are displayed
        monthlyDropdown.setConverter(converter);
        monthSpinner.getValueFactory().setConverter(converter);


        //set initial value
        monthSpinner.getEditor()
                    .setText(Month.from(LocalDate.now()).getDisplayName(TextStyle.FULL, Locale.getDefault()));

        //Hide the Monthly spinner by default
        updateMonthlySpinner(false);

        timePeriodSpinnerLabel.setText("Week");

        //Dropdown menus for the spinners
        yearlyDropdown.setVisible(false);
        yearlyDropdown.getItems().addAll(ValueFactory.getYearsAsList());
        weeklyDropdown.setVisible(false);
        weeklyDropdown.getItems().addAll(ValueFactory.getWeeksAsList());
        monthlyDropdown.setVisible(false);
        monthlyDropdown.getItems().addAll(ValueFactory.getMonthList());
    }

    private void setupProjectTableUIElements() {
        projectYearSpinner.setValueFactory(ValueFactory.getYearValueFactory());
        projectYearDropdown.getItems().addAll(ValueFactory.getYearsAsList());
    }

    private void setupRawTableUIElements() {
        rawStartDate.setValue(LocalDate.now().minusWeeks(1));
        rawStartDate.setDisable(true);
        rawEndDate.setValue(LocalDate.now());
        rawEndDate.setDisable(true);
    }

    /**
     * Sets input actions on UI elements
     */
    private void setKeyAndClickListeners() {

        // update formatted tables when tableview gui is shown
        root.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue == null && newValue != null && formattedTimeDataLogic.getMonthlyMasterData() != null) {
                updateFormattedTableData();
            }
        });

        bindTooltip(excelFeedbackLabel, errorTooltip);
        applyFilterBtn.setOnAction(event -> applyFilters());
        initExcelExportBtn();

        rawStartDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isAfter(rawEndDate.getValue())) {
                rawStartDate.getStyleClass().add("error");
            }
            else if(! newValue.isEqual(oldValue) && ! newValue.isEqual(rawTimeDataLogic.getFilteredDataStartDate())) {
                rawTimeDataLogic.setDataStartDate(rawStartDate.getValue());
                if(! rawEndDate.getValue().isEqual(rawTimeDataLogic.getFilteredDataEndDate())) {
                    rawTimeDataLogic.setDataEndDate(rawEndDate.getValue());
                }
                setRawDataTableData();
                rawStartDate.getStyleClass().remove("error");
                rawEndDate.getStyleClass().remove("error");
            }
            else if(newValue.isEqual(rawEndDate.getValue()) || oldValue.isEqual(newValue)) {
                rawStartDate.getStyleClass().remove("error");
                rawEndDate.getStyleClass().remove("error");
            }
        });

        rawEndDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isBefore(rawStartDate.getValue())) {
                rawEndDate.getStyleClass().add("error");
            }
            else if(! newValue.isEqual(oldValue) && newValue != rawTimeDataLogic.getFilteredDataEndDate()) {
                rawTimeDataLogic.setDataEndDate(rawEndDate.getValue());
                if(! rawStartDate.getValue().isEqual(rawTimeDataLogic.getFilteredDataStartDate())) {
                    rawTimeDataLogic.setDataStartDate(rawStartDate.getValue());
                }
                setRawDataTableData();
                rawStartDate.getStyleClass().remove("error");
                rawEndDate.getStyleClass().remove("error");
            }
            else if(newValue.isEqual(rawStartDate.getValue()) || oldValue.isEqual(newValue)) {
                rawStartDate.getStyleClass().remove("error");
                rawEndDate.getStyleClass().remove("error");
            }
        });

        weeklyToggleBtn.setOnAction((ActionEvent e) -> {
            showContentInParentContainer(tableRoot, weeklyTable);
            summarySelectionBtn.setText("Weekly Summary");
            //switchView(summaryRoot, weeklySummary);
            updateWeeklySpinner(true);
            updateMonthlySpinner(false);
            timePeriodSpinnerLabel.setText("Week");
            updateFormattedTableData();
        });
        monthlyToggleBtn.setOnAction((ActionEvent e) -> {
            showContentInParentContainer(tableRoot, monthlyTable);
            summarySelectionBtn.setText("Monthly Summary");
            //switchView(summaryRoot, monthlySummary);
            updateWeeklySpinner(false);
            updateMonthlySpinner(true);
            timePeriodSpinnerLabel.setText("Month");
            updateFormattedTableData();
        });

        //Year Spinner + Dropdown
        initSpinnerAndDropdownClickEvents(yearSpinner, yearlyDropdown);
        yearSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            formattedTimeDataLogic.setSelectedYear(newValue);
            updateFormattedTableData();
        });

        //Week Spinner + Dropdown
        initSpinnerAndDropdownClickEvents(weekSpinner, weeklyDropdown);
        weekSpinner.valueProperty().addListener((observable, oldWeek, newWeek) -> {
            if ((int) newWeek != oldWeek) {
                formattedTimeDataLogic.setSelectedWeek(newWeek);
                updateFormattedTableData();
            }
        });

        //Month Spinner + Dropdown
        initSpinnerAndDropdownClickEvents(monthSpinner, monthlyDropdown);
        monthSpinner.valueProperty().addListener((observable, oldMonth, newMonth) -> {
            if (newMonth != null && !newMonth.equals(oldMonth)) {
                formattedTimeDataLogic.setSelectedMonth(newMonth.get());
                updateFormattedTableData();
            }
        });

        // Project year spinner + dropdown
        initSpinnerAndDropdownClickEvents(projectYearSpinner, projectYearDropdown);
        projectYearSpinner.valueProperty().addListener((observable, oldYear, newYear) -> {
            if ((int) newYear != oldYear) {
                projectLogic.setSelectedYear(newYear);
                updateProjectTableData();
            }
        });


        explorerBtn.setOnAction(event -> {
            try {
                Desktop.getDesktop().open(new File(FilePath.APP_HOME.getPath()));
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            catch(IllegalArgumentException e) {
                //could not find path
            }
        });
    }

    /**
     * Initialize click listeners for two connected components: a dropdown with numbers and a spinner which can change
     * the value in the dropdown.
     * @param spinner
     * @param dropdown
     */
    private void initSpinnerAndDropdownClickEvents(Spinner spinner, ComboBox dropdown) {
        spinner.getEditor().setOnMouseClicked((MouseEvent e) -> {
            spinner.getEditor().setVisible(false);
            dropdown.setVisible(true);
            dropdown.show();
        });
        dropdown.setOnHiding((Event e) -> {
            spinner.getEditor().setVisible(true);
            dropdown.setVisible(false);
            if(dropdown.getValue() != null) {
                spinner.getValueFactory().setValue(dropdown.getValue());
            }
        });
    }

    private void initExcelExportBtn() {
        exportBtn.setOnAction(event -> {
            //try to create excel file, and initialize user feedback
            Thread t = new Thread(() -> {
                exportBtn.setDisable(true);
                exportProgressIndicator.setVisible(true);

                try {
                    //create document
                    boolean success = formattedTimeDataLogic.exportToExcelDocument(formattedTimeDataLogic.getMonthlyMasterData(),
                                                                 Integer.parseInt(yearSpinner.getEditor().getText()));

                    //show success in ui
                    Platform.runLater(() -> {
                        if(success) {
                            showSuccessLabel(excelFeedbackLabel, "Excel document was successfully created");
                        }
                        else {
                            showErrorLabel(excelFeedbackLabel, "Cannot create Excel document while time entries are still downloading");
                        }
                        errorTooltip.setOpacity(0);
                    });
                }
                //show error in ui
                catch(IOException e) {
                    Platform.runLater(() -> {
                        showErrorLabel(excelFeedbackLabel, "Error creating Excel document");
                        errorTooltip.setText("Could not create Excel file.\n" +
                                             "Check that you have permission for the folder and that the file isn't already opened");
                        errorTooltip.setOpacity(.9);
                    });
                }
            });
            t.start();

            //wait for the task to complete, then set things back to normal
            Thread t1 = new Thread(() -> {
                try {
                    t.join();
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
                exportProgressIndicator.setVisible(false);
                exportBtn.setDisable(false);
            });
            t1.start();
        });
    }

    private void showSuccessLabel(Label label, String message) {
        label.setVisible(true);
        label.setGraphic(successImg);
        label.setText(message);
        label.getStyleClass().remove("error");
        label.getStyleClass().add("success");
    }


    private void showErrorLabel(Label label, String message) {
        label.setVisible(true);
        label.setGraphic(errorImg);
        label.setText(message);
        label.getStyleClass().remove("success");
        label.getStyleClass().add("error");
    }

    /**
     * Show tooltip on mouse entered
     * https://stackoverflow.com/questions/26854301/how-to-control-the-javafx-tooltips-delay
     * @param node
     * @param tooltip
     */
    public static void bindTooltip(final Node node, final Tooltip tooltip) {
        node.setOnMouseEntered(event -> {
            // +15 moves the tooltip 15 pixels below the mouse cursor;
            // if you don't change the y coordinate of the tooltip, you
            // will see constant screen flicker
            tooltip.show(node, event.getScreenX(), event.getScreenY() + 15);
        });
        node.setOnMouseExited(event -> tooltip.hide());
    }
    //endregion

    //region Raw data methods
    // |##################################################|
    // |               RAW DATA TABLE METHODS             |
    // |##################################################|

    /**
     * Sets data to the raw data table, using selected time period
     */
    private void setRawDataTableData() {
        rawData.getItems().setAll(buildObservableRawData());
        Platform.runLater(() -> {
            rawEndDate.setValue(rawTimeDataLogic.getFilteredDataEndDate());
            rawStartDate.setValue(rawTimeDataLogic.getFilteredDataStartDate());
            rawEndDate.setDisable(false);
            rawStartDate.setDisable(false);
        });
    }

    /**
     * Creates an observable list containing RawTimeDataModel objects
     * @return an ObservableList containing RawTimeDatModel objects
     */
    private ObservableList<RawTimeDataModel> buildObservableRawData() {
        Session session = Session.getInstance();
        return FXCollections.observableArrayList(rawTimeDataLogic.buildRawMasterData(session.getTimeEntries(),
                                                                                     session.getProjects(),
                                                                                     session.getWorkspaces(),
                                                                                     session.getClients(),
                                                                                     filterOptions));
    }
    //endregion

    //region Formatted data table methods
    // |##################################################|
    // |           FORMATTED DATA TABLE METHODS           |
    // |##################################################|

    /**
     * Update the currently visible table.
     */
    private void updateFormattedTableData() {
        if(rawTimeDataLogic.getMasterTimeEntries() == null) {
            return;
        }
        formattedTimeDataLogic.buildMasterData(rawTimeDataLogic.getFilteredTimeEntries());
        if(weekSpinner.isVisible()) {
            updateWeeklyTable();
        }
        else if (monthSpinner.isVisible()) {
            updateMonthlyTable();
        }
    }

    /**
     * Get data for currently selected year, generate project-wise report and show it in the table
     */
    private void updateProjectTableData() {
        if(rawTimeDataLogic.getMasterTimeEntries() == null) {
            return;
        }
        List<ProjectModel> data = projectLogic.buildMasterData(rawTimeDataLogic.getFilteredTimeEntries());
        ObservableList<ProjectModel> observableData = FXCollections.observableArrayList(data);
        showProjectDataInTable(observableData);
    }

    /**
     * Update the monthly table and summary labels
     */
    private void updateMonthlyTable() {
        ObservableList<DailyFormattedDataModel> data = createObservableMonthlyData();
        showDailyDataInTable(data, monthlyTable);
    }

    /**
     * Update the weekly table and summary labels.
     */
    private void updateWeeklyTable() {
        ObservableList<DailyFormattedDataModel> data = createObservableWeeklyData();
        showDailyDataInTable(data, weeklyTable);
    }

    /**
     * Update GUI: Show a day-by-day data in a summary table
     * @param data The daily data
     * @param tableView TableView where to display the data
     */
    private void showDailyDataInTable(ObservableList<DailyFormattedDataModel> data,
                                      TableView<DailyFormattedDataModel> tableView) {
        try {
            Platform.runLater(() -> {
                double[] values = calculateSummary(data);
                tableView.getItems().setAll(data);
                hoursWorkedLabel.setText(df.format(values[0]));
                extraTimeWorkedLabel.setText(df.format(values[1]));
            });
        }
        catch(IllegalStateException e) {
            // tried getting data before it was loaded
            System.out.println(e.getMessage());
        }
    }

    /**
     * Update GUI: Show a project-wise data in a summary table
     * @param data      Data for each project
     */
    private void showProjectDataInTable(ObservableList<ProjectModel> data) {
        try {
            Platform.runLater(() -> {
                projectDataTable.getItems().setAll(data);
            });
        }
        catch(IllegalStateException e) {
            // tried getting data before it was loaded
            System.out.println(e.getMessage());
        }
    }

    /**
     * Calculate hours worked and hours worked, respectively for the given dataset
     * @param data the dataset to calculate from
     * @return an array containing [1] hours worked and [2] overtime worked
     */
    private double[] calculateSummary(ObservableList<DailyFormattedDataModel> data) {
        double extraTime = 0d;
        double worked = 0d;

        for(DailyFormattedDataModel item : data) {
            extraTime += item.getExtraTime();
            worked += item.getWorkedHours();
        }
        extraTimeWorkedLabel.setText(df.format(extraTime));
        hoursWorkedLabel.setText(df.format(worked));

        return new double[]{worked, extraTime};
    }

    //region Tableview setup methods

    /**
     * Sets up a formatted table with a weekly overview
     */
    @SuppressWarnings("Duplicates")
    private void prepareFormattedWeeklyTable() {
        this.weeklyTable = new TableView<>();
        //Create all columns necessary

        TableColumn<DailyFormattedDataModel, String> weekdayCol = new TableColumn<>("Week Day");
        weekdayCol.setCellValueFactory(new PropertyValueFactory<>("weekday"));
        weekdayCol.setSortable(false);

        TableColumn<DailyFormattedDataModel, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setSortable(false);

        TableColumn<DailyFormattedDataModel, Double> workedHoursCol = new TableColumn<>("Worked Hours");
        workedHoursCol.setCellValueFactory(new PropertyValueFactory<>("workedHours"));
        workedHoursCol.setSortable(false);
        workedHoursCol.getStyleClass().add("right");
        workedHoursCol.setCellFactory(col -> setDecimalFormatter(df));


        TableColumn<DailyFormattedDataModel, Double> supposedHoursCol = new TableColumn<>("Ordinary work hours");
        supposedHoursCol.setCellValueFactory(new PropertyValueFactory<>("supposedHours"));
        supposedHoursCol.setSortable(false);
        supposedHoursCol.setCellFactory(col -> setDecimalFormatter(df));
        supposedHoursCol.getStyleClass().add("right");

        TableColumn<DailyFormattedDataModel, Double> accumulatedHoursCol = new TableColumn<>("Accumulated");
        accumulatedHoursCol.setCellValueFactory(new PropertyValueFactory<>("accumulatedHours"));
        accumulatedHoursCol.setCellFactory(col -> setDecimalFormatter(df));
        accumulatedHoursCol.setSortable(false);
        accumulatedHoursCol.getStyleClass().add("right");

        TableColumn<DailyFormattedDataModel, String> noteCol = new TableColumn<>("Notes");
        noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
        noteCol.setSortable(false);

        TableColumn<DailyFormattedDataModel, Double> extraTimeCol = new TableColumn<>("+/- Hours");
        extraTimeCol.setCellValueFactory(new PropertyValueFactory<>("extraTime"));
        extraTimeCol.setSortable(false);
        extraTimeCol.getStyleClass().add("right");
        extraTimeCol.setCellFactory(col -> new TableCell<DailyFormattedDataModel, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setText(null);
                    setGraphic(null);
                }
                else {
                    setText(df.format(item));
                    setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, Font.getDefault().getSize()));
                    if(item < 0.0) {
                        setTextFill(Color.RED); // or use setStyle(String)
                    }
                    else if(item > 0.0) {
                        setTextFill(Color.GREEN); // or use setStyle(String)
                    }
                    else {
                        setTextFill(Color.BLACK);
                    }
                }
            }
        });
        workedHoursCol.setPrefWidth(120);
        supposedHoursCol.setPrefWidth(140);
        extraTimeCol.setPrefWidth(90);
        //Adds the columns to the table and updates it
        this.weeklyTable.getColumns().addAll(weekdayCol, dateCol, supposedHoursCol, workedHoursCol, extraTimeCol,
                                             accumulatedHoursCol, noteCol);
        this.weeklyTable.setEditable(false);

        //must be called, or else the table won't appear
        showContentInParentContainer(tableRoot, weeklyTable);
    }

    /**
     * Sets up a formatted table with project summary
     */
    private void prepareProjectSummaryTable() {
        this.projectDataTable = new TableView<>();
        this.projectDataTable.setEditable(false);
        // Disable horizontal scrollbar (code from https://stackoverflow.com/questions/55093764/javafx-how-to-disable-scrollbars-in-tableview)
        this.projectDataTable.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });

        //Create all columns
        TableColumn<ProjectModel, String> nameCol = new TableColumn<>("Project");
        nameCol.setCellValueFactory(TableColumnFormatting.createNameFormatter());
        nameCol.setSortable(true);
        nameCol.setMinWidth(250);

        //Adds the columns to the table and updates it
        ObservableList<TableColumn<ProjectModel, ?>> columns = this.projectDataTable.getColumns();
        columns.add(nameCol);
        for (String month : ProjectSummaryLogic.getMonthNames()) {
            TableColumn<ProjectModel, Number> monthHourCol = new TableColumn<>(month);
            monthHourCol.setSortable(true);
            monthHourCol.setCellValueFactory(TableColumnFormatting.createMonthFormatter(month));
            monthHourCol.setPrefWidth(50);
            columns.add(monthHourCol);
        }

        //must be called, or else the table won't appear
        showContentInParentContainer(projectRoot, projectDataTable);
    }


    /**
     * Sets up a formatted table with monthly overview
     */
    @SuppressWarnings("Duplicates")
    private void prepareFormattedMonthlyTable() {
        //Clears the already existing data in the table

        this.monthlyTable = new TableView<>();

        //this.monthlyTable.setRowFactory();

        String fillerStyle = "-fx-fill: blue;";

        //Create all columns necessary
        String evenStyle = "evenRow";
        String oddStyle = "oddRow";

        TableColumn<DailyFormattedDataModel, YearWeek> weekNumbCol = new TableColumn<>("Week number");
        weekNumbCol.setCellValueFactory(new PropertyValueFactory<>("weekNumber"));
        weekNumbCol.setSortable(false);
        weekNumbCol.setCellFactory(col -> new TableCell<DailyFormattedDataModel, YearWeek>() {
            @Override
            protected void updateItem(YearWeek item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setText(null);
                    setGraphic(null);
                }
                else {
                    setText(String.valueOf(item.getWeek()));

                }
            }
        });


        TableColumn<DailyFormattedDataModel, Integer> weekdayCol = new TableColumn<>("Weekday");
        weekdayCol.setCellValueFactory(new PropertyValueFactory<>("weekday"));
        weekdayCol.setSortable(false);

        TableColumn<DailyFormattedDataModel, Integer> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setSortable(false);

        TableColumn<DailyFormattedDataModel, Double> workedHoursCol = new TableColumn<>("Hours worked");
        workedHoursCol.setCellValueFactory(new PropertyValueFactory<>("workedHours"));
        workedHoursCol.setSortable(false);
        workedHoursCol.setCellFactory(col ->

                                              setDecimalFormatter(df));
        workedHoursCol.getStyleClass().

                add("right");


        TableColumn<DailyFormattedDataModel, Double> supposedHoursCol = new TableColumn<>("Ordinary work hours");
        supposedHoursCol.setCellValueFactory(new PropertyValueFactory<>("supposedHours"));
        supposedHoursCol.setSortable(false);
        supposedHoursCol.setCellFactory(col ->

                                                setDecimalFormatter(df));
        supposedHoursCol.getStyleClass().

                add("right");


        TableColumn<DailyFormattedDataModel, Double> extraTimeCol = new TableColumn<>("+/- Hours");
        extraTimeCol.setCellValueFactory(new PropertyValueFactory<>("extraTime"));
        extraTimeCol.setSortable(false);
        extraTimeCol.setCellFactory(col ->

                                            setDecimalFormatter(df));
        extraTimeCol.getStyleClass().

                add("right");

        TableColumn<DailyFormattedDataModel, Double> accumulatedHoursCol = new TableColumn<>("Accumulated");
        accumulatedHoursCol.setCellValueFactory(new PropertyValueFactory<>("accumulatedHours"));
        accumulatedHoursCol.setSortable(false);
        accumulatedHoursCol.setCellFactory(col ->

                                                   setDecimalFormatter(df));
        accumulatedHoursCol.getStyleClass().

                add("right");

        TableColumn<DailyFormattedDataModel, Double> noteCol = new TableColumn<>("Notes");
        noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
        noteCol.setSortable(false);

        extraTimeCol.setCellFactory(col -> new TableCell<DailyFormattedDataModel, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setText(null);
                    setGraphic(null);
                }
                else {
                    setText(df.format(item));
                    setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, Font.getDefault().getSize()));
                    if(item < 0.0) {
                        setTextFill(Color.RED); // or use setStyle(String)
                    }
                    else if(item > 0.0) {
                        setTextFill(Color.GREEN); // or use setStyle(String)
                    }
                    else {
                        setTextFill(Color.BLACK);
                    }
                }
            }
        });
        workedHoursCol.setPrefWidth(120);
        supposedHoursCol.setPrefWidth(140);
        extraTimeCol.setPrefWidth(90);
        weekNumbCol.setPrefWidth(90);
        //Adds the columns to the table and updates it
        monthlyTable.getColumns().
                addAll(weekNumbCol, weekdayCol, dateCol, supposedHoursCol, workedHoursCol, extraTimeCol,
                       accumulatedHoursCol, noteCol);
        monthlyTable.setEditable(false);
    }

    /**
     * Builds the viewable table of all the raw from the Toggl user's data
     */
    private void prepareRawDataTable() {
        //Clears the already existing data in the table
        clearTable(rawData);

        //Create all columns necessary

        TableColumn<RawTimeDataModel, String> projectCol = new TableColumn<>("Project");
        projectCol.setCellValueFactory(new PropertyValueFactory<>("project"));

        TableColumn<RawTimeDataModel, String> clientCol = new TableColumn<>("Client");
        clientCol.setCellValueFactory(new PropertyValueFactory<>("client"));

        TableColumn<RawTimeDataModel, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));


        TableColumn<RawTimeDataModel, String> startDateCol = new TableColumn<>("Start Date");
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<RawTimeDataModel, String> startTimeCol = new TableColumn<>("Start Time");
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        startTimeCol.getStyleClass().add("right");

        TableColumn<RawTimeDataModel, String> endDateCol = new TableColumn<>("End Date");
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        TableColumn<RawTimeDataModel, String> endTimeCol = new TableColumn<>("End Time");
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        endTimeCol.getStyleClass().add("right");

        TableColumn<RawTimeDataModel, String> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationCol.getStyleClass().add("right");

        projectCol.setPrefWidth(120);
        descCol.setPrefWidth(120);
        startDateCol.setPrefWidth(110);
        startTimeCol.setPrefWidth(60);
        endDateCol.setPrefWidth(110);
        endTimeCol.setPrefWidth(60);
        //Adds the columns to the table and updates it
        rawData.setEditable(false);
        rawData.getColumns()
               .addAll(projectCol, clientCol, descCol, startDateCol, startTimeCol, endDateCol, endTimeCol, durationCol);
    }

    private <T> TableCell<T, Double> setDecimalFormatter(DecimalFormat format) {
        return new TableCell<T, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setText(null);
                    setGraphic(null);
                }
                else {
                    setText(format.format(item));
                }
            }
        };
    }
    //endregion

    /**
     * Creates an observable list containing WeeklyTimeDataModel objects
     * @return an ObservableList containing WeeklyTimeDatModel objects
     */
    private ObservableList<DailyFormattedDataModel> createObservableWeeklyData() {

        // find the yearweek to fetch data from
        YearWeek yearWeek = YearWeek.of(Integer.parseInt(yearSpinner.getEditor().getText()),
                                        Integer.parseInt(weekSpinner.getEditor().getText()));

        return FXCollections.observableArrayList(formattedTimeDataLogic.getWeeklyData(yearWeek));
    }

    /**
     * Creates an observable list containing MonthlyTimeDataModel objects
     * @return an ObservableList containing MonthlyTimeDatModel objects
     */
    private ObservableList<DailyFormattedDataModel> createObservableMonthlyData() {
        // find the yearmonth to fetch data from
        YearMonth yearMonth = YearMonth
                .of(Integer.parseInt(yearSpinner.getEditor().getText()), monthSpinner.getValue().get());

        List<DailyFormattedDataModel> data = formattedTimeDataLogic.getMonthlyData(yearMonth);
        return FXCollections.observableArrayList(data);
    }

    private void updateWeeklySpinner(boolean show) {
        weekSpinner.setVisible(show);
        weekSpinner.setDisable(! show);
    }

    private void updateMonthlySpinner(boolean show) {
        monthSpinner.setVisible(show);
        monthSpinner.setDisable(! show);
    }
    //endregion

    //region Filter options methods

    /**
     * fill all filter buttons with the necessary buttons and data
     */
    private void setFilterOptions() {
        Session session = Session.getInstance();
        Platform.runLater(() -> {
            clearCheckMenuObjects(projectFilterBtn);
            clearCheckMenuObjects(workspaceFilterBtn);
            clearCheckMenuObjects(clientFilterBtn);


            projectFilterBtn.getItems().add(new CheckMenuObject<>(new Project(), "No Project"));
            HashMap<Long, Project> projects = session.getProjects();
            for(Map.Entry<Long, Project> project: projects.entrySet()) {
                projectFilterBtn.getItems().add(new CheckMenuObject(project.getValue(), project.getValue().getName()));
            }

            workspaceFilterBtn.getItems().add(new CheckMenuObject<>(new Workspace(), "No Workspace"));
            HashMap<Long, Workspace> workspaces = session.getWorkspaces();
            for(Map.Entry<Long, Workspace> workspace: workspaces.entrySet()) {
                workspaceFilterBtn.getItems()
                                  .add(new CheckMenuObject(workspace.getValue(), workspace.getValue().getName()));
            }

            clientFilterBtn.getItems().add(new CheckMenuObject<>(new Client(), "No Client"));
            HashMap<Long, Client> clients = session.getClients();
            for(Map.Entry<Long, Client> client: clients.entrySet()) {
                clientFilterBtn.getItems().add(new CheckMenuObject(client.getValue(), client.getValue().getName()));
            }
        });
    }

    private void clearCheckMenuObjects(MenuButton button) {
        button.getItems().removeIf(item -> item instanceof CheckMenuObject);
    }


    /**
     * Initialize a button with all items common for filter buttons,
     * including "select all" and "deselect buttons"
     * @param button the MenuButton to initialize
     */
    private void initializeFilterButton(MenuButton button) {
        MenuItem selectAll = new CustomMenuItem(new Label("Select all"));
        MenuItem deselectAll = new CustomMenuItem(new Label("Deselect all"));
        ((CustomMenuItem) selectAll).setHideOnClick(false);
        ((CustomMenuItem) deselectAll).setHideOnClick(false);
        selectAll.setOnAction(event -> toggleAllCheckboxes(button, true));
        deselectAll.setOnAction(event -> toggleAllCheckboxes(button, false));
        selectAll.setStyle("-fx-font-weight: bold;");
        deselectAll.setStyle("-fx-font-weight: bold;");
        button.getItems().add(selectAll);
        button.getItems().add(deselectAll);
        button.getItems().add(new SeparatorMenuItem());
    }

    private void toggleAllCheckboxes(MenuButton button, boolean value) {
        List<MenuItem> items = button.getItems();

        for(MenuItem item: items) {
            if(item instanceof CheckMenuObject) {
                Node content = ((CheckMenuObject) item).getContent();
                if(content instanceof CheckBox) {
                    ((CheckBox) content).setSelected(value);
                }
            }
        }
    }

    //add generic object to filter options set
    private <T> void addFilterOption(T t) {
        filterOptions.add(t);
    }

    //remove generic object from filter options set
    private <T> void removeFilterOption(T t) {
        filterOptions.remove(t);
    }


    /**
     * Inner class to create a menu-item with a check box and an object attached to it.
     */
    class CheckMenuObject<Content> extends CustomMenuItem {

        public CheckMenuObject(Object object, String name) {
            super();
            setHideOnClick(false);
            CheckBox cb = new CheckBox(name);
            setGraphic(cb);
            setContent(cb);
            cb.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue) {
                    removeFilterOption(object);
                }
                else {
                    addFilterOption(object);
                }
            });
            cb.setSelected(true);
        }
    }
    //endregion

    // |##################################################|
    // |                  OTHER METHODS                   |
    // |##################################################|

    private void applyFilters() {
        if (rawTimeDataLogic.getMasterTimeEntries() != null) {
            setRawDataTableData();
            updateFormattedTableData();
            updateProjectTableData();
        }
    }

    /**
     * Clears the selected TableView
     * @param table the TableView to clear
     */
    private <T> void clearTable(TableView<T> table) {
        table.getColumns().clear();
        table.getItems().clear();
    }

    /**
     * Add content to a parent container
     * @param parent A parent container where to add the content to.
     * @param content The content to add
     */
    private void showContentInParentContainer(Pane parent, Region content) {
        ObservableList<Node> children = parent.getChildren();
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
        if(loadedData.containsAll(
                EnumSet.of(Data.TIME_ENTRIES, Data.PROJECTS, Data.TASKS, Data.WORKSPACES, Data.WORKHOURS,
                           Data.CLIENT))) {
            loadedData.clear(); // clear the set, readying it for next
            setRawDataTableData();
            updateFormattedTableData();
            updateProjectTableData();
            setFilterOptions();
        }
    }
}
