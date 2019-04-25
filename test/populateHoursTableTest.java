import UnemployedVoodooFamily.GUI.Content.SettingsController;
import UnemployedVoodooFamily.GUI.GUIBaseController;
import UnemployedVoodooFamily.Logic.SettingsLogic;
import org.junit.Test;

import java.io.IOException;

public class populateHoursTableTest {


    public populateHoursTableTest() {
        testTable();
    }

    @Test
    public void testTable() {
        try {
            new GUIBaseController().start();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        //logic.populateHoursTable();
    }
}
