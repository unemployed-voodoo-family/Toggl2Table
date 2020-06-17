package UnemployedVoodooFamily;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Logic.FileLogic;

import java.io.File;
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

    /**
     * Write a collection to a log file. File name will be generated, based on the current system time.
     * @param dataset Collection to be logged
     * @param name    Name of the file (some other parts will be added, this is just part of the final file name)
     * @return True on success, false on error
     */
    public boolean dumpCollection(Collection<?> dataset, String name) {
        String date = LocalDateTime.now().format(formatter);
        String fileName = FilePath.LOGS_HOME.getPath() + File.separator + date + "." + name + "-dump.json";
        return FileLogic.saveCollectionToJson(fileName, dataset);
    }
}
