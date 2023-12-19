package root;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import root.utils.Loader;

public final class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Loader.load("src\\resources\\views\\main-view.fxml", stage);
        stage.setTitle("Lab 2");
        stage.show();
    }
    
    public final static void main(String[] args) {
        launch(args);
    }
}