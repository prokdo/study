import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

import util.Loader;
import udp.*;

public final class Main extends Application {
    // private final static String SENDER_IP = "127.0.0.1";
    // private final static String PATH_RECEIVER = "out/";

    private final static String PATH_RECEIVER = "out";
    private final static int SENDER_PORT = 4444;
    private final static int RECEIVER_PORT = 4445;
    private final static String SENDER_IP = "26.11.51.237";

    public final static void main(String[] args) throws Exception {
        launch(args);
        test(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Loader.load("resource/view/main_view.fxml", stage, "UDPTransport");
    }

    public static void test(String[] args) throws Exception {
        UDPReceiver receiver = new UDPReceiver();
        receiver.setPort(RECEIVER_PORT);
        receiver.setSenderAddres(SENDER_IP);
        receiver.setSenderPort(SENDER_PORT);

        receiver.start();
        if (receiver.receive(PATH_RECEIVER))
            System.out.println("File received");
        else
            System.out.println("File not received");
        receiver.stop();
    }
}
