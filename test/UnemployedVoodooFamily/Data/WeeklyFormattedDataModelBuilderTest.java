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
    public void build1() {
        Double WORKED_HOURS = 10.0;
        Double STANDARD_WORK_HOURS = 7.5;
        Double STANDARD_WORK_HOURS_WEEK = 7.5*5;
        Double OVERTIME = WORKED_HOURS - STANDARD_WORK_HOURS*5;


        LocalDate localDate = LocalDate.of(2019, 01, 02);
        WeeklyFormattedDataModelBuilder builder = new WeeklyFormattedDataModelBuilder(localDate);
        builder.addDailyData(
                new DailyFormattedDataModel(WORKED_HOURS, 7.5, localDate));

        WeeklyFormattedDataModel dataModel = builder.build();
        assertEquals((Integer) 1, dataModel.getWeekNumber());
        assertEquals(STANDARD_WORK_HOURS_WEEK, dataModel.getSupposedHours());
        assertEquals(WORKED_HOURS, dataModel.getWorkedHours(), 0.01);
        assertEquals(OVERTIME, dataModel.getOvertime(), 0.01);
    }

    @Test
    public void testWorkOnWeekend() {
        Double WORKED_HOURS = 10.0;
        Double STANDARD_WORK_HOURS = 7.5;
        Double STANDARD_WORK_HOURS_WEEK = 7.5 * 5;
        Double OVERTIME = WORKED_HOURS - STANDARD_WORK_HOURS_WEEK;


        LocalDate localDate = LocalDate.of(2019, 1, 7);
        WeeklyFormattedDataModelBuilder builder = new WeeklyFormattedDataModelBuilder(localDate);

        for(int i = 0; i < 7; i++) {
            builder.addDailyData(new DailyFormattedDataModel(10.0,10.0, localDate.plusDays(i)));
        }

        WeeklyFormattedDataModel dataModel = builder.build();
        assertEquals((Integer) 2, dataModel.getWeekNumber());
        //assertEquals(STANDARD_WORK_HOURS, dataModel.getSupposedHours());
        assertEquals(70, dataModel.getWorkedHours(), 0.01);
        assertEquals(20, dataModel.getOvertime(), 0.01);
    }

    @Test
    public void testStandardWorkWeek() {
        Double WORKED_HOURS = 10.0;
        Double STANDARD_WORK_HOURS = 7.5;
        Double STANDARD_WORK_HOURS_WEEK = 7.5 * 5;
        Double OVERTIME = WORKED_HOURS - STANDARD_WORK_HOURS_WEEK;


        LocalDate localDate = LocalDate.of(2019, 1, 7);
        WeeklyFormattedDataModelBuilder builder = new WeeklyFormattedDataModelBuilder(localDate);

        for(int i = 0; i < 5; i++) {
            builder.addDailyData(new DailyFormattedDataModel(10.0,10.0, localDate.plusDays(i)));
        }

        WeeklyFormattedDataModel dataModel = builder.build();
        assertEquals((Integer) 2, dataModel.getWeekNumber());
        //assertEquals(STANDARD_WORK_HOURS, dataModel.getSupposedHours());
        assertEquals(50, dataModel.getWorkedHours(), 0.01);
        assertEquals(0, dataModel.getOvertime(), 0.01);
    }

    @Test
    public void testWorkOverTime() {
        Double WORKED_HOURS = 10.0;
        Double STANDARD_WORK_HOURS = 7.5;
        Double STANDARD_WORK_HOURS_WEEK = 7.5 * 5;
        Double OVERTIME = WORKED_HOURS - STANDARD_WORK_HOURS_WEEK;


        LocalDate localDate = LocalDate.of(2019, 1, 7);
        WeeklyFormattedDataModelBuilder builder = new WeeklyFormattedDataModelBuilder(localDate);

        for(int i = 0; i < 5; i++) {
            builder.addDailyData(new DailyFormattedDataModel(15.0,7.5, localDate.plusDays(i)));
        }

        WeeklyFormattedDataModel dataModel = builder.build();
        assertEquals((Integer) 2, dataModel.getWeekNumber());
        //assertEquals(STANDARD_WORK_HOURS, dataModel.getSupposedHours());
        assertEquals(75, dataModel.getWorkedHours(), 0.01);
        assertEquals(37.5, dataModel.getOvertime(), 0.01);
    }
}
