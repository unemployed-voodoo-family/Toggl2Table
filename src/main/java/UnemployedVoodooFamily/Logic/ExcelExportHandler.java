package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.MonthlyFormattedDataListFactory;
import ch.simas.jtoggl.TimeEntry;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;

//This class should gather all the data necessary and write them to the excel document
public class ExcelExportHandler {

    private static ExcelWriter excelWriter;
    private static HashMap<String, List> monthlyDataLists;

    public ExcelExportHandler(List<TimeEntry> timeEntries, int year) {
        excelWriter = new ExcelWriter();
        monthlyDataLists = new HashMap<>();
        for(Month month: Month.values()) {
            monthlyDataLists.put(StringUtils.capitalize(month.toString().toLowerCase()),
                                 new MonthlyFormattedDataListFactory().buildMonthlyDataList(timeEntries, month, year));
        }
    }

    public boolean makeExcelDocument() throws IOException {
        //generateDummyLists(); //Remove this later, dummy
        boolean exportSuccess = excelWriter.generateExcelSheet(monthlyDataLists);
        if(exportSuccess)   {
            System.out.println("Successfully exported to excel document");
        }
        return exportSuccess;
    }
}
