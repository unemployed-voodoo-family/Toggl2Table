package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import ch.simas.jtoggl.TimeEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.ss.formula.functions.T;

import java.io.*;
import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestUtils {

    private String testFileName = "timeentries-dump-test.json";

    public static List<TimeEntry> getTestTimeEntries() {
        File f = new File(FilePath.LOGS_HOME.getPath());

        File[] matchingFiles = f.listFiles((dir, name) -> name.endsWith("timeentries-dump.json"));
        File file = new File(FilePath.LOGS_HOME.getPath(), "timeentries-dump-test.json");

        List<TimeEntry> timeEntries = null;
        try(FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<TimeEntry>>(){}.getType();
            timeEntries = gson.fromJson(reader, listType);
        }
        catch(FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("========= ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! =========");
            System.out.println("Please setup test-file to use unit tests");
            System.out.println("\"timeentries-dump-test.json\" should be placed under TogglTimeSheet\\logs");
            System.out.println("========= ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! =========\n");


        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return timeEntries;
    }

    public

    static List<?> getTestList(String path) {
        File f = new File(path);

        List<?> list = null;

        try {
            FileReader reader = new FileReader(f);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<?>>(){}.getType();
            list = gson.fromJson(reader, listType);
        }
        catch(FileNotFoundException e) {
            System.out.println("Could not find file " + f.toString());
        }

        return list;
    }
}
