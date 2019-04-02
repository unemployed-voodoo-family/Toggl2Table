package UnemployedVoodooFamily.Data;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Logic.PropertiesLogic;
import UnemployedVoodooFamily.Logic.TestUtils;
import ch.simas.jtoggl.TimeEntry;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Spinner;
import org.apache.poi.ss.formula.functions.T;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import static org.junit.Assert.*;

public class DailyFormattedDataModelBuilderTest {
    private List<TimeEntry> timeEntries;
    Double STANDARD_WORK_HOURS = 7.5;
    Locale locale = Locale.getDefault();
    TextStyle style = TextStyle.FULL;

    @Test
    public void build() {

        Double WORKED_HOURS = 14.5088889;
        Double OVERTIME =WORKED_HOURS - STANDARD_WORK_HOURS;


        LocalDate localDate = LocalDate.of(2019, 01, 02);
        DailyFormattedDataModelBuilder builder = new DailyFormattedDataModelBuilder(localDate);
        for(int i = 0; i < 4; i++) {
            builder.addTimeEntry(timeEntries.get(i));
        }
        DailyFormattedDataModel dataModel = builder.build();
        assertEquals(localDate, dataModel.getDay());
        assertEquals(DayOfWeek.WEDNESDAY.getDisplayName(style, locale), dataModel.getWeekDay());
        assertEquals(STANDARD_WORK_HOURS, dataModel.getSupposedHours());
        assertEquals(WORKED_HOURS, dataModel.getWorkedHours(), 0.01);
        assertEquals(OVERTIME, dataModel.getOvertime(), 0.01);
    }

    @Test
    public void buildNull() {

        LocalDate localDate = LocalDate.of(2019, 01, 02);
        DailyFormattedDataModelBuilder builder = new DailyFormattedDataModelBuilder(localDate);

        DailyFormattedDataModel dataModel = builder.addTimeEntry(new TimeEntry()).build();
        assertEquals(localDate, dataModel.getDay());
        assertEquals(DayOfWeek.WEDNESDAY.getDisplayName(style, locale), dataModel.getWeekDay());
        assertEquals(STANDARD_WORK_HOURS, dataModel.getSupposedHours());
        assertEquals(0, dataModel.getWorkedHours(), 0.01);
        assertEquals(-7.5, dataModel.getOvertime(), 0.01);
    }

    @Test
    public void getWeekDay() {
    }

    @Before
    public void setUp() throws Exception {
        timeEntries = TestUtils.getTestTimeEntries();

    }

    @After
    public void tearDown() throws Exception {
        timeEntries = null;
    }
}