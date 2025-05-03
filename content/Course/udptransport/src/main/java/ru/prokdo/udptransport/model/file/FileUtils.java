package ru.prokdo.udptransport.model.file;

import java.nio.file.Paths;

public final class FileUtils {
    private FileUtils() {
    }

    public static String getName(String path) {
        return Paths.get(path).getFileName().toString();
    }
}
