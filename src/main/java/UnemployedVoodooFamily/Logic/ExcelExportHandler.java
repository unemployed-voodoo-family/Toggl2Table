package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.DailyFormattedDataModel;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.Month;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//This class should gather all the data necessary and write them to the excel document
public class ExcelExportHandler {

    private ExcelWriter excelWriter;
    private HashMap<String, List> monthlyDataLists;
    private int year;


    public ExcelExportHandler(Map<YearMonth, List<DailyFormattedDataModel>> timeEntries, int year) {
        excelWriter = new ExcelWriter();
        this.year = year;
        monthlyDataLists = new HashMap<>();
        for(Month month: Month.values()) {
            monthlyDataLists.put(StringUtils.capitalize(month.toString().toLowerCase()),
                                 timeEntries.get(YearMonth.of(year, month)));
        }
    }

    public boolean makeExcelDocument() throws IOException {
        boolean exportSuccess = excelWriter.generateExcelSheet(monthlyDataLists, year);
        if(exportSuccess)   {
            System.out.println("Successfully exported to excel document");
        }
        return exportSuccess;
    }
}
