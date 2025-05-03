package ru.prokdo.udptransport.model.file;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class FileChunkCleaner {

    private FileChunkCleaner() {
    }

    // Метод для многопоточного удаления файлов-чанков
    public static boolean clear(int chunkCount, String fileName, int threadCount) {
        // Создаем ExecutorService с фиксированным количеством потоков
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // Флаг успешного удаления всех файлов
        final boolean[] success = {true};

        // Запускаем задачи для удаления файлов-чанков
        for (int i = 0; i < chunkCount; i++) {
            final int chunkIndex = i;

            executor.submit(() -> {
                // Удаление чанка
                boolean deleted = deleteChunk(fileName, chunkIndex);
                if (!deleted) {
                    synchronized (success) {
                        success[0] = false;
                    }
                }
            });
        }

        // Ожидание завершения всех задач
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        return success[0];
    }

    // Метод для удаления одного чанка
    private static boolean deleteChunk(String fileName, int chunkIndex) {
        File chunkFile = new File(String.format(FileChunk.CHUNK_NAME_TEMPLATE, fileName, chunkIndex));
        return chunkFile.delete();
    }
}
