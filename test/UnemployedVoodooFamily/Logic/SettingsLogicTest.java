package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Data.WorkHours;
import org.junit.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class SettingsLogicTest {

    SettingsLogic logic;
    String path = FilePath.getCurrentUserWorkhours();
    FileLogic propsLogic = new FileLogic();
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
    public void setWorkHours1() {
        LocalDate d2 = LocalDate.of(2019, 11, 30);
        LocalDate d3 = LocalDate.of(2019, 12, 30);
        LocalDate d4 = LocalDate.of(2019, 4, 30);
        LocalDate d5 = LocalDate.of(2019, 5, 30);

        logic.setWorkHours(d2, d3, "7", "");
        this.workHours = propsLogic.loadJson(path);
        assertEquals(1, workHours.size());

        logic.setWorkHours(d4, d5, "8", "");
        this.workHours = propsLogic.loadJson(path);
        assertEquals(2, workHours.size());
        LocalDate to = workHours.get(1).getTo();
        assertEquals(d5, to);

        logic.setWorkHours(d5, d3, "8", "");
        this.workHours = propsLogic.loadJson(path);
        assertEquals(1, workHours.size());
        LocalDate to1 = workHours.get(0).getTo();
        assertEquals(d3, to1);
        LocalDate from1 = workHours.get(0).getFrom();
        assertEquals(d4, from1);
    }

    @Test
    public void setWorkHours() {
        LocalDate dFeb = LocalDate.of(2019, 2, 1);
        LocalDate dJan = LocalDate.of(2019, 1, 1);
        LocalDate dNov = LocalDate.of(2019, 11, 1);
        LocalDate dDec = LocalDate.of(2019, 12, 1);
        LocalDate dApr = LocalDate.of(2019, 4, 1);
        LocalDate dMay = LocalDate.of(2019, 5, 1);
        //LocalDate d6 = LocalDate.of(2019, 11, 30);
        //LocalDate d7 = LocalDate.of(2019, 11, 30);


        //make an entry covering the whole year
        logic.setWorkHours(dFeb, dNov, "7", "");
        this.workHours = propsLogic.loadJson(path);
        assertEquals(1, workHours.size());

        logic.setWorkHours(dJan, dNov, "8", "");
        this.workHours = propsLogic.loadJson(path);
        assertEquals(1, workHours.size());
        LocalDate from = workHours.get(0).getFrom();
        assertEquals(dJan, from);
        LocalDate to = workHours.get(0).getTo();
        assertEquals(dNov, to);

        logic.setWorkHours(dJan, dDec, "8", "");
        this.workHours = propsLogic.loadJson(path);
        assertEquals(1, workHours.size());
        LocalDate to2 = workHours.get(0).getTo();
        assertEquals(dDec, to2);

        // place an entry in the middle with same hours value
        logic.setWorkHours(dApr, dMay, "8", "same");
        this.workHours = propsLogic.loadJson(path);
        assertEquals(1, workHours.size());
        LocalDate to3 = workHours.get(0).getTo();
        assertEquals(dDec, to3);
        LocalDate from3 = workHours.get(0).getFrom();
        assertEquals(dJan, from3);
        assertEquals(1, workHours.size());

        // place an entry in the middle with different hours value
        logic.setWorkHours(dApr, dMay, "7", "different");
        this.workHours = propsLogic.loadJson(path);
        System.out.println(workHours.get(1));
        LocalDate to4 = workHours.get(1).getTo();
        assertEquals(dDec, to3);
        LocalDate from4 = workHours.get(1).getFrom();
        assertEquals(dJan, from3);
        assertEquals(3, workHours.size());



    }

}