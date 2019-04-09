    package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.DailyFormattedDataModel;
import ch.simas.jtoggl.TimeEntry;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;

import java.time.YearMonth;
import java.util.List;

import static org.junit.Assert.*;

public class FormattedTimeDataLogicTest {

    private List<TimeEntry> timeEntries = TestUtils.getTestTimeEntries();
    private FormattedTimeDataLogic formattedTimeDataLogic = new FormattedTimeDataLogic();

    @Before
    public void setUp() throws Exception {
        //formattedTimeDataLogic = new FormattedTimeDataLogic();
    }

    @Test
    public void buildObservableMonthlyTimeData() {
        YearMonth ym = YearMonth.of(2019, 1);
        int daysInMonth = ym.lengthOfMonth();

        formattedTimeDataLogic.buildMasterData(timeEntries, 2019);
        List<DailyFormattedDataModel> data = formattedTimeDataLogic.getMonthlyData(ym);
        assertNotNull(data);
        assertEquals(daysInMonth, data.size());

    }

    @Test
    public void exportToExcelDocument() {
    }

    @Test
    public void buildObservableWeeklyTimeData() {
    }
}