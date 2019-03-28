package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import ch.simas.jtoggl.Client;
import ch.simas.jtoggl.Project;
import ch.simas.jtoggl.TimeEntry;
import ch.simas.jtoggl.Workspace;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RawTimeDataLogicTest {

    RawTimeDataLogic logic;

    Map<Long, TimeEntry> timeEntries;
    Map<Long, Project> projects;
    Map<Long, Workspace> workspaces;
    Map<Long, Client> clients;


    @Before
    public void setUp() throws Exception {
        logic = new RawTimeDataLogic();
        /*projects = (List<Project>) TestUtils.getTestList(FilePath.LOGS_HOME.getPath() + File.separator + "projects-dump-test.json");
        workspaces =(List<Workspace>) TestUtils.getTestList(FilePath.LOGS_HOME.getPath() + File.separator + "workspaces-dump-test.json");
        timeEntries = (List<TimeEntry>) TestUtils.getTestList(FilePath.LOGS_HOME.getPath() + File.separator + "timeentries-dump-test.json");*/
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void buildObservableRawTimeData() {
        System.out.println(timeEntries);
        //logic.buildRawMasterData(timeEntries, projects, workspaces, clients, Collections.EMPTY_SET);
    }

    @Test
    public void buildObservableRawTimeDataFiltered() {
    }

    @Test
    public void getDataStartTime() {
    }

    @Test
    public void getDataEndTime() {
    }

    @Test
    public void getFilteredTimeEntries() {
    }
}