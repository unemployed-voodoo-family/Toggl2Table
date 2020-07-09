package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.DailyFormattedDataModel;
import ch.simas.jtoggl.TimeEntry;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;
import org.threeten.extra.YearWeek;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class FormattedTimeDataLogicTest {

    private List<TimeEntry> timeEntries;
    private FormattedTimeDataLogic formattedTimeDataLogic;

    @Before
    public void setUp() throws Exception {
        //formattedTimeDataLogic = new FormattedTimeDataLogic();
        timeEntries = TestUtils.getTestTimeEntries();
        formattedTimeDataLogic = new FormattedTimeDataLogic();
    }

    @Test
    public void testBuildEveryMonth() {
        YearMonth jan = YearMonth.of(2019, 1);
        YearMonth feb = YearMonth.of(2019, 2);
        YearMonth mar = YearMonth.of(2019, 3);
        YearMonth apr = YearMonth.of(2019, 4);
        YearMonth may = YearMonth.of(2019, 5);
        YearMonth jun = YearMonth.of(2019, 6);
        YearMonth jul = YearMonth.of(2019, 7);
        YearMonth aug = YearMonth.of(2019, 8);
        YearMonth sep = YearMonth.of(2019, 9);
        YearMonth okt = YearMonth.of(2019, 10);
        YearMonth nov = YearMonth.of(2019, 11);
        YearMonth dec = YearMonth.of(2019, 12);

        formattedTimeDataLogic.setSelectedYear(2019);
        formattedTimeDataLogic.buildMasterData(timeEntries);

        Map<YearMonth, List<DailyFormattedDataModel>> masterData = formattedTimeDataLogic.getMonthlyMasterData();
        assertNotNull(masterData);
        assertEquals(12, masterData.size());
        boolean sorted = isSorted(masterData);
        assertTrue(sorted);


        List<DailyFormattedDataModel> dataJan = formattedTimeDataLogic.getMonthlyData(jan);
        assertNotNull(dataJan);
        assertEquals(31, dataJan.size());

        List<DailyFormattedDataModel> dataFeb = formattedTimeDataLogic.getMonthlyData(feb);
        assertNotNull(dataFeb);
        assertEquals(28, dataFeb.size());

        List<DailyFormattedDataModel> dataMar = formattedTimeDataLogic.getMonthlyData(mar);
        assertNotNull(dataMar);
        assertEquals(31, dataMar.size());

        List<DailyFormattedDataModel> dataApr = formattedTimeDataLogic.getMonthlyData(apr);
        assertNotNull(dataApr);
        assertEquals(30, dataApr.size());

        List<DailyFormattedDataModel> dataMay = formattedTimeDataLogic.getMonthlyData(may);
        assertNotNull(dataMay);
        assertEquals(31, dataMay.size());

        List<DailyFormattedDataModel> dataJun = formattedTimeDataLogic.getMonthlyData(jun);
        assertNotNull(dataJun);
        assertEquals(30, dataJun.size());

        List<DailyFormattedDataModel> dataJul = formattedTimeDataLogic.getMonthlyData(jul);
        assertNotNull(dataJul);
        assertEquals(31, dataJul.size());

        List<DailyFormattedDataModel> dataAug = formattedTimeDataLogic.getMonthlyData(aug);
        assertNotNull(dataAug);
        assertEquals(31, dataAug.size());

        List<DailyFormattedDataModel> dataSep = formattedTimeDataLogic.getMonthlyData(sep);
        assertNotNull(dataSep);
        assertEquals(30, dataSep.size());

        List<DailyFormattedDataModel> dataOct = formattedTimeDataLogic.getMonthlyData(okt);
        assertNotNull(dataOct);
        assertEquals(31, dataOct.size());

        List<DailyFormattedDataModel> dataNov = formattedTimeDataLogic.getMonthlyData(nov);
        assertNotNull(dataNov);
        assertEquals(30, dataNov.size());

        List<DailyFormattedDataModel> dataDec = formattedTimeDataLogic.getMonthlyData(dec);
        assertNotNull(dataDec);
        assertEquals(31, dataDec.size());
    }

    private boolean isSorted(Map<YearMonth, List<DailyFormattedDataModel>> list) {
        LocalDate prevdate = LocalDate.MIN;
        for(List<DailyFormattedDataModel> monthList : list.values()) {
            prevdate = LocalDate.MIN; // after each new key is checked, reset since map is not sorted
            for(DailyFormattedDataModel item :monthList) {
                if(!item.getDate().isAfter(prevdate)) {
                    return false;
                }
                prevdate = item.getDate();
            }
        }
        return true;
    }

    @Test
    public void testWeeks() {
        formattedTimeDataLogic.setSelectedYear(2019);
        formattedTimeDataLogic.buildMasterData(timeEntries);
        Map<YearWeek, List<DailyFormattedDataModel>> data = formattedTimeDataLogic.getWeeklyMasterData();
        assertNotNull(data);
        assertEquals(53, data.size());
    }
}