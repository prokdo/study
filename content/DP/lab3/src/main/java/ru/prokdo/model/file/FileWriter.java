package ru.prokdo.model.file;

import java.nio.file.Paths;
import java.io.BufferedWriter;
import java.nio.file.Files;


public final class FileWriter {
    public final String filePath;

    public FileWriter(String filePath) {
        if (filePath == null) throw new IllegalArgumentException("File path cannot be null");
        
        this.filePath = filePath;
    }

    public boolean write(String str) {
        if (str == null || str.isBlank() || str.isEmpty()) return false;

        try { 
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath)); 

            writer.write(str);

            writer.close();

            return true;
        }
        catch (Exception exception) { return false; }
    }
}
