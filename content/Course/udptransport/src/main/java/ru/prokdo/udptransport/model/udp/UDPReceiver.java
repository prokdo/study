package ru.prokdo.udptransport.model.udp;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Objects;

import ru.prokdo.udptransport.controller.log.Logger;
import ru.prokdo.udptransport.model.file.FileChunk;
import ru.prokdo.udptransport.model.file.FileChunkCleaner;
import ru.prokdo.udptransport.model.file.FileUniter;

public final class UDPReceiver {
    private int port = UDPConstants.DEFAULT_RECEIVER_PORT;
    private int senderPort;
    private InetAddress senderAddress;
    private DatagramSocket socket;
    private boolean isRunning;

    private byte[] receiveBuffer = new byte[FileChunk.CHUNK_SIZE + 2 * Integer.BYTES];
    private byte[] sendBuffer = new byte[UDPConstants.DEFAULT_RECEIVER_SEND_BUFFER_SIZE];

    private Logger logger;

    public UDPReceiver(Logger logger) {
        this.logger = Objects.requireNonNull(logger);
    }

    public int getPort() {
        return this.port;
    }

    public int getSenderPort() {
        return this.senderPort;
    }

    public String getSenderAddress() {
        return this.senderAddress.toString();
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSenderPort(int senderPort) {
        this.senderPort = senderPort;
    }

    public void setSenderAddress(String senderAddress) {
        try {
            this.senderAddress = InetAddress.getByName(senderAddress);
        } catch (Exception exception) {
            exception.printStackTrace();

            this.senderAddress = null;
        }
    }

    public boolean start() {
        try {
            this.socket = new DatagramSocket(this.port);
            this.socket.setSoTimeout(UDPConstants.DEFAULT_CONNECTION_TIMEOUT);
        } catch (Exception exception) {
            exception.printStackTrace();

            this.socket = null;

            return false;
        }

        this.isRunning = true;

        return true;
    }

    public boolean stop() {
        if (this.socket == null) {
            return false;
        }

        this.socket.close();

        this.isRunning = false;

        return true;
    }

    private boolean connect() {
        this.sendBuffer = UDPConstants.DEFAULT_CONNECTION_SIGNAL.getBytes();
        DatagramPacket connectPacket = new DatagramPacket(this.sendBuffer, this.sendBuffer.length,
                this.senderAddress, this.senderPort);

        try {
            this.socket.send(connectPacket);
        } catch (Exception exception) {
            exception.printStackTrace();

            return false;
        }

        return true;
    }

    private boolean disconnect() {
        this.receiveBuffer = new byte[UDPConstants.DEFAULT_DISCONNECTION_SIGNAL.getBytes().length];
        DatagramPacket disconnectPacket = new DatagramPacket(this.receiveBuffer, this.receiveBuffer.length);

        try {
            this.socket.receive(disconnectPacket);
        } catch (Exception exception) {
            exception.printStackTrace();

            return false;
        }

        int errors = 0;
        while (!new String(disconnectPacket.getData()).equals(UDPConstants.DEFAULT_DISCONNECTION_SIGNAL)
        // || disconnectPacket.getAddress() != this.senderAddress
        // || disconnectPacket.getPort() != this.senderPort
        ) {
            if (++errors > UDPConstants.DEFAULT_ERRORS_COUNT)
                return false;

            try {
                this.socket.receive(disconnectPacket);
            } catch (Exception exception) {
                exception.printStackTrace();

                return false;
            }
        }

        return true;
    }

    private Object[] receiveInfo() {
        this.receiveBuffer = new byte[FileChunk.CHUNK_SIZE + 2 * Integer.BYTES];

        int hashCode = -1;
        int chunkAmount = -1;
        String fileName = null;
        DatagramPacket namePacket = null;

        int errors = 0;
        while (true) {
            byte[] hashCodeBytes = new byte[Integer.BYTES];
            byte[] chunkAmountBytes = new byte[Integer.BYTES];
            byte[] fileNameLengthBytes = new byte[Integer.BYTES];
            namePacket = new DatagramPacket(this.receiveBuffer, this.receiveBuffer.length);

            try {
                this.socket.receive(namePacket);
            } catch (Exception exception) {
                exception.printStackTrace();

                if (++errors > UDPConstants.DEFAULT_ERRORS_COUNT)
                    return null;
            }

            int pos = 0;
            for (; pos < Integer.BYTES; pos++)
                hashCodeBytes[pos] = namePacket.getData()[pos];

            for (; pos < 2 * Integer.BYTES; pos++)
                chunkAmountBytes[pos - Integer.BYTES] = namePacket.getData()[pos];

            for (; pos < 3 * Integer.BYTES; pos++)
                fileNameLengthBytes[pos - 2 * Integer.BYTES] = namePacket.getData()[pos];

            int fileNameLength = ByteBuffer.wrap(fileNameLengthBytes).getInt();

            byte[] fileNameBytes = new byte[fileNameLength];
            for (; pos < 3 * Integer.BYTES + fileNameLength; pos++)
                fileNameBytes[pos - 3 * Integer.BYTES] = namePacket.getData()[pos];

            hashCode = ByteBuffer.wrap(hashCodeBytes).getInt();
            chunkAmount = ByteBuffer.wrap(chunkAmountBytes).getInt();
            fileName = new String(fileNameBytes);

            if (hashCode != Objects.hash(fileName, chunkAmount)
            // || namePacket.getAddress() != this.senderAddress
            // || namePacket.getPort() != this.senderPort
            ) {
                try {
                    this.socket.send(
                            new DatagramPacket(UDPConstants.DEFAULT_CORRUPT_SIGNAL.getBytes(),
                                    UDPConstants.DEFAULT_CORRUPT_SIGNAL.getBytes().length, this.senderAddress,
                                    this.senderPort));

                    if (++errors > UDPConstants.DEFAULT_ERRORS_COUNT)
                        return null;

                    continue;
                } catch (Exception exception) {
                    exception.printStackTrace();

                    return null;
                }
            }

            break;
        }

        try {
            this.socket.send(
                    new DatagramPacket(UDPConstants.DEFAULT_APPROVE_SIGNAL.getBytes(),
                            UDPConstants.DEFAULT_APPROVE_SIGNAL.getBytes().length,
                            this.senderAddress,
                            this.senderPort));
        } catch (Exception exception) {
            exception.printStackTrace();

            return null;
        }

        return new Object[] { fileName, chunkAmount };
    }

    private FileChunk approveReceive(DatagramPacket receivePacket) {
        // if (receivePacket.getAddress() != this.senderAddress ||
        // receivePacket.getPort() != this.senderPort)
        // return null;

        byte[] hashCodeBytes = new byte[Integer.BYTES];
        byte[] idBytes = new byte[Integer.BYTES];
        byte[] data = new byte[FileChunk.CHUNK_SIZE];

        int pos = 0;
        for (; pos < Integer.BYTES; pos++)
            hashCodeBytes[pos] = this.receiveBuffer[pos];

        for (; pos < 2 * Integer.BYTES; pos++)
            idBytes[pos - Integer.BYTES] = this.receiveBuffer[pos];

        for (; pos < FileChunk.CHUNK_SIZE + 2 * Integer.BYTES; pos++)
            data[pos - 2 * Integer.BYTES] = this.receiveBuffer[pos];

        int hashCode = ByteBuffer.wrap(hashCodeBytes).getInt();
        int id = ByteBuffer.wrap(idBytes).getInt();

        FileChunk chunk = new FileChunk(id, data);

        if (hashCode != chunk.hashCode())
            return null;

        try {
            this.socket.send(
                    new DatagramPacket(UDPConstants.DEFAULT_APPROVE_SIGNAL.getBytes(),
                            UDPConstants.DEFAULT_APPROVE_SIGNAL.getBytes().length, this.senderAddress,
                            this.senderPort));
        } catch (Exception exception) {
            exception.printStackTrace();

            return null;
        }

        return chunk;
    }

    private boolean receiveChunks(String fileName, int chunkAmount) {
        int received = 0;
        int errors = 0;
        while (received < chunkAmount) {
            this.receiveBuffer = new byte[FileChunk.CHUNK_SIZE + 2 * Integer.BYTES];

            DatagramPacket receivePacket = new DatagramPacket(this.receiveBuffer, this.receiveBuffer.length);
            try {
                this.socket.receive(receivePacket);
            } catch (Exception exception) {
                exception.printStackTrace();

                if (++errors > UDPConstants.DEFAULT_ERRORS_COUNT)
                    return false;
            }

            FileChunk chunk = this.approveReceive(receivePacket);
            if (chunk == null) {
                logger.log("Фрагмент №" + received + " поврежден");

                if (++errors > UDPConstants.DEFAULT_ERRORS_COUNT)
                    return false;

                try {
                    this.socket.send(
                            new DatagramPacket(UDPConstants.DEFAULT_CORRUPT_SIGNAL.getBytes(),
                                    UDPConstants.DEFAULT_CORRUPT_SIGNAL.getBytes().length, this.senderAddress,
                                    this.senderPort));
                } catch (Exception exception) {
                    exception.printStackTrace();

                    return false;
                }

            } else {
                logger.log("Фрагмент №" + received + " получен");
                chunk.setFileName(fileName);
                if (!chunk.save())
                    return false;

                received++;
            }
        }

        return true;
    }

    public boolean receive(String path) {
        if (!this.isRunning)
            return false;

        logger.log("Подключение к отправителю...");
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

        logger.log("Успешное подключение к отправителю");

        logger.log("Получение информации о файле...");

        Object[] info = this.receiveInfo();
        if (info == null) {
            this.disconnect();
            return false;
        }

        String fileName = (String) info[0];
        int chunkAmount = (int) info[1];

        logger.log("Получение файла...");

        if (!this.receiveChunks(fileName, chunkAmount)) {
            this.disconnect();
            return false;
        }

        if (!this.disconnect())
            return false;

        if (!FileUniter.unite(path, chunkAmount, fileName))
            return false;

        FileChunkCleaner.clear(chunkAmount, fileName, UDPConstants.DEFAULT_THREAD_POOL_SIZE);

        return true;
    }
}
