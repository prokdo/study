package ru.prokdo.model.database;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import ru.prokdo.model.database.access.AccessMatrix;
import ru.prokdo.model.database.operational.File;
import ru.prokdo.model.database.operational.User;


public final class DataBaseWriter {
    private final String infoPath;

    public DataBaseWriter(String infoPath) {
        this.infoPath = infoPath;
    }

    public boolean write(EntityType entityType, Entity entity) {
        if (entityType != EntityType.ACCESS_MATRIX) return false;

        return this.writeAccessMatrix((AccessMatrix) entity);
    }

    public boolean write(EntityType entityType, Entity[] entities) {
        switch (entityType) {
            case FILE -> { return this.writeFiles((File[]) entities); }
            case USER -> { return this.writeUsers((User[]) entities); }
            case ACCESS_MATRIX -> { throw new IllegalArgumentException("Cannot write array of access matrixes"); }
            default -> { throw new IllegalArgumentException("Unknown entity type"); }
        }
    }

    private boolean writeAccessMatrix(AccessMatrix accessMatrix) {
        try {
            BufferedWriter writer =  Files.newBufferedWriter(Paths.get(this.infoPath + "/access"));

            writer.write(String.format("%d %d", accessMatrix.getUserCount(), accessMatrix.getFilesCount()));
            writer.newLine();
            writer.newLine();

            writer.write(accessMatrix.toString());

            writer.close();

            return true;
        }
        catch (Exception exception) { exception.printStackTrace(); return false; }
    }

    private boolean writeFiles(File[] files) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.infoPath + "/files"));

            writer.write(String.format("%d", files.length));
            writer.newLine();
            writer.newLine();
            
            for (int i = 0; i < files.length; i++) {
                writer.write(String.format("%d", files[i].getId()));
                writer.newLine();

                writer.write(files[i].getName());
                writer.newLine();

                if (i != files.length - 1) writer.newLine();
            }

            writer.close();

            return true;
        }
        catch (Exception exception) { exception.printStackTrace(); return false; }
    }

    private boolean writeUsers(User[] users) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.infoPath + "/users"));

            writer.write(String.format("%d", users.length));
            writer.newLine();
            writer.newLine();
            
            for (int i = 0; i < users.length; i++) {
                writer.write(String.format("%d", users[i].getId()));
                writer.newLine();
                
                writer.write(users[i].getName());
                writer.newLine();

                writer.write(users[i].getPassword());
                writer.newLine();

                writer.write(Boolean.toString(users[i].isSuper()));
                writer.newLine();

                if (i != users.length - 1) writer.newLine();
            }

            writer.close();

            return true;
        }
        catch (Exception exception) { exception.printStackTrace(); return false; }
    }
}
