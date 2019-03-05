package UnemployedVoodooFamily.Logic;

public class FormattedTimeDataLogic {

    //Called when the "export to excel" button is pressed
    public boolean buildExcelDocument()    {
        boolean exportSuccess = new ExcelWriter().generateExcelSheet();
        System.out.println(exportSuccess);
        return exportSuccess;
    }

}
