package ru.prokdo.model.file;

import java.nio.file.Paths;
import java.io.BufferedReader;
import java.nio.file.Files;


public final class FileReader {
    public final String filePath;

    public FileReader(String filePath) {
        if (filePath == null) throw new IllegalArgumentException("File path cannot be null");
        
        this.filePath = filePath;
    }

    public String read() {
        try { 
            BufferedReader reader = Files.newBufferedReader(Paths.get(filePath)); 

            String result = reader.readLine();

            reader.close();

            return result;
        }
        catch (Exception exception) { return null; }
    }
}
