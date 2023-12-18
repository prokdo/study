package file;

import java.io.File;

public final class FileChunkCleaner {
    private FileChunkCleaner() {
    }

    public static boolean clear(int count, String fileName) {
        for (int i = 0; i < count; i++) {
            if (!new File(String.format(FileChunk.CHUNK_NAME_TEMPLATE, fileName, i)).delete()) {
                return false;
            }
        }

        return true;
    }
}
