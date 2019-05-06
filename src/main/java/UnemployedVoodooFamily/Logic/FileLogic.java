package UnemployedVoodooFamily.Logic;

import UnemployedVoodooFamily.Data.WorkHours;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class FileLogic {


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

    public List<WorkHours> loadJson(String path) {
        FileReader reader = null;
        File file = getFile(path);
        List<WorkHours> list = null;
        try {
            reader = new FileReader(file);
            Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
            Type listType = new TypeToken<List<WorkHours>>() {}.getType();
            list = gson.fromJson(reader, listType);
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                reader.close();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
        return list == null ? new ArrayList<>() : list;
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
