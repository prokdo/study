package udp;

public final class UDPConstants {
    private UDPConstants() {
    }

    public final static int DEFAULT_SENDER_PORT = 4444;
    public final static int DEFAULT_RECEIVER_PORT = 4445;

    public final static int DEFAULT_CHUNK_SIZE = 1024;

    public final static int DEFAULT_CONNECTION_TIMEOUT = 60000;
    public final static int DEFAULT_PACKET_TIMEOUT = 5000;
    public final static int DEFAULT_ERRORS_COUNT = 1000;

    public final static int DEFAULT_SENDER_RECEIVE_BUFFER_SIZE = 16;
    public final static int DEFAULT_RECEIVER_SEND_BUFFER_SIZE = 16;

    public final static String DEFAULT_CONNECTION_SIGNAL = "CONNECT";
    public final static String DEFAULT_DISCONNETION_SIGNAL = "DISCONNECT";
    public final static String DEFAULT_APPROVE_SIGNAL = "APPROVE";
    public final static String DEFAULT_CORRUPT_SIGNAL = "CORRUPT";
}
