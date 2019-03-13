package UnemployedVoodooFamily;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class Logger {
    private static Logger logger = new Logger();

    public static Logger getInstance() {
        return logger;
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");

    private Logger() {
        /*File theDir = new File(FilePath.LOGS_HOME.getPath());

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;

            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR created");
            }
        }*/
    }

    public void dumpCollection(Collection<?> dataset, String name) {
        File file = null;
        file = new File(FilePath.LOGS_HOME.getPath() + File.separator + LocalDateTime.now().format(formatter) + "." + name + "-dump.json");
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        writeFile(file, dataset);
    }

    private void writeFile(File file, Collection<?> dataset) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        gson.toJson(dataset, writer);
        /*JsonArray jsonArray= new JsonParser().parse(gson.toJson(dataset)).getAsJsonArray();
        String jsonStr = jsonArray.get(0).toString();
        TimeEntry t = new TimeEntry(jsonStr);
        System.out.println(t.toString());
        //TimeEntry t = new TimeEntry();
        Convert json to timeentry (for later reference)*/
        try {
            writer.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
