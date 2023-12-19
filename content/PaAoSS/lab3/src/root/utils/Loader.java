package root.utils;

import java.io.IOException;
import java.nio.file.Paths;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Loader {
    private Loader() {}

    public static final void load(String sceneName, Stage stageToLoad) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Paths.get(sceneName).toUri().toURL());
        Scene scene = new Scene(fxmlLoader.load());

        stageToLoad.setScene(scene);
    }
}