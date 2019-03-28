package UnemployedVoodooFamily.Data;

import UnemployedVoodooFamily.Logic.TestUtils;
import ch.simas.jtoggl.TimeEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class WeeklyFormattedDataModelBuilderTest {
    List<TimeEntry> timeEntries;

    @Before
    public void setUp() throws Exception {
        timeEntries = TestUtils.getTestTimeEntries();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addDailyData() {

    }

    @Test
    public void build() {
        Double WORKED_HOURS = 14.5088889;
        Double STANDARD_WORK_HOURS = 7.5;
        Double OVERTIME = WORKED_HOURS - STANDARD_WORK_HOURS;


        LocalDate localDate = LocalDate.of(2019, 01, 02);
        WeeklyFormattedDataModelBuilder builder = new WeeklyFormattedDataModelBuilder(localDate);
        builder.addDailyData(
                new DailyFormattedDataModel(WORKED_HOURS, 7.5, WORKED_HOURS - STANDARD_WORK_HOURS, LocalDate.now()));
        WeeklyFormattedDataModel dataModel = builder.build();
        assertEquals((Integer) 1, dataModel.getWeekNumber());
        //assertEquals(STANDARD_WORK_HOURS, dataModel.getSupposedHours());
        assertEquals(WORKED_HOURS, dataModel.getWorkedHours(), 0.01);
        assertEquals(OVERTIME, dataModel.getOvertime(), 0.01);
    }
}