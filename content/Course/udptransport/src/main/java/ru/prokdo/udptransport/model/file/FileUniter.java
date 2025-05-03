package ru.prokdo.udptransport.model.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public final class FileUniter {
    private FileUniter() {
    }

    public static boolean unite(String path, int count, String fileName) {
        try {
            byte[] buffer = new byte[FileChunk.CHUNK_SIZE];

            OutputStream outputStream = new FileOutputStream(path + "/" + fileName);

            for (int i = 0; i < count; i++) {
                InputStream inputStream = new FileInputStream(
                        String.format(FileChunk.CHUNK_NAME_TEMPLATE, fileName, i));

                int bytesRead = inputStream.read(buffer);
                outputStream.write(buffer, 0, bytesRead);

                inputStream.close();
            }

            outputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();

            return false;
        }

        return true;
    }
}
