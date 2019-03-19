package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.MonthlySheetRowEntry;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;

public class ExcelWriter {

    private Workbook workbook;

    private CellStyle headerCellStyle;
    private CellStyle dataCellStyle;
    private CellStyle dataCellStyleAlternate;
    private CellStyle summaryCellStyle;
    private static String[] columnNames = {"Week number", "Week day", "Date", "Supposed work hours", "Worked hours", "Overtime"};

    public ExcelWriter() {
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
        for(String key: monthLists.keySet()) {
            constructSheet(key, monthLists.get(key));
        }
        int order = 0;
        for(Month month: Month.values()) {
            workbook.setSheetOrder(StringUtils.capitalize(month.toString().toLowerCase()), order);
            order++;
        }
        FileOutputStream fileOut = new FileOutputStream("Time Report.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        workbook.close();
    }

    private void constructSheet(String sheetName, ArrayList<MonthlySheetRowEntry> data) {
        Sheet sheet = this.workbook.createSheet(sheetName);

        sheet.createFreezePane(0, 1);
        //Create Header row
        Row headerRow = sheet.createRow(0);
        for(int i = 0; i < (columnNames.length); i++) { //For all the columns
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(headerCellStyle);
        }

        //Create data rows for every data entry
        int rowNumber = 1;
        int previousRowWeek = 0;
        boolean alternateStyle = false;
        for(MonthlySheetRowEntry m: data) {
            Row row = sheet.createRow(rowNumber++);
            row.createCell(0);
            if(previousRowWeek != m.getWeekNumber()) {
                alternateStyle = !alternateStyle;
                previousRowWeek = m.getWeekNumber();
                row.getCell(0).setCellValue("Week " + m.getWeekNumber());
            }
            row.createCell(1).setCellValue(m.getWeekDay());
            row.createCell(2).setCellValue(m.getDate());
            row.createCell(3).setCellValue(m.getSupposedWorkHours());
            row.createCell(4).setCellValue(m.getActualWorkedHours());
            row.createCell(5).setCellFormula(
                    "SUM(" + row.getCell(4).getAddress().formatAsString() + "-" + row.getCell(3).getAddress()
                                                                                     .formatAsString() + ")");
            for(Cell cell: row) {
                //TODO rewrite to use week number
                if(alternateStyle) {
                    cell.setCellStyle(dataCellStyleAlternate);
                }
                else {
                    cell.setCellStyle(dataCellStyle);
                }
            }
        }

        //TODO make a for loop to construct all the cells
        Row summaryRow = sheet.createRow(rowNumber);
        summaryRow.createCell(0).setCellValue(sheetName + " summary");
        summaryRow.createCell(1);
        summaryRow.createCell(2);
        summaryRow.createCell(3).setCellFormula("SUM(" + CellReference
                .convertNumToColString(summaryRow.getCell(3).getColumnIndex()) + 1 + ":" + CellReference
                .convertNumToColString(summaryRow.getCell(3).getColumnIndex()) + rowNumber + ")");
        summaryRow.createCell(4).setCellFormula("SUM(" + CellReference
                .convertNumToColString(summaryRow.getCell(4).getColumnIndex()) + 1 + ":" + CellReference
                .convertNumToColString(summaryRow.getCell(4).getColumnIndex()) + rowNumber + ")");
        summaryRow.createCell(5).setCellFormula("SUM(" + CellReference
                .convertNumToColString(summaryRow.getCell(5).getColumnIndex()) + 1 + ":" + CellReference
                .convertNumToColString(summaryRow.getCell(5).getColumnIndex()) + rowNumber + ")");

        for(Cell cell: summaryRow) {
            cell.setCellStyle(summaryCellStyle);
        }

        for(int i = 0; i < columnNames.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void setupStandardRowFormatting() {
        headerCellStyle = workbook.createCellStyle();
        summaryCellStyle = workbook.createCellStyle();
        dataCellStyle = workbook.createCellStyle();
        dataCellStyleAlternate = workbook.createCellStyle();

        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldFont.setFontHeightInPoints((short) 13);

        Font dataFont = workbook.createFont();
        dataFont.setFontHeightInPoints((short) 12);

        headerCellStyle.setFont(boldFont);
        headerCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        headerCellStyle.setBorderBottom(BorderStyle.MEDIUM);

        summaryCellStyle.setFont(boldFont);
        summaryCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        summaryCellStyle.setBorderTop(BorderStyle.MEDIUM);

        dataCellStyle.setFont(dataFont);
        dataCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        dataCellStyle.setFillForegroundColor(IndexedColors.TURQUOISE.getIndex());
        dataCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        dataCellStyleAlternate.setFont(dataFont);
        dataCellStyleAlternate.setAlignment(HorizontalAlignment.RIGHT);
        dataCellStyleAlternate.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        dataCellStyleAlternate.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }
}
