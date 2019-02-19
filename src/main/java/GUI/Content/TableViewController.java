package GUI.Content;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import java.io.IOException;
import java.net.URL;

public class TableViewController{
    //TODO:make content scale properly when being resized

    public Node loadFXML() throws IOException {
        URL r = getClass().getClassLoader().getResource("Table.fxml");
        return FXMLLoader.load(r);
    }

}
