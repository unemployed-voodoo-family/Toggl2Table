package UnemployedVoodooFamily.GUI.Content;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;

public class ProfileController {


    public Node loadFXML() throws IOException {
        URL r = getClass().getClassLoader().getResource("Profile.fxml");
        return FXMLLoader.load(r);
    }
}
