package ru.prokdo.udptransport.model.file;

import java.util.Arrays;
import java.util.Objects;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

public final class FileChunk {
    private String fileName;
    private Integer id;
    private byte[] data = new byte[CHUNK_SIZE];

    private boolean isFileNameSet = false;
    private boolean isIdSet = false;
    private boolean isDataSet = false;

    public static int CHUNK_SIZE = 1024;
    public static String CHUNK_NAME_TEMPLATE = "out/temp/%s_chunk_%d";

    public FileChunk(String fileName, int id) {
        this.fileName = Objects.requireNonNull(fileName);
        this.id = id;

        this.isFileNameSet = true;
        this.isIdSet = true;
    }

    public FileChunk(int id, byte[] data) {
        this.id = id;
        this.data = Objects.requireNonNull(data);

        this.isIdSet = true;
        this.isDataSet = true;
    }

    public FileChunk(String fileName, int id, byte[] data) {
        this.fileName = Objects.requireNonNull(fileName);
        this.id = id;
        this.data = Objects.requireNonNull(data);

        this.isFileNameSet = true;
        this.isIdSet = true;
        this.isDataSet = true;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        if (this.isDataSet)
            throw new IllegalStateException("Data can be set only once");

        this.data = Objects.requireNonNull(data);
        this.isDataSet = true;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        if (this.isFileNameSet)
            throw new IllegalStateException("File name can be set only once");

        this.fileName = Objects.requireNonNull(fileName);
        this.isFileNameSet = true;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(int id) {
        if (this.isIdSet)
            throw new IllegalStateException("Id can be set only once");

        this.id = id;
        this.isIdSet = true;
    }

    public boolean read() {
        try {
            InputStream inputStream = new FileInputStream(
                    String.format(CHUNK_NAME_TEMPLATE, this.fileName, this.id));
            if (inputStream.read(this.data) == -1) {
                inputStream.close();

                return false;
            }

            inputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();

            return false;
        }

        this.isDataSet = true;
        return true;
    }

    public boolean save() {
        try {
            OutputStream outputStream = new FileOutputStream(
                    String.format(CHUNK_NAME_TEMPLATE, this.fileName, this.id));

            outputStream.write(this.data);
            outputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();

            return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FileChunk) {
            FileChunk other = (FileChunk) obj;
            return this.id.equals(other.id) && this.data.equals(other.data);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.data);
    }

    @Override
    public String toString() {
        return "[id: " + this.id + ", data: " + this.data + "]";
    }
}
