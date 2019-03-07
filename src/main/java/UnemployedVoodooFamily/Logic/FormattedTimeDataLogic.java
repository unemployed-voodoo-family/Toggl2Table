package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Enums.Month;

import java.util.ArrayList;
import java.util.HashMap;

public class FormattedTimeDataLogic {



    public FormattedTimeDataLogic() {

    }

    //Called when the "export to excel" button is pressed
    public boolean exportToExcelDocument()    {
        ExcelExportHandler exportHandler = new ExcelExportHandler();
        return exportHandler.makeExcelDocument();
    }
}
