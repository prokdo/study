package ru.prokdo.udptransport.model.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public final class FileSplitter {
    private FileSplitter() {
    }

    public static int split(String path, int chunkSize) {
        int count = 0;
        try {
            byte[] buffer = new byte[chunkSize];

            InputStream inputStream = new FileInputStream(path);

            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                OutputStream outputStream = new FileOutputStream(
                        String.format(FileChunk.CHUNK_NAME_TEMPLATE, FileUtils.getName(path), count));

                outputStream.write(buffer, 0, bytesRead);
                outputStream.close();

                count++;
            }

            inputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();

            return -1;
        }

        return count;
    }
}
