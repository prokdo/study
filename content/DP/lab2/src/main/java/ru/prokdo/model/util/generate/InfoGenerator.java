package ru.prokdo.model.util.generate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import ru.prokdo.model.database.DataBaseReader;
import ru.prokdo.model.database.DataBaseWriter;
import ru.prokdo.model.database.EntityType;
import ru.prokdo.model.database.access.AccessMatrix;
import ru.prokdo.model.database.operational.File;
import ru.prokdo.model.database.operational.User;
import ru.prokdo.model.util.TaskInfo;


public final class InfoGenerator {
    public final String infoPath;
    public final String resourcePath;

    private final Random RANDOM;

    private final String[] usersName = {
        "Boris", 
        "Dmitry", 
        "Elena", 
        "Oleg", 
        "Alexander", 
        "Ekaterina", 
        "Nadezhda", 
        "Maria", 
        "Valery", 
        "Zoya",
        "Sophia",
        "Vladimir",
        "Natalia",
        "Nikita",
        "Anastasia",
        "Artem",
        "Larisa",
        "Mihail",
        "Diana",
        "Igor"
    };

    public InfoGenerator(String infoPath, String resourcePath) {
        this.infoPath = infoPath;
        this.resourcePath = resourcePath;

        this.RANDOM = new Random();
    }

    public boolean generate() {
        return this.generateUsers() && this.generateFiles() && this.generateAccessMatrix();
    }

    private boolean generateAccessMatrix() {
        DataBaseReader DBReader = new DataBaseReader(infoPath);
        DataBaseWriter DBWriter = new DataBaseWriter(infoPath);

        User[] users;
        File[] files;
        try {
        users = (User[]) DBReader.read(EntityType.USER);
        files = (File[]) DBReader.read(EntityType.FILE);
        }
        catch (Exception exception) { return false; }

        AccessMatrix accessMatrix = new AccessMatrix(users.length, files.length);

        for (int i = 0; i < users.length; i++)
            for (int j = 0; j < files.length; j++) {
                if (users[i].isSuper()) { accessMatrix.setAccessKey(7, i, j); continue; }

                accessMatrix.setAccessKey(RANDOM.nextInt(0, 8), i, j);
            }

        return DBWriter.write(EntityType.ACCESS_MATRIX, accessMatrix);
    }

    private boolean generateFiles() {
        DataBaseWriter DBWriter = new DataBaseWriter(infoPath);

        File[] files = new File[TaskInfo.FILES_NUMBER];
        for (int i = 0; i < TaskInfo.FILES_NUMBER; i++) {
            int id = i;
            String name = String.format("Object%d.txt", i);

            files[i] = new File(id, name);
        }

        for (var file : files) {
            try { 
                if (!Files.exists(Paths.get(resourcePath + "/" + file.getName()))) Files.createFile(Paths.get(resourcePath + "/" + file.getName())); 
                Files.writeString(Paths.get(resourcePath + "/" + file.getName()), String.format("Это содержимое файла под именем %s", file.getName()));
            }
            catch (IOException exception) { return false; }
        }

        return DBWriter.write(EntityType.FILE, files);
    }

    private boolean generateUsers() {
        DataBaseWriter DBWriter = new DataBaseWriter(infoPath);

        User[] users = new User[TaskInfo.USERS_NUMBER];
        boolean isSuperSet = false;
        for (int i = 0; i < TaskInfo.USERS_NUMBER; i++) {
            int id = i;
            String name;
            while (true) {
                name = this.usersName[RANDOM.nextInt(this.usersName.length)];

                boolean isUnique = true;
                for (var user : users) {
                    if (user == null) break;

                    if (user.getName().equals(name)) { isUnique = false; break; }
                }

                if (isUnique) break;
            }
            String password = String.format("%d", i).repeat(3);
            boolean isSuper = false;
            if (!isSuperSet) switch (RANDOM.nextInt(2)) {
                case 0 -> isSuper = false;
                case 1 -> { isSuper = true; isSuperSet = true; }
            }

            users[i] = new User(id, name, password, isSuper);
        }

        return DBWriter.write(EntityType.USER, users);
    }
}
