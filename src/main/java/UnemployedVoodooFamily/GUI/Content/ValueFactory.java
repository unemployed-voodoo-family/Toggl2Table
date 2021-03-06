package UnemployedVoodooFamily.GUI.Content;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static UnemployedVoodooFamily.Logic.ProjectSummaryLogic.CURRENT_YEAR;
import static UnemployedVoodooFamily.Logic.ProjectSummaryLogic.FIRST_TOGGL_YEAR;

/**
 * A helper class to generate lists and factories for spinners and dropdowns for years, weeks, months
 */
public class ValueFactory {
    private static final ObservableList<SimpleObjectProperty<Month>> MONTH_LIST = FXCollections.observableArrayList();

    /**
     * Get a spinner factory containing integers for years, from the year when Toggl was launched until current year
     * @return a year value factory
     */
    public static IntegerSpinnerValueFactory getYearValueFactory() {
        return new SpinnerValueFactory.IntegerSpinnerValueFactory(FIRST_TOGGL_YEAR, CURRENT_YEAR, CURRENT_YEAR);
    }

    /**
     * Get a spinner factory containing integers from 1 to 52 (week numbers
     * @param selectedWeek Which week should be marked as selected
     * @return Year spinner factory
     */
    public static IntegerSpinnerValueFactory getWeekValueFactory(int selectedWeek) {
        return new javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory(1, 52, selectedWeek);
    }

    /**
     * Get years as a list, starting from the first year when Toggl was launched until the current year
     * @return a list with ordered years
     */
    public static List<Integer> getYearsAsList() {
        return IntStream.rangeClosed(FIRST_TOGGL_YEAR, CURRENT_YEAR).boxed().collect(Collectors.toList());
    }

    /**
     * Get week numbers (1..52) as a list
     * @return a list of ordered week numbers
     */
    public static List<Integer> getWeeksAsList() {
        return IntStream.rangeClosed(1, 52).boxed().collect(Collectors.toList());
    }

    public static SpinnerValueFactory<SimpleObjectProperty<Month>> getMonthValueFactory() {
        return new javafx.scene.control.SpinnerValueFactory.ListSpinnerValueFactory<>(getMonthList());
    }

    /**
     * Lazy init month list and return it
     * @return A list with ordered months, Jan to Dec
     */
    public static ObservableList<SimpleObjectProperty<Month>> getMonthList() {
        if (MONTH_LIST.isEmpty()) {
            System.out.println("Initializing month list");
            for(Month m: Month.values()) {
                MONTH_LIST.add(new SimpleObjectProperty<>(m));
            }
        }
        return MONTH_LIST;
    }

    /**
     * Get observable property with current month as the value
     * @return observable property with current month as the value
     */
    public static SimpleObjectProperty<Month> getCurrentMonth() {
        return getMonthList().get(Month.from(LocalDate.now()).getValue() - 1);
    }
}
