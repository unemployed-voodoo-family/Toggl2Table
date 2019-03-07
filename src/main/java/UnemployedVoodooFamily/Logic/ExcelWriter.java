package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.MonthlySheetRowEntry;
import UnemployedVoodooFamily.Enums.Month;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelWriter {

    private Workbook workbook;

    private static CellStyle headerCellStyle;
    private static CellStyle dataCellStyle;
    private static String[] columnNames = {"Week number", "Week day", "Date" ,"Supposed work hours", "Worked hours", "Overtime"};

    public ExcelWriter()    {
        this.workbook = new XSSFWorkbook();
        setupStandardRowFormatting();
    }

    public boolean generateExcelSheet(HashMap<String, ArrayList> monthLists) {
        boolean success = false;
        try {
            buildWorkbook(monthLists);
            success = true;
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return success; //Return true if document was build correctly, false if failed
    }

    private void buildWorkbook(HashMap<String, ArrayList> monthLists) throws IOException {
        CreationHelper creationHelper = workbook.getCreationHelper();
        for(String key : monthLists.keySet())   {
            constructSheet(key, monthLists.get(key));
        }
        int order = 0;
        for(Month month : Month.values())   {
            workbook.setSheetOrder(StringUtils.capitalize(month.toString().toLowerCase()), order);
            order++;
        }
        FileOutputStream fileOut = new FileOutputStream("Time Report.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        workbook.close();
    }

    private void constructSheet(String sheetName, ArrayList<MonthlySheetRowEntry> data)  {
        Sheet sheet = this.workbook.createSheet(sheetName);

        //Create Header row
        Row headerRow = sheet.createRow(0);
        for(int i = 0; i < (columnNames.length); i++) { //For all the columns
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(headerCellStyle);
        }

        //Create data rows for every data entry
        int rowNumber = 1;
        for(MonthlySheetRowEntry m : data)  {
            Row row = sheet.createRow(rowNumber++);
            row.createCell(0).setCellValue(m.getWeekNumber());
            row.createCell(1).setCellValue(m.getWeekDay());
            row.createCell(2).setCellValue(m.getDate());
            row.createCell(3).setCellValue(m.getSupposedWorkHours());
            row.createCell(4).setCellValue(m.getActualWorkedHours());
            row.createCell(5).setCellFormula("SUM(" + row.getCell(4).getAddress().formatAsString()
                                                     + "-" + row.getCell(3).getAddress().formatAsString()
                                                     + ")");
            for(Cell cell : row)    {
                cell.setCellStyle(dataCellStyle);
            }
        }
        for(int i = 0; i < columnNames.length; i++)  {
            sheet.autoSizeColumn(i);
        }
    }

    private void setupStandardRowFormatting()   {
        headerCellStyle = workbook.createCellStyle();
        dataCellStyle = workbook.createCellStyle();

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.RIGHT);

        dataCellStyle.setAlignment(HorizontalAlignment.RIGHT);
    }
}
