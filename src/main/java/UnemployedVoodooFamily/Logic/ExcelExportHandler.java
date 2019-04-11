package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.MonthlyFormattedDataListFactory;
import ch.simas.jtoggl.TimeEntry;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.Month;
import java.util.HashMap;
import java.util.List;

//This class should gather all the data necessary and write them to the excel document
public class ExcelExportHandler {

    private ExcelWriter excelWriter;
    private HashMap<String, List> monthlyDataLists;
    private int year;


    public ExcelExportHandler(List<TimeEntry> timeEntries, int year) {
        excelWriter = new ExcelWriter();
        this.year = year;
        monthlyDataLists = new HashMap<>();
        for(Month month: Month.values()) {
            monthlyDataLists.put(StringUtils.capitalize(month.toString().toLowerCase()),
                                 new MonthlyFormattedDataListFactory().buildMonthlyDataList(timeEntries, month, year));
        }
    }

    public boolean makeExcelDocument() throws IOException {
        //generateDummyLists(); //Remove this later, dummy
        boolean exportSuccess = excelWriter.generateExcelSheet(monthlyDataLists, year);
        if(exportSuccess)   {
            System.out.println("Successfully exported to excel document");
        }
        return exportSuccess;
    }
}
