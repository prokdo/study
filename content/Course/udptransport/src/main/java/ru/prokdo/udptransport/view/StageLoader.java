package ru.prokdo.udptransport.view;

import java.io.IOException;
import java.nio.file.Paths;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class StageLoader {
    private StageLoader() {
    }

    public static final void load(String sceneName, Stage stageToLoad, String stageTitle, boolean isResizable) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Paths.get(sceneName).toUri().toURL());
        Scene scene = new Scene(fxmlLoader.load());

        stageToLoad.setScene(scene);
        stageToLoad.setTitle(stageTitle);
        stageToLoad.setResizable(isResizable);
        stageToLoad.show();
    }
}