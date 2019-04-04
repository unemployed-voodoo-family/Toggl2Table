package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.DateRange;
import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Data.WorkHours;
import org.junit.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

public class SettingsLogicTest {

    SettingsLogic logic;
    String path = FilePath.getCurrentUserWorkhours();
    PropertiesLogic propsLogic = new PropertiesLogic();
    List<WorkHours> workHours;

    @Before
    public void SetUp() {
        logic = new SettingsLogic(path);
    }

    @After
    public void TearDown() {
        boolean success = propsLogic.deleteFile(path);
        assertTrue(success);
    }

    @Test
    public void setWorkHours() {
        LocalDate d1 = LocalDate.of(2019, 1, 1);
        LocalDate d2 = LocalDate.of(2019, 11, 30);
        LocalDate d3 = LocalDate.of(2019, 12, 30);
        LocalDate d4 = LocalDate.of(2019, 4, 30);
        LocalDate d5 = LocalDate.of(2019, 5, 30);
        //LocalDate d6 = LocalDate.of(2019, 11, 30);
        //LocalDate d7 = LocalDate.of(2019, 11, 30);
        logic.setWorkHours(d1, d2, "7");
        this.workHours = propsLogic.loadJson(path);
        assertEquals(1, workHours.size());

        logic.setWorkHours(d1, d3, "8");
        this.workHours = propsLogic.loadJson(path);
        assertEquals(1, workHours.size());
        LocalDate to = workHours.get(0).getTo();
        assertEquals(d3, to);

        logic.setWorkHours(d4, d5, "8");
        this.workHours = propsLogic.loadJson(path);
        assertEquals(2, workHours.size());

        logic.setWorkHours(d4, d5, "7");
        this.workHours = propsLogic.loadJson(path);
        assertEquals(3, workHours.size());

    }

}