package ru.prokdo.ksSolver;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.prokdo.ksSolver.util.fxml.ViewLoader;

public final class App extends Application {
    public final static void run(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        ViewLoader.load(getClass().getResource("/resources/ksSolver/view/main_view.fxml"), stage, "Knapsack solver", true);
    }
}
