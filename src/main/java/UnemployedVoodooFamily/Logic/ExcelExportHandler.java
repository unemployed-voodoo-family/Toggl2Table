package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.MonthlySheetRowEntry;
import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
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
        if(exportSuccess)   {
            System.out.println("Successfully exported to excel document");
        }
        return exportSuccess;
    }

    //TODO remove these when proper data getting methods have been implemented
    private ArrayList<MonthlySheetRowEntry> buildDummyList(double testDouble) {
        ArrayList<MonthlySheetRowEntry> l = new ArrayList<>();
        l.add(new MonthlySheetRowEntry(1 + (4*(int)testDouble - 4), DayOfWeek.MONDAY, LocalDate.now(), 8, 12));
        l.add(new MonthlySheetRowEntry(1 + (4*(int)testDouble - 4), DayOfWeek.TUESDAY, LocalDate.now(), 8, 8));
        l.add(new MonthlySheetRowEntry(1 + (4*(int)testDouble - 4), DayOfWeek.WEDNESDAY, LocalDate.now(), 8, 8));
        l.add(new MonthlySheetRowEntry(1 + (4*(int)testDouble - 4), DayOfWeek.THURSDAY, LocalDate.now(), 8, 8));
        l.add(new MonthlySheetRowEntry(1 + (4*(int)testDouble - 4), DayOfWeek.FRIDAY, LocalDate.now(), 8, 8));

        l.add(new MonthlySheetRowEntry(2 + (4*(int)testDouble - 4), DayOfWeek.MONDAY, LocalDate.now(), 8, 8));
        l.add(new MonthlySheetRowEntry(2 + (4*(int)testDouble - 4), DayOfWeek.TUESDAY, LocalDate.now(), 8, 7));
        l.add(new MonthlySheetRowEntry(2 + (4*(int)testDouble - 4), DayOfWeek.WEDNESDAY, LocalDate.now(), 8, 7));
        l.add(new MonthlySheetRowEntry(2 + (4*(int)testDouble - 4), DayOfWeek.THURSDAY, LocalDate.now(), 8, 8));
        l.add(new MonthlySheetRowEntry(2 + (4*(int)testDouble - 4), DayOfWeek.FRIDAY, LocalDate.now(), 8, 8));

        l.add(new MonthlySheetRowEntry(3 + (4*(int)testDouble - 4), DayOfWeek.TUESDAY, LocalDate.now(), 8, 9));
        l.add(new MonthlySheetRowEntry(3 + (4*(int)testDouble - 4), DayOfWeek.WEDNESDAY, LocalDate.now(), 8, 9));
        l.add(new MonthlySheetRowEntry(3 + (4*(int)testDouble - 4), DayOfWeek.THURSDAY, LocalDate.now(), 8, 8));
        l.add(new MonthlySheetRowEntry(3 + (4*(int)testDouble - 4), DayOfWeek.FRIDAY, LocalDate.now(), 8, 8));

        l.add(new MonthlySheetRowEntry(4 + (4*(int)testDouble - 4), DayOfWeek.MONDAY, LocalDate.now(), 7.5, 8-testDouble));
        l.add(new MonthlySheetRowEntry(4 + (4*(int)testDouble - 4), DayOfWeek.TUESDAY, LocalDate.now(), 7.5, 8-testDouble));
        l.add(new MonthlySheetRowEntry(4 + (4*(int)testDouble - 4), DayOfWeek.WEDNESDAY, LocalDate.now(), 7.5, 14));
        l.add(new MonthlySheetRowEntry(4 + (4*(int)testDouble - 4), DayOfWeek.THURSDAY, LocalDate.now(), 7.5, 8));
        l.add(new MonthlySheetRowEntry(4 + (4*(int)testDouble - 4), DayOfWeek.FRIDAY, LocalDate.now(), 7.5, 8));

        return l;
    }

    private void generateDummyLists() {
        int testInt = 1;
        for(String key: monthlyDataLists.keySet()) {
            monthlyDataLists.put(key, buildDummyList(testInt));
            testInt++;
        }
    }
}
