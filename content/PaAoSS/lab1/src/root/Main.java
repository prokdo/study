package root;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Loader.load("..\\resources\\main-view.fxml", stage);
        stage.setTitle("Lab 1");
        stage.show();
    }

    public static final void main(String[] args) {
        launch(args);
    }
}