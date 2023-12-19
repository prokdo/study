import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

import util.Loader;
import udp.*;

public final class Main extends Application {
    // private final static String SENDER_IP = "127.0.0.1";
    // private final static String PATH_RECEIVER = "out/";

    private final static String PATH_SENDER = "resource/test/test.jpeg";
    private final static int SENDER_PORT = 4444;
    private final static int RECEIVER_PORT = 4445;
    private final static String RECEIVER_IP = "26.11.51.237";

    public final static void main(String[] args) throws Exception {
        launch(args);
        test(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Loader.load("resource/view/main_view.fxml", stage, "UDPTransport");
    }

    public static void test(String[] args) throws Exception {
        UDPSender sender = new UDPSender();
        sender.setPort(SENDER_PORT);
        sender.setReceiverAddres(RECEIVER_IP);
        sender.setReceiverPort(RECEIVER_PORT);

        sender.start();
        if (sender.send(PATH_SENDER))
            System.out.println("File sent successfully");
        else
            System.out.println("File sent unsuccessfully");
        sender.stop();
    }
}
