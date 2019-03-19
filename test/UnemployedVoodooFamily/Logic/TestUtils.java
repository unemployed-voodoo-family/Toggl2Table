package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import ch.simas.jtoggl.TimeEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestUtils {


    public static List<TimeEntry> getTestTimeEntries() {
        File f = new File(FilePath.LOGS_HOME.getPath());
        File[] matchingFiles = f.listFiles((dir, name) -> name.endsWith("timeentries-dump.json"));
        List<TimeEntry> timeEntries = null;
        try {
            FileReader reader = new FileReader(matchingFiles[0]);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<TimeEntry>>(){}.getType();
            timeEntries = gson.fromJson(reader, listType);
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }


        /*File file = null;
        try {
            file = new File(path);
            file.createNewFile();
        }
        catch(IOException e) {
            e.printStackTrace();
        }*/
        return timeEntries;
    }
}
