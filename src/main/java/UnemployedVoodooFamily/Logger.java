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
    }

    public void dumpCollection(Collection<?> dataset, String name) {
        File file = new File(FilePath.LOGS_HOME.getPath() + File.separator + LocalDateTime.now()
                                                                                          .format(formatter) + "." + name + "-dump.json");
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
        Gson gson = builder.setPrettyPrinting().serializeNulls().create();
        gson.toJson(dataset, writer);
        try {
            writer.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
