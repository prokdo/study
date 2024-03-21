package ru.prokdo.model.database;

import java.io.IOException;

import ru.prokdo.model.database.access.AccessKey;
import ru.prokdo.model.database.access.AccessMatrix;
import ru.prokdo.model.database.operational.File;
import ru.prokdo.model.database.operational.User;
import ru.prokdo.model.database.operational.util.OperationArgs;
import ru.prokdo.model.database.operational.util.OperationType;
import ru.prokdo.model.file.FileReader;
import ru.prokdo.model.file.FileWriter;


public final class DataBase {
    public final String infoPath;
    public final String resourcePath;

    private final DataBaseReader reader;
    private final DataBaseWriter writer;

    private AccessMatrix accessMatrix;
    private File[] files;
    private User[] users;

    public DataBase(String infoPath, String resourcePath) {
        if (infoPath == null || resourcePath == null) throw new NullPointerException("DataBase paths cannot be null");

        this.infoPath = infoPath;
        this.resourcePath = resourcePath;

        this.reader = new DataBaseReader(infoPath);
        this.writer = new DataBaseWriter(infoPath);

        try { this.initialize(); }
        catch (Exception exception) { exception.printStackTrace(); }
    }

    public boolean initialize() throws IOException {
        this.accessMatrix = (AccessMatrix) this.reader.read(EntityType.ACCESS_MATRIX)[0];
        this.files = (File[]) this.reader.read(EntityType.FILE);
        this.users = (User[]) this.reader.read(EntityType.USER);

        return this.accessMatrix != null && this.files != null && this.users != null;
    }

    public int getUserId(String userName) {
        if (userName == null) throw new IllegalArgumentException("User name cannot be null");

        for (var user : this.users)
            if (user.getName().equals(userName)) return user.getId();

        return -1;
    }

    public String getUserName(int userId) {
        for (var user : this.users)
            if (user.getId() == userId) return user.getName();

        return null;
    }

    public User getUserById(int userId) {
        for (var user : this.users)
            if (user.getId() == userId) return user;

        return null;
    }

    public User getUserByName(String userName) {
        if (userName == null) throw new IllegalArgumentException("User name cannot be null");

        for (var user : this.users)
            if (user.getName().equals(userName)) return user;

        return null;
    }

    public int getFileId(String fileName) {
        if (fileName == null) throw new IllegalArgumentException("File name cannot be null");

        for (var file : this.files)
            if (file.getName().equals(fileName)) return file.getId();

        return -1;
    }

    public String getFileName(int fileId) {
        for (var file : this.files)
            if (file.getId() == fileId) return file.getName();

        return null;
    }

    public File getFileById(int fileId) {
        for (var file : this.files)
            if (file.getId() == fileId) return file;

        return null;
    }

    public File getFileByName(String fileName) {
        if (fileName == null) throw new IllegalArgumentException("File name cannot be null");

        for (var file : this.files)
            if (file.getName().equals(fileName)) return file;

        return null;
    }

    public int getUsersNumber() {
        return this.users.length;
    }

    public int getFilesNumber() {
        return this.files.length;
    }
    
    public String evaluate(OperationArgs operationArgs) {
        if (!accessMatrix.checkAccess(operationArgs))
            return "@FAIL@";

        if (operationArgs.operationType() == OperationType.READ) {
            FileReader reader = new FileReader(resourcePath + "/" + this.getFileName(operationArgs.fileId()));
            System.out.println(resourcePath + "/" + this.getFileName(operationArgs.fileId()));
            return reader.read();
        }

        if (operationArgs.operationType() == OperationType.WRITE) {
            FileWriter writer = new FileWriter(resourcePath + "/" + this.getFileName(operationArgs.fileId()));
            if (writer.write(operationArgs.inputStr())) return "@SUCCESS@";
            else return "@FAIL@";
        }

        if (operationArgs.operationType() == OperationType.DELEGATE) {
            AccessKey originKey = new AccessKey(this.accessMatrix.getAccessKey(operationArgs.userId(), operationArgs.fileId()));
            AccessKey destKey = new AccessKey(this.accessMatrix.getAccessKey(operationArgs.otherUserId(), operationArgs.fileId()));

            if (!destKey.getDelegateBit()) destKey.setDelegateBit(originKey.getDelegateBit());
            if (!destKey.getReadBit()) destKey.setReadBit(originKey.getReadBit());
            if (!destKey.getWriteBit()) destKey.setWriteBit(originKey.getWriteBit());
            
            originKey.setDelegateBit(false);
            originKey.setReadBit(false);
            originKey.setWriteBit(false);

            this.accessMatrix.setAccessKey(originKey.toInt(), operationArgs.userId(), operationArgs.fileId());
            this.accessMatrix.setAccessKey(destKey.toInt(), operationArgs.otherUserId(), operationArgs.fileId());

            if (writer.write(EntityType.ACCESS_MATRIX, this.accessMatrix)) return "@SUCCESS@";
            else return "@FAIL@";
        }

        return "@FAIL@";
    }

    public AccessKey[] getUserAccessKeys(String userName) {
        int userId = this.getUserId(userName);

        AccessKey[] result = new AccessKey[this.getFilesNumber()];

        for (int i = 0; i < this.accessMatrix.getUserAccessKeys(userId).length; i++)
            result[i] = new AccessKey(this.accessMatrix.getUserAccessKeys(userId)[i]);

        return result;
    }

    public String[] getFilesNames() {
        String[] result = new String[this.getFilesNumber()];

        for (int i = 0; i < this.getFilesNumber(); i++)
            result[i] = this.files[i].getName();

        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("DataBase {\n");

        result.append(String.format("\tКоличество пользователей (субъектов): %d\n", this.getUsersNumber()));
        result.append(String.format("\tКоличество файлов (объектов): %d\n", this.getFilesNumber()));
        
        result.append('\n');
        
        result.append("\tПользователи:\n ");
        for (var user : this.users) {
            result.append('\t');
            result.append(user.getName());
            result.append('\n');
            result.append('\t');
            result.append(user.isSuper());
            result.append('\n');
        }

        result.append("Файлы:\n ");
        for (var file : this.files) {
            result.append('\t');
            result.append(file.getName());
            result.append('\n');
        }

        result.append('}');

        return result.toString();
    }
}
