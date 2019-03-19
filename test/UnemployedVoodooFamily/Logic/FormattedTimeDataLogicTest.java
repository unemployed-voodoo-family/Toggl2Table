package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.DailyFormattedDataModel;
import ch.simas.jtoggl.TimeEntry;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;

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
        formattedTimeDataLogic.buildObservableWeeklyTimeData(timeEntries);
        ObservableList<DailyFormattedDataModel> data = formattedTimeDataLogic.getWeeklyMasterData();
        assertNotNull(data);
        assertEquals(365, data.size());
    }

    @Test
    public void exportToExcelDocument() {
    }

    @Test
    public void buildObservableWeeklyTimeData() {
    }
}