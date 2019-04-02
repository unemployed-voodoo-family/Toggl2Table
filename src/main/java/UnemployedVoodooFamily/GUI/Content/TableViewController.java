package UnemployedVoodooFamily.GUI.Content;

import UnemployedVoodooFamily.Data.DailyFormattedDataModel;
import UnemployedVoodooFamily.Data.Enums.Data;
import UnemployedVoodooFamily.Data.RawTimeDataModel;
import UnemployedVoodooFamily.Data.WeeklyFormattedDataModel;
import UnemployedVoodooFamily.Logic.FormattedTimeDataLogic;
import UnemployedVoodooFamily.Logic.Listeners.DataLoadListener;
import UnemployedVoodooFamily.Logic.RawTimeDataLogic;
import UnemployedVoodooFamily.Logic.Session;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.Workspace;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;

public class TableViewController<Content extends Pane> implements DataLoadListener {

    @FXML
    private Tab rawDataTab;

    @FXML
    private Tab formattedDataTab;

    @FXML
    private TableView rawData;

    @FXML
    private Label excelFeedbackLabel;

    @FXML
    private ToggleButton weeklyToggleBtn;

    @FXML
    private ToggleButton monthlyToggleBtn;

    @FXML
    private Button exportBtn;

    @FXML
    private GridPane tableRoot;

    @FXML
    private DatePicker rawStartDate;
    @FXML
    private DatePicker rawEndDate;
    @FXML
    private AnchorPane summaryRoot;

    @FXML
    private VBox filterBox;
    @FXML
    private MenuButton projectFilterBtn;
    @FXML
    private MenuButton workspaceFilterBtn;
    @FXML
    private Button applyFilterBtn;
    @FXML
    private TabPane tableTabPane;
    @FXML
    private HBox root;

    @FXML
    private Spinner yearSpinner;
    @FXML
    private Spinner weekSpinner;
    @FXML
    private Spinner monthSpinner;

    @FXML
    private ComboBox yearlyDropdown;
    @FXML
    private ComboBox weeklyDropdown;
    @FXML
    private ComboBox monthlyDropdown;


    @FXML
    private Content weeklySummary;
    @FXML
    private Content monthlySummary;

    @FXML
    private TableView monthlyTable;
    @FXML
    private TableView weeklyTable;

    @FXML
    private Label yearSpinnerLabel;
    @FXML
    private Label timePeriodSpinnerLabel;

    private final ToggleGroup timeSpanToggleGroup = new ToggleGroup();

    private RawTimeDataLogic rawTimeDataLogic = new RawTimeDataLogic();
    private FormattedTimeDataLogic formattedTimeDataLogic = new FormattedTimeDataLogic();
    private EnumSet<Data> loadedData = EnumSet.noneOf(Data.class);

    private Set<Object> filterOptions = new HashSet<>();


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
        try {
            this.weeklySummary = new WeeklySummaryViewController().loadFXML();
            this.monthlySummary = new MonthlySummaryViewController().loadFXML();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        buildFormattedWeeklyTable();
        buildFormattedMonthlyTable();
        buildRawDataTable();

        setupFormattedTableUIElements();
        setupRawTableUIElements();

        initializeFilterButton(projectFilterBtn);
        initializeFilterButton(workspaceFilterBtn);
    }

    private void setupFormattedTableUIElements() {
        weeklyToggleBtn.setToggleGroup(timeSpanToggleGroup);
        monthlyToggleBtn.setToggleGroup(timeSpanToggleGroup);
        weeklyToggleBtn.setSelected(true);

        yearSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        weekSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        monthSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);

        int firstTogglYear = 2006;
        //Sets default values for the spinners
        //Change WEEKLY in case default formatted view changes
        yearSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(firstTogglYear, LocalDate.now().getYear(),
                                                                   LocalDate.now().getYear()));
        weekSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 52, formattedTimeDataLogic.getSelectedWeek()));

        //Creates a list with all months for the monthly spinner to use
        ObservableList<String> monthsList = FXCollections.observableArrayList();
        for(Month m: Month.values()) {
            monthsList.add(StringUtils.capitalize(m.toString().toLowerCase()));
        }
        monthSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<String>(monthsList));
        monthSpinner.getValueFactory()
                    .setValue(StringUtils.capitalize(LocalDate.now().getMonth().toString().toLowerCase()));
        //Hide the Monthly spinner by default
        updateMonthlySpinner(false);

        timePeriodSpinnerLabel.setText("Week");

        //Dropdown menus for the spinners
        yearlyDropdown.setVisible(false);
        yearlyDropdown.getItems().addAll(IntStream.rangeClosed(firstTogglYear, LocalDate.now().getYear()).boxed()
                                                  .collect(Collectors.toList()));
        weeklyDropdown.setVisible(false);
        weeklyDropdown.getItems().addAll(IntStream.rangeClosed(1, 52).boxed().collect(Collectors.toList()));
        monthlyDropdown.setVisible(false);
        monthlyDropdown.getItems().addAll(monthsList);
    }

    private void setupRawTableUIElements() {
        rawStartDate.setValue(LocalDate.now().minusWeeks(1));
        rawStartDate.setDisable(true);
        rawEndDate.setValue(LocalDate.now());
        rawEndDate.setDisable(true);
    }

    @SuppressWarnings("Duplicates")
    /**
     * Sets input actions on UI elements
     */ private void setKeyAndClickListeners() {

        applyFilterBtn.setOnAction(event -> applyFilters());

        exportBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                boolean success = formattedTimeDataLogic.exportToExcelDocument();
                if(success) {
                    excelFeedbackLabel.setText("Excel document was successfully created");
                    excelFeedbackLabel.getStyleClass().remove("error");
                    excelFeedbackLabel.getStyleClass().add("success");
                }
                else {
                    excelFeedbackLabel.setText("Excel document was NOT created");
                    excelFeedbackLabel.getStyleClass().remove("success");
                    excelFeedbackLabel.getStyleClass().add("error");
                }

                Thread t1 = new Thread(() -> {
                    double opacity = 1.00;
                    exportBtn.setDisable(true);
                    excelFeedbackLabel.setOpacity(opacity);
                    while(opacity >= 0.00) {
                        excelFeedbackLabel.setOpacity(opacity);
                        try {
                            sleep(30);
                        }
                        catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                        opacity = (opacity - 0.02);
                    }
                    excelFeedbackLabel.setOpacity(0.00);
                    exportBtn.setDisable(false);
                });
                t1.start();
            }
        });

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
            weeklyToggleBtn.setSelected(true);
            switchView(tableRoot, weeklyTable);
            switchView(summaryRoot, weeklySummary);
            updateWeeklySpinner(true);
            updateMonthlySpinner(false);
            timePeriodSpinnerLabel.setText("Week");
        });
        monthlyToggleBtn.setOnAction((ActionEvent e) -> {
            monthlyToggleBtn.setSelected(true);
            switchView(tableRoot, monthlyTable);
            switchView(summaryRoot, monthlySummary);
            updateWeeklySpinner(false);
            updateMonthlySpinner(true);
            timePeriodSpinnerLabel.setText("Month");
            updateMonthlyTable();
        });

        //Year Spinner + Dropdown
        yearSpinner.getEditor().setOnMouseClicked((MouseEvent e) -> {
            yearSpinner.getEditor().setVisible(false);
            yearlyDropdown.setVisible(true);
            yearlyDropdown.show();
            System.out.println("Year spinner clicked");
        });
        yearlyDropdown.setOnHiding((Event e) -> {
            yearSpinner.getEditor().setVisible(true);
            yearlyDropdown.setVisible(false);
            if(yearlyDropdown.getValue() != null) {
                yearSpinner.getValueFactory().setValue(yearlyDropdown.getValue());
            }
        });
        yearSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                formattedTimeDataLogic.setSelectedYear(newValue);
                updateMonthlyTable();
            }
        });

        //Week Spinner + Dropdown
        weekSpinner.getEditor().setOnMouseClicked((MouseEvent e) -> {
            weekSpinner.getEditor().setVisible(false);
            weeklyDropdown.setVisible(true);
            weeklyDropdown.show();
            System.out.println("Week spinner clicked");
        });
        weeklyDropdown.setOnHiding((Event e) -> {
            weekSpinner.getEditor().setVisible(true);
            weeklyDropdown.setVisible(false);
            if(weeklyDropdown.getValue() != null) {
                weekSpinner.getValueFactory().setValue(weeklyDropdown.getValue());
            }
        });
        weekSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                formattedTimeDataLogic.setSelectedWeek(newValue);
            }
        });

        //Month Spinner + Dropdown
        monthSpinner.getEditor().setOnMouseClicked((MouseEvent e) -> {
            monthSpinner.getEditor().setVisible(false);
            monthlyDropdown.setVisible(true);
            monthlyDropdown.show();
            System.out.println("Month spinner clicked");
        });
        monthlyDropdown.setOnHiding((Event e) -> {
            monthSpinner.getEditor().setVisible(true);
            monthlyDropdown.setVisible(false);
            if(monthlyDropdown.getValue() != null) {
                monthSpinner.getValueFactory().setValue(monthlyDropdown.getValue());
            }
        });
        monthSpinner.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                formattedTimeDataLogic.setSelectedMonth(Month.valueOf(newValue.toUpperCase()));
                updateMonthlyTable();
            }
        });
    }

    private void applyFilters() {
        setRawDataTableData();
        setFormattedTableData();
        setRawDataTableData();
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

        TableColumn<RawTimeDataModel, String> clientCol = new TableColumn<>("Client");
        clientCol.setCellValueFactory(new PropertyValueFactory<>("client"));

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

        TableColumn<RawTimeDataModel, String> durationCol = new TableColumn<>("Duration (HH:mm:ss)");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));

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

    private void setRawDataTableData() {
        rawData.getItems().setAll(getObservableRawData());
        Platform.runLater(() -> {
            rawEndDate.setValue(rawTimeDataLogic.getFilteredDataEndDate());
            rawStartDate.setValue(rawTimeDataLogic.getFilteredDataStartDate());
            rawEndDate.setDisable(false);
            rawStartDate.setDisable(false);
        });
    }

    private void setFormattedTableData() {
        updateWeeklyTable();
        updateMonthlyTable();
    }

    private void updateMonthlyTable() {
        monthlyTable.getItems().setAll(getObservableMonthlyData());
    }

    private void updateWeeklyTable() {
        weeklyTable.getItems().setAll(getObservableWeeklyData());
    }

    /**
     * Creates an observable list containing RawTimeDataModel objects
     * @return an ObservableList containing RawTimeDatModel objects
     */
    private ObservableList<RawTimeDataModel> getObservableRawData() {
        Session session = Session.getInstance();
        return FXCollections.observableArrayList(
                rawTimeDataLogic.buildRawMasterData(session.getTimeEntries(), session.getProjects(), session.getWorkspaces(),
                                                    session.getClients(), filterOptions));
    }

    // |##################################################|
    // |           FORMATTED DATA TABLE METHODS           |
    // |##################################################|

    /**
     * Builds a formatted table with a weekly overview
     */
    @SuppressWarnings("Duplicates")
    private void buildFormattedWeeklyTable() {
        DecimalFormat df = new DecimalFormat("#0.00 ");

        this.weeklyTable = new TableView();
        //Create all columns necessary
        TableColumn<DailyFormattedDataModel, String> weekDayCol = new TableColumn<>("Week Day");
        weekDayCol.setCellValueFactory(new PropertyValueFactory<>("weekDay"));
        weekDayCol.setSortable(false);

        TableColumn<DailyFormattedDataModel, Double> workedHoursCol = new TableColumn<>("Worked Hours");
        workedHoursCol.setCellValueFactory(new PropertyValueFactory<>("workedHours"));
        workedHoursCol.setSortable(false);
        workedHoursCol.setCellFactory(col -> setDoubleFormatter(df));

        TableColumn<DailyFormattedDataModel, Double> supposedHoursCol = new TableColumn<>("Supposed Hours");
        supposedHoursCol.setCellValueFactory(new PropertyValueFactory<>("supposedHours"));
        supposedHoursCol.setSortable(false);
        supposedHoursCol.setCellFactory(col -> setDoubleFormatter(df));

        TableColumn<DailyFormattedDataModel, Double> overtimeCol = new TableColumn<>("Overtime");
        overtimeCol.setCellValueFactory(new PropertyValueFactory<>("overtime"));
        overtimeCol.setSortable(false);
        overtimeCol.setCellFactory(col -> new TableCell<DailyFormattedDataModel, Double>() {
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
        supposedHoursCol.setPrefWidth(120);
        overtimeCol.setPrefWidth(120);
        weekDayCol.setPrefWidth(90);
        System.out.println("done");
        //Adds the columns to the table and updates it
        this.weeklyTable.getColumns().addAll(weekDayCol, workedHoursCol, supposedHoursCol, overtimeCol);
        this.weeklyTable.setEditable(false);

        //must be called, or else the table won't appear
        switchView(tableRoot, weeklyTable);
        switchView(summaryRoot, weeklySummary);
    }

    /**
     * Builds a formatted table with monthly overview
     */
    @SuppressWarnings("Duplicates")
    private void buildFormattedMonthlyTable() {
        //Clears the already existing data in the table

        this.monthlyTable = new TableView();
        DecimalFormat df = new DecimalFormat("#0.00 ");

        //Create all columns necessary

        TableColumn<WeeklyFormattedDataModel, Integer> weekNumbCol = new TableColumn<>("Week Number");
        weekNumbCol.setCellValueFactory(new PropertyValueFactory<>("weekNumber"));
        weekNumbCol.setSortable(false);

        TableColumn<WeeklyFormattedDataModel, Double> workedHoursCol = new TableColumn<>("Worked Hours");
        workedHoursCol.setCellValueFactory(new PropertyValueFactory<>("workedHours"));
        workedHoursCol.setSortable(false);
        workedHoursCol.setCellFactory(col -> setDoubleFormatter(df));

        TableColumn<WeeklyFormattedDataModel, Double> supposedHoursCol = new TableColumn<>("Supposed Hours");
        supposedHoursCol.setCellValueFactory(new PropertyValueFactory<>("supposedHours"));
        supposedHoursCol.setSortable(false);
        supposedHoursCol.setCellFactory(col -> setDoubleFormatter(df));

        TableColumn<WeeklyFormattedDataModel, Double> overtimeCol = new TableColumn<>("Overtime");
        overtimeCol.setCellValueFactory(new PropertyValueFactory<>("overtime"));
        overtimeCol.setSortable(false);

        overtimeCol.setCellFactory(col -> new TableCell<WeeklyFormattedDataModel, Double>() {
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
        supposedHoursCol.setPrefWidth(120);
        overtimeCol.setPrefWidth(120);
        weekNumbCol.setPrefWidth(90);
        //Adds the columns to the table and updates it
        monthlyTable.getColumns().addAll(weekNumbCol, workedHoursCol, supposedHoursCol, overtimeCol);
        monthlyTable.setEditable(false);
    }

    private <T> TableCell<T, Double> setDoubleFormatter(DecimalFormat df) {
        return new TableCell<T, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setText(null);
                    setGraphic(null);
                }
                else {
                    setText(df.format(item));
                }
            }
        };
    }

    /**
     * Creates an observable list containing WeeklyTimeDataModel objects
     * @return an ObservableList containing WeeklyTimeDatModel objects
     */
    private ObservableList<DailyFormattedDataModel> getObservableWeeklyData() {
        if(rawTimeDataLogic.getFilteredTimeEntries() != null) {
            return formattedTimeDataLogic.buildObservableWeeklyTimeData(rawTimeDataLogic.getFilteredTimeEntries());
        }
        return formattedTimeDataLogic.buildObservableWeeklyTimeData(rawTimeDataLogic.getFilteredTimeEntries());
    }

    /**
     * Creates an observable list containing MonthlyTimeDataModel objects
     * @return an ObservableList containing MonthlyTimeDatModel objects
     */
    private ObservableList<WeeklyFormattedDataModel> getObservableMonthlyData() {
        return formattedTimeDataLogic.buildMonthlySortedData();
    }

    private void updateWeeklySpinner(boolean show) {
        weekSpinner.setVisible(show);
        weekSpinner.setDisable(! show);
    }

    private void updateMonthlySpinner(boolean show) {
        monthSpinner.setVisible(show);
        monthSpinner.setDisable(! show);
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

    private <T extends Pane, S extends Region> void switchView(T root, S content) {
        ObservableList<Node> children = root.getChildren();
        if(children.isEmpty()) {
            children.addAll(content);
        }
        else if(! children.contains(content)) {
            children.clear();
            children.addAll(content);
        }
        content.prefWidthProperty().bind(root.widthProperty());
        content.prefHeightProperty().bind(root.heightProperty());
    }

    /**
     * fill all filter buttons with the necessary buttons and data
     */
    private void setFilterOptions() {
        Session session = Session.getInstance();
        clearCheckMenuObjects(projectFilterBtn);
        clearCheckMenuObjects(workspaceFilterBtn);

        HashMap<Long, Project> projects = (HashMap<Long, Project>) session.getProjects();
        for(Map.Entry<Long, Project> project: projects.entrySet()) {
            projectFilterBtn.getItems().add(new CheckMenuObject(project, project.getValue().getName()));
        }
        HashMap<Long, Workspace> workspaces = (HashMap) session.getWorkspaces();
        for(Map.Entry<Long, Workspace> workspace: workspaces.entrySet()) {
            workspaceFilterBtn.getItems().add(new CheckMenuObject(workspace, workspace.getValue().getName()));
        }
    }

    private void clearCheckMenuObjects(MenuButton button) {
        Iterator<MenuItem> it = button.getItems().iterator();
        while(it.hasNext()) {
            MenuItem item = it.next();
            if(item instanceof CheckMenuObject) {
                it.remove();
            }
        }
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
        private Object object;

        public CheckMenuObject(Object object, String name) {
            super();
            this.object = object;
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
            loadedData = EnumSet.noneOf(Data.class); //empty the set, readying it for next
            setFilterOptions();
            setRawDataTableData();
            setFormattedTableData();
        }
    }
}
