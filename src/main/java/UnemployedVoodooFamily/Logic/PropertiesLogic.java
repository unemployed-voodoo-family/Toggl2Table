package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.Enums.FilePath;
import UnemployedVoodooFamily.Data.WorkHours;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PropertiesLogic {


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

    public void saveProps(String path, Properties props) {
        File file = getFile(path);
        try {
            OutputStream o = new FileOutputStream(file);
            props.store(o, "");
            o.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

    private File getFile(String path) {
        File file = new File(path);
        try {
            file.createNewFile();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    public void writeToJson(String path, WorkHours wh) {
        File file = getFile(path);

        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        gson.toJson(wh, writer);
        try {
            writer.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public List<WorkHours> loadJson(String path) {
        FileReader reader = null;
        File file = getFile(path);
        try {
            reader = new FileReader(file);
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<WorkHours>>() {}.getType();
        List<WorkHours> list = gson.fromJson(reader, listType);
        try {
            reader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void saveJson(String path, Collection<?> dataset) {
        File file = new File(path);
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
