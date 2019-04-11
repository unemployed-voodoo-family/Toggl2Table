package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import ch.simas.jtoggl.TimeEntry;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    private String testFileName = "timeentries-dump-test.json";

    public static List<TimeEntry> getTestTimeEntries() {
        File f = new File(FilePath.LOGS_HOME.getPath());

        //File[] matchingFiles = f.listFiles((dir, name) -> name.endsWith("timeentries-dump.json"));
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
        }
        catch(IllegalStateException | JsonSyntaxException e) {
            System.out.println("========= ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! =========");
            System.out.println("Your \"timeentries-dump-test.json\" file probably \n has an outdated format");
            System.out.println("========= ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! ! =========\n");
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
