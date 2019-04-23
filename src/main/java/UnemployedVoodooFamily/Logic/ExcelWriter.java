package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.DailyFormattedDataModel;
import UnemployedVoodooFamily.Data.Enums.FilePath;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Month;
import java.util.HashMap;
import java.util.List;

public class ExcelWriter {

    private Workbook workbook;

    private CellStyle headerCellStyle;
    private CellStyle dataCellStyle;
    private CellStyle dataCellStyleAlt;
    private CellStyle summaryCellStyle;
    private static String[] columnNames = {"Week number", "Week day", "Date", "Supposed work hours", "Hours worked", "+/- Hours", "Accumulated hours", "Notes"};

    public ExcelWriter() {
        this.workbook = new XSSFWorkbook();
        setupStandardRowFormatting();
    }

    public boolean generateExcelSheet(HashMap<String, List> monthLists, int year) throws IOException {
        boolean success = false;
        buildWorkbook(monthLists, year);
        success = true;
        return success; //Return true if document was build correctly, false if failed
    }

    private void buildWorkbook(HashMap<String, List> monthLists, int year) throws IOException {
        CreationHelper creationHelper = workbook.getCreationHelper();
        for(String key: monthLists.keySet()) {
            constructMonthlySheet(key, monthLists.get(key));
        }
        int order = 0;
        for(Month month: Month.values()) {
            workbook.setSheetOrder(StringUtils.capitalize(month.toString().toLowerCase()), order);
            order++;
        }
        workbook.setActiveSheet(0);
        workbook.setSelectedTab(0);
        FileOutputStream fileOut = new FileOutputStream(FilePath.APP_HOME.getPath() +"\\Time Report " + year + ".xlsx");
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    private void constructYearlySummarySheet()  {

    }

    private void constructMonthlySheet(String sheetName, List<DailyFormattedDataModel> data) {
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
        for(DailyFormattedDataModel m: data) {
            Row row = sheet.createRow(rowNumber);
            row.createCell(0);

            if(m.getWeek().getWeek() != previousRowWeek) {
                alternateStyle = ! alternateStyle;
                previousRowWeek = m.getWeek().getWeek();
                row.getCell(0).setCellValue("Week " + m.getWeek().getWeek());
            }
            row.createCell(1).setCellValue(m.getWeekday());
            row.createCell(2).setCellValue(m.getDate().toString());
            row.createCell(3).setCellValue(m.getSupposedHours());
            row.createCell(4).setCellValue(m.getWorkedHours());
            row.createCell(5).setCellFormula(
                    "SUM(" + row.getCell(4).getAddress().formatAsString()
                            + "-" + row.getCell(3).getAddress().formatAsString() + ")");
            if(rowNumber > 1) {
                row.createCell(6).setCellFormula(
                        "SUM(" + sheet.getRow(rowNumber-1).getCell(6).getAddress().formatAsString()
                                + "+" + row.getCell(5).getAddress().formatAsString() + ")");
            }
            else {
                row.createCell(6).setCellFormula(row.getCell(5).getAddress().formatAsString());
            }
            row.createCell(7).setCellValue(m.getNote());
            for(Cell cell: row) {
                if(alternateStyle) {
                    if(cell.getColumnIndex() == 5 || cell.getColumnIndex() == 3) {
                        CellStyle dataAltBorder = workbook.createCellStyle();
                        dataAltBorder.cloneStyleFrom(dataCellStyleAlt);
                        dataAltBorder.setBorderLeft(BorderStyle.THICK);
                        dataAltBorder.setLeftBorderColor(IndexedColors.WHITE.getIndex());
                        cell.setCellStyle(dataAltBorder);
                    }
                    else {
                        cell.setCellStyle(dataCellStyleAlt);
                    }
                }
                else {
                    if(cell.getColumnIndex() == 5 || cell.getColumnIndex() == 3) {
                        CellStyle dataBorder = workbook.createCellStyle();
                        dataBorder.cloneStyleFrom(dataCellStyle);
                        dataBorder.setBorderLeft(BorderStyle.THICK);
                        dataBorder.setLeftBorderColor(IndexedColors.WHITE.getIndex());
                        cell.setCellStyle(dataBorder);
                    }
                    else {
                        cell.setCellStyle(dataCellStyle);
                    }
                }
            }
            rowNumber++;
        }

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
        summaryRow.createCell(5);
        summaryRow.createCell(6).setCellFormula(sheet.getRow(rowNumber-1).getCell(6).getAddress().formatAsString());
        summaryRow.createCell(7);

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
        dataCellStyleAlt = workbook.createCellStyle();
        Short numberFormat = workbook.createDataFormat().getFormat("0.00");

        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldFont.setFontHeightInPoints((short) 13);

        Font dataFont = workbook.createFont();
        dataFont.setFontHeightInPoints((short) 12);

        headerCellStyle.setFont(boldFont);
        headerCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        headerCellStyle.setBorderBottom(BorderStyle.MEDIUM);

        summaryCellStyle.setFont(boldFont);
        summaryCellStyle.setDataFormat(numberFormat);
        summaryCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        summaryCellStyle.setBorderTop(BorderStyle.MEDIUM);

        XSSFColor dataColor = new XSSFColor(java.awt.Color.decode("#F2FFC8"));
        dataCellStyle.setFont(dataFont);
        dataCellStyle.setDataFormat(numberFormat);
        dataCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        dataCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        ((XSSFCellStyle)dataCellStyle).setFillForegroundColor(dataColor);

        XSSFColor dataAltColor = new XSSFColor(java.awt.Color.decode("#C7F9DB"));
        dataCellStyleAlt.cloneStyleFrom(dataCellStyle);
        ((XSSFCellStyle)dataCellStyleAlt).setFillForegroundColor(dataAltColor);
    }
}
