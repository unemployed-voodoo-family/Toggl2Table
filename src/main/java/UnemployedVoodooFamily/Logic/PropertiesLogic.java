package UnemployedVoodooFamily.Logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class PropertiesLogic {


    public Properties loadProps(String path) {
        Properties props = new Properties();
        File file = getFile(path);
            try {
                props.load(new FileInputStream(file));
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        return props;
    }

    public void saveProps(String path, Properties props) {
        File file = getFile(path);
        try {
            props.store(new FileOutputStream(file), "");
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

    private File getFile(String path) {
        URL resourceUrl = getClass().getResource(path);
        File file = null;
        try {
            file = new File(path);
            file.createNewFile();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
