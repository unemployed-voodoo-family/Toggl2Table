package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.MonthlySheetRowEntry;
import UnemployedVoodooFamily.Enums.Month;
import UnemployedVoodooFamily.Enums.WeekDay;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

//This class should gather all the data necessary and write them to the excel document
public class ExcelExportHandler {

    private static ExcelWriter excelWriter;
    private static HashMap<String, ArrayList> monthlyDataLists;

    public ExcelExportHandler() {
        excelWriter = new ExcelWriter();
        monthlyDataLists = new HashMap<>();
        for(Month month: Month.values()) {
            monthlyDataLists.put(StringUtils.capitalize(month.toString().toLowerCase()), new ArrayList());
        }
    }

    public boolean makeExcelDocument() {
        generateDummyLists(); //Remove this later, dummy
        boolean exportSuccess = excelWriter.generateExcelSheet(monthlyDataLists);
        System.out.println(exportSuccess);
        return exportSuccess;
    }

    //TODO remove these when proper data getting methods have been implemented
    private ArrayList<MonthlySheetRowEntry> buildDummyList(double testDouble) {
        ArrayList<MonthlySheetRowEntry> l = new ArrayList<>();
        l.add(new MonthlySheetRowEntry("Week Number", WeekDay.FRIDAY, LocalDate.now(), testDouble, testDouble + 1));
        return l;
    }

    private void generateDummyLists() {
        int testInt = 1;
        for(String key: monthlyDataLists.keySet()) {
            testInt++;
            monthlyDataLists.put(key, buildDummyList(testInt));

        }
    }

}
