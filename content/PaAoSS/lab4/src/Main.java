import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

import util.Loader;

public final class Main extends Application {
    public final static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Loader.load("resource/view/main_view.fxml", stage, "Lab 4");
    }
}
