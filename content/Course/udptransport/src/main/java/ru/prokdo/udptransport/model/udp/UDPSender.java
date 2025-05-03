package ru.prokdo.udptransport.model.udp;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Objects;

import ru.prokdo.udptransport.controller.log.Logger;
import ru.prokdo.udptransport.model.file.FileChunk;
import ru.prokdo.udptransport.model.file.FileChunkCleaner;
import ru.prokdo.udptransport.model.file.FileSplitter;
import ru.prokdo.udptransport.model.file.FileUtils;


public final class UDPSender {
    private int port = UDPConstants.DEFAULT_SENDER_PORT;
    private int receiverPort;
    private InetAddress receiverAddress;
    private DatagramSocket socket;
    private boolean isRunning;

    private byte[] sendBuffer = new byte[FileChunk.CHUNK_SIZE + 2 * Integer.BYTES];
    private byte[] receiveBuffer = new byte[UDPConstants.DEFAULT_SENDER_RECEIVE_BUFFER_SIZE];

    private Logger logger;

    public UDPSender(Logger logger) {
        this.logger = Objects.requireNonNull(logger);
    }

    public int getPort() {
        return this.port;
    }

    public int getReceiverPort() {
        return this.receiverPort;
    }

    public String getReceiverAddress() {
        return this.receiverAddress.toString();
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setReceiverPort(int receiverPort) {
        this.receiverPort = receiverPort;
    }

    public void setReceiverAddress(String receiverAddress) {
        try {
            this.receiverAddress = InetAddress.getByName(receiverAddress);
        } catch (Exception exception) {
            exception.printStackTrace();

            this.receiverAddress = null;
        }
    }

    public boolean start() {
        try {
            this.socket = new DatagramSocket(port);
            this.socket.setSoTimeout(UDPConstants.DEFAULT_CONNECTION_TIMEOUT);
        }
        catch (Exception exception) {
            exception.printStackTrace();

            this.socket = null;

            return false;
        }

        if (this.receiverAddress == null || this.receiverPort == 0) return false;

        this.isRunning = true;

        return true;
    }

    public boolean stop() {
        if (this.socket == null) return false;

        this.socket.close();

        this.isRunning = false;

        return true;
    }

    private boolean connect() {
        this.receiveBuffer = new byte[UDPConstants.DEFAULT_CONNECTION_SIGNAL.getBytes().length];
        DatagramPacket connectPacket = null;

        int errors = 0;
        do {
            try {
                connectPacket = new DatagramPacket(this.receiveBuffer, this.receiveBuffer.length);
                this.socket.receive(connectPacket);

                if (++errors > UDPConstants.DEFAULT_ERRORS_COUNT)
                    return false;
            } catch (Exception exception) {
                if (++errors > UDPConstants.DEFAULT_ERRORS_COUNT) {
                    exception.printStackTrace();
                    return false;
                }
            }
        } while (!new String(connectPacket.getData()).equals(UDPConstants.DEFAULT_CONNECTION_SIGNAL)
        // || this.receiverAddress != connectPacket.getAddress()
        // || this.receiverPort != connectPacket.getPort()
        );

        return true;
    }

    private boolean disconnect() {
        this.sendBuffer = UDPConstants.DEFAULT_DISCONNECTION_SIGNAL.getBytes();
        DatagramPacket disconnectPacket = new DatagramPacket(this.sendBuffer, this.sendBuffer.length,
                this.receiverAddress, this.receiverPort);

        try {
            this.socket.send(disconnectPacket);
        } catch (Exception exception) {
            exception.printStackTrace();

            return false;
        }

        return true;
    }

    private void fillSendBuffer(FileChunk chunk) {
        byte[] hashBytes = ByteBuffer.allocate(Integer.BYTES).putInt(chunk.hashCode()).array();
        byte[] idBytes = ByteBuffer.allocate(Integer.BYTES).putInt(chunk.getId()).array();
        int pos = 0;
        for (; pos < Integer.BYTES; pos++)
            this.sendBuffer[pos] = hashBytes[pos];

        for (; pos < 2 * Integer.BYTES; pos++)
            this.sendBuffer[pos] = idBytes[pos - Integer.BYTES];

        for (; pos < FileChunk.CHUNK_SIZE + 2 * Integer.BYTES; pos++)
            this.sendBuffer[pos] = chunk.getData()[pos - 2 * Integer.BYTES];
    }

    private boolean sendInfo(String fileName, int chunkAmount) {
        this.sendBuffer = new byte[Integer.BYTES * 3 + fileName.getBytes().length];

        byte[] fileInfoHashBytes = ByteBuffer.allocate(Integer.BYTES).putInt(Objects.hash(fileName, chunkAmount))
                .array();
        byte[] chunkAmountBytes = ByteBuffer.allocate(Integer.BYTES).putInt(chunkAmount).array();
        byte[] fileNameLength = ByteBuffer.allocate(Integer.BYTES).putInt(fileName.getBytes().length).array();
        byte[] fileNameBytes = fileName.getBytes();
        int pos = 0;
        for (; pos < Integer.BYTES; pos++)
            this.sendBuffer[pos] = fileInfoHashBytes[pos];

        for (; pos < 2 * Integer.BYTES; pos++)
            this.sendBuffer[pos] = chunkAmountBytes[pos - Integer.BYTES];

        for (; pos < 3 * Integer.BYTES; pos++)
            this.sendBuffer[pos] = fileNameLength[pos - 2 * Integer.BYTES];

        for (; pos < fileNameBytes.length + 3 * Integer.BYTES; pos++)
            this.sendBuffer[pos] = fileNameBytes[pos - 3 * Integer.BYTES];

        DatagramPacket sendPacket = new DatagramPacket(this.sendBuffer, this.sendBuffer.length,
                this.receiverAddress, this.receiverPort);
        int errors = 0;
        try {
            this.socket.send(sendPacket);
            while (!approveSend())
                if (++errors > UDPConstants.DEFAULT_ERRORS_COUNT)
                    return false;
                else
                    this.socket.send(sendPacket);
        } catch (Exception exception) {
            exception.printStackTrace();

            return false;
        }

        return true;
    }

    private boolean approveSend() {
        this.receiveBuffer = new byte[UDPConstants.DEFAULT_APPROVE_SIGNAL.getBytes().length];
        try {
            DatagramPacket receivePacket = new DatagramPacket(this.receiveBuffer, this.receiveBuffer.length);
            this.socket.receive(receivePacket);

            if (!new String(receivePacket.getData()).equals(UDPConstants.DEFAULT_APPROVE_SIGNAL)
            // || this.receiverAddress != receivePacket.getAddress()
            // || this.receiverPort != receivePacket.getPort()
            )
                return false;
        } catch (Exception exception) {
            exception.printStackTrace();

            return false;
        }

        this.sendBuffer = new byte[FileChunk.CHUNK_SIZE + 2 * Integer.BYTES];
        return true;
    }

    private boolean sendChunks(String path, int chunkAmount) {
        int sended = 0;
        int errors = 0;
        while (sended < chunkAmount) {
            if (errors > UDPConstants.DEFAULT_ERRORS_COUNT) {
                return false;
            }

            FileChunk chunk = new FileChunk(FileUtils.getName(path), sended);
            if (!chunk.read())
                return false;

                this.fillSendBuffer(chunk);

                logger.log("Отправление фрагмента №" + sended + "...");

                DatagramPacket sendPacket = new DatagramPacket(this.sendBuffer, this.sendBuffer.length,
                        this.receiverAddress, this.receiverPort);

                try {
                    this.socket.send(sendPacket);
                } catch (Exception exception) {
                    exception.printStackTrace();

                return false;
            }

            if (this.approveSend())
                sended++;
            else {
                logger.log("Фрагмент " + sended + " не прошел проверку");
                errors++;
            }

            this.sendBuffer = new byte[FileChunk.CHUNK_SIZE + 2 * Integer.BYTES];
        }

        return true;
    }

    public boolean send(String path) {
        if (!this.isRunning)
            return false;

        int chunkAmount = FileSplitter.split(path, FileChunk.CHUNK_SIZE);
        if (chunkAmount == -1)
            return false;

        logger.log("Ожидание подключения получателя...");
        if (!this.connect()) {
            this.disconnect();
            return false;
        }

        try {
            this.socket.setSoTimeout(UDPConstants.DEFAULT_PACKET_TIMEOUT);
        } catch (Exception exception) {
            exception.printStackTrace();

            return false;
        }

        logger.log("Получатель подключен");
        logger.log("Отправление информации о файле...");

        if (!this.sendInfo(FileUtils.getName(path), chunkAmount)) {
            this.disconnect();
            return false;
        }

        if (!this.sendChunks(path, chunkAmount)) {
            this.disconnect();
            return false;
        }

        logger.log("Отправление завершено");

        if (!this.disconnect())
            return false;

        logger.log("Соединение завершено");

        FileChunkCleaner.clear(chunkAmount, FileUtils.getName(path), UDPConstants.DEFAULT_THREAD_POOL_SIZE);

        return true;
    }
}
