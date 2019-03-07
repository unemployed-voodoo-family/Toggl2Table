package UnemployedVoodooFamily;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;

public class Logger {
    private static Logger logger = new Logger();

    public static Logger getInstance() {
        return logger;
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private static final String TIME_ENTRY_FILE = "time-entries";
    private final String USER_HOME = System.getProperty("user.dir");
    private final String LOGS_HOME = USER_HOME + "\\TogglTimeSheet\\logs\\";

    private Logger() {
        File theDir = new File(LOGS_HOME);

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
        }
    }

    public void log(Collection<?> dataset) {
        File file = null;
        file = new File(LOGS_HOME + LocalDateTime.now().format(formatter) + "-" + "collection-log.json");
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
        try {
            writer.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
