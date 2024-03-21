package ru.prokdo.model.database;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import ru.prokdo.model.database.access.AccessMatrix;
import ru.prokdo.model.database.operational.File;
import ru.prokdo.model.database.operational.User;


public class DataBaseReader {
    private final String infoPath;

    public DataBaseReader(String infoPath) {
        this.infoPath = infoPath;
    }

    public Entity[] read(EntityType entityType) {
        switch(entityType) {
            case ACCESS_MATRIX -> { return this.readAccessMatrix(); }
            case FILE -> { return this.readFiles(); }
            case USER -> { return this.readUsers(); }
            default -> { throw new IllegalArgumentException("Unknown entity type"); }
        }
    }

    private AccessMatrix[] readAccessMatrix() {
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get(this.infoPath + "/access"));

            String[] matrixInfoStr = reader.readLine().split(" ");
            int[][] accessKeys = new int[Integer.parseInt(matrixInfoStr[0])][Integer.parseInt(matrixInfoStr[1])];

            reader.readLine();

            for (int i = 0; i < Integer.parseInt(matrixInfoStr[0]); i++) {
                String fileLine = reader.readLine();
                
                String[] accessKeysStr = fileLine.split("\t");
                for (int j = 0; j < accessKeysStr.length; j++)
                    accessKeys[i][j] = Integer.parseInt(accessKeysStr[j]);
            }

            reader.close();

            AccessMatrix[] result = {new AccessMatrix(accessKeys)};

            return result;
        }
        catch (Exception exception) { exception.printStackTrace(); return null; }
    }

    private User[] readUsers() {
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get(this.infoPath + "/users"));

            int usersNumber = Integer.parseInt(reader.readLine());
            reader.readLine();

            User[] users = new User[usersNumber];
            for (int i = 0; i < usersNumber; i++) {
                int userId = Integer.parseInt(reader.readLine());
                String userName = reader.readLine();
                String userPassword = reader.readLine();
                boolean userIsSuper = Boolean.parseBoolean(reader.readLine());

                users[i] = new User(userId, userName, userPassword, userIsSuper);

                if (i != usersNumber - 1) reader.readLine();
            }

            reader.close();

            return users;
        }
        catch (Exception exception) { exception.printStackTrace(); return null; }
    }

    private File[] readFiles() {
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get(this.infoPath + "/files"));

            int filesNumber = Integer.parseInt(reader.readLine());
            reader.readLine();

            File[] files = new File[filesNumber];
            for (int i = 0; i < filesNumber; i++) {
                int fileId = Integer.parseInt(reader.readLine());
                String fileName = reader.readLine();

                files[i] = new File(fileId, fileName);

                if (i != filesNumber - 1) reader.readLine();
            }

            reader.close();

            return files;
        }
        catch (Exception exception) { exception.printStackTrace(); return null; }
    }
}
