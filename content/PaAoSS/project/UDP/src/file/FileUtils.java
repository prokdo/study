package file;

import java.io.File;

public final class FileUtils {
    private FileUtils() {
    }

    public static String getName(String path) {
        return new File(path).getName();
    }
}
