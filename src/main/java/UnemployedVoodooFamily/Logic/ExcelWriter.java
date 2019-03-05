package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.MonthlyFormattedTimeData;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExcelWriter {


    private static String[] timeOverviewColumns = {"Week number", "Supposed work hours", "Worked hours", "Overtime"};
    //TODO create own data class for excel sheet(?)
    private static List<MonthlyFormattedTimeData> monthlyEntries = new ArrayList<>();

    //TODO feed the generator with appropriate parameters
    public boolean generateExcelSheet() {
        boolean success = false;
        try {
            buildEntriesList(); //Put list in as parameter(?)
            buildSheet();
            success = true;
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return success; //Return true if document was build correctly, false if failed
    }

    private void buildEntriesList() { //TODO parameters

        //########TESTING########
        monthlyEntries.add(new MonthlyFormattedTimeData("Jan", "2", "3", "4", "1"));
    }

    private void buildSheet() throws IOException {
        //Create Excel workbook ( .xlsx file)
        Workbook workbook = new XSSFWorkbook();
        CreationHelper creationHelper = workbook.getCreationHelper();


        //Set up fonts
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        //TODO Create a separate method which generates the cell styles
        //Create cell style
        CellStyle summaryCellStyle = workbook.createCellStyle();
        summaryCellStyle.setFont(headerFont);

        //TODO create a "sheet" factory
        Sheet janSheet = workbook.createSheet(monthlyEntries.get(0).getMonth());

        //Create row
        Row janSummaryRow = janSheet.createRow(0);

        //Create header summary cells
        for(int i = 0; i < (timeOverviewColumns.length); i++) { //For all the columns
            Cell cell = janSummaryRow.createCell(i);
            cell.setCellValue(timeOverviewColumns[i]);
            cell.setCellStyle(summaryCellStyle);
        }

        //Create information cells

        int rowNumber = 1;
        for(MonthlyFormattedTimeData entry: monthlyEntries) {
            Row row = janSheet.createRow(rowNumber++);
            row.createCell(0).setCellValue(entry.getWeekNumber());
            row.createCell(1).setCellValue(Double.parseDouble(entry.getSupposedHours()));
            row.createCell(2).setCellValue(Double.parseDouble(entry.getWorkedHours()));
            row.createCell(3).setCellFormula("SUM(" + row.getCell(2).getAddress().formatAsString()
                                                     + "-" + row.getCell(1).getAddress().formatAsString()
                                                     + ")");
        }

        //resize columns
        for(int i = 0; i <timeOverviewColumns.length; i++)  {
            janSheet.autoSizeColumn(i);
        }

        //Write file to output
        FileOutputStream fileOut = new FileOutputStream("Time Report.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        workbook.close();

    }

}
