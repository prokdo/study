package ru.prokdo.udptransport;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

import ru.prokdo.udptransport.view.StageLoader;

public final class Main extends Application {
    public final static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        StageLoader.load("rsrc/view/main_view.fxml", stage, "UDPTransport", true);
    }
}
