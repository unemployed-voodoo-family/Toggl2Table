package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.WorkHours;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.*;

/**
 * Handles reading/writing operations for files
 */
public class FileLogic {


    /**
     * Load properties from a file
     * @param path Path to a file where properties are stored
     * @return
     */
    public Properties loadProps(String path) {
        Properties props = new Properties();
        File file = getFile(path);
        try {
            InputStream i = new FileInputStream(file);
            props.load(i);
            i.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    private static File getFile(String path) {
        File file = new File(path);
        try {
            file.createNewFile();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * Read a JSON file, interpret it as a list of objects
     * @param path Path to the JSON file
     * @return The collection containing the objects from the JSON file
     */
    public static List<WorkHours> loadWorkHourListFromJsonFile(String path) {
        List<WorkHours> list = null;
        File file = getFile(path);
        try {
            FileReader reader = new FileReader(file);
            Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
            TypeToken<ArrayList<WorkHours>> token = new TypeToken<ArrayList<WorkHours>>() {};
            list = gson.fromJson(reader, token.getType());
            reader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Save a collection to a JSON file. Create the file and all parent directories if necessary.
     * @param path PAth to the JSON file
     * @param dataset the data to be stored
     * @return True on success, false on error
     */
    public static boolean saveCollectionToJson(String path, Collection<?> dataset) {
        File file = new File(path);
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return writeCollectionToFile(file, dataset);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Write a collection to a file.
     * @param file A file, already opened for writing
     * @param dataset Dataset to write to a file
     * @return True on success, false on error
     */
    private static boolean writeCollectionToFile(File file, Collection<?> dataset) {
        try {
            FileWriter writer = new FileWriter(file);
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.setPrettyPrinting().serializeNulls().create();
            gson.toJson(dataset, writer);
            writer.close();
            return true;
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
