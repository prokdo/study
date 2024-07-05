package ru.prokdo.ksSolver.util.fxml;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class ViewLoader {
    private ViewLoader() {
    }

    public static final void load(URL sceneURL, Stage stageToLoad, String stageTitle, boolean isResizable) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(sceneURL);
        Scene scene = new Scene(fxmlLoader.load());

        stageToLoad.setScene(scene);
        stageToLoad.setTitle(stageTitle);
        stageToLoad.setResizable(isResizable);
        stageToLoad.show();
    }
}