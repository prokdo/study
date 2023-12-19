package root;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Loader {
    private Loader() {}

    public static final void load(String sceneName, Stage stageToLoad) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(sceneName));
        Scene scene = new Scene(fxmlLoader.load());

        stageToLoad.setScene(scene);
    }
}
