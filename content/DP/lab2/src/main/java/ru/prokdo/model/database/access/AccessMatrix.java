package ru.prokdo.model.database.access;

import ru.prokdo.model.database.Entity;
import ru.prokdo.model.database.EntityType;
import ru.prokdo.model.database.operational.util.OperationArgs;


public final class AccessMatrix extends Entity {
    private int usersCount;
    private int filesCount;

    private int[][] accessKeys;

    public AccessMatrix(int usersCount, int filesCount) {
        this.usersCount = usersCount;
        this.filesCount = filesCount;

        this.accessKeys = new int[usersCount][filesCount];

        this.entityType = EntityType.ACCESS_MATRIX;
    }

    public AccessMatrix(int[][] accessKeys) {
        for (var accessKeysRow : accessKeys)
            for (var accessKey : accessKeysRow)
                if (accessKey < 0 || accessKey > 7) throw new IllegalArgumentException("Access key out of bounds [0, 7]");

        this.usersCount = accessKeys.length;
        this.filesCount = accessKeys[0].length;

        this.accessKeys = accessKeys.clone();

        this.entityType = EntityType.ACCESS_MATRIX;
    }

    public int getUserCount() {
        return this.usersCount;
    }

    public int getFilesCount() {
        return this.filesCount;
    }

    public int getAccessKey(int userId, int fileId) {
        return this.accessKeys[userId][fileId];
    }

    public int[] getUserAccessKeys(int userId) {
        return this.accessKeys[userId].clone();
    }

    public int[] getFileAccessKeys(int fileId) {
        int[] fileAccessKeys = new int[this.usersCount];

        for (int i = 0; i < usersCount; i++)
            fileAccessKeys[i] = this.accessKeys[i][fileId];

        return fileAccessKeys;
    }

    public void setUserCount(int userCount) {
        this.usersCount = userCount;
    }

    public void setFilesCount(int filesCount) {
        this.filesCount = filesCount;
    }

    public void setAccessKey(int accessKey, int userId, int fileId) {
        if (accessKey < 0 || accessKey > 7) throw new IllegalArgumentException("Access key out of bounds [0, 7]"); 
        
        this.accessKeys[userId][fileId] = accessKey;
    }

    public void setUserAccessKeys(int[] accessKeys, int userId) {
        if (accessKeys.length != this.filesCount) throw new IllegalArgumentException("Access keys array length does not match files number");

        for (var accessKey : accessKeys)
            if (accessKey < 0 || accessKey > 7) throw new IllegalArgumentException("Access key out of bounds [0, 7]"); 

        this.accessKeys[userId] = accessKeys.clone();
    }

    public void setFileAccessKeys(int[] accessKeys, int fileId) {
        if (accessKeys.length != this.usersCount) throw new IllegalArgumentException("Access keys array length does not match users number");

        for (int i = 0; i < this.usersCount; i++) {
            if (accessKeys[i] < 0 || accessKeys[i] > 7) throw new IllegalArgumentException("Access key out of bounds [0, 7]"); 

            this.accessKeys[i][fileId] = accessKeys[i];
        }
    }

    public boolean checkAccess(OperationArgs operationArgs) {
        AccessKey accessKey = new AccessKey(this.accessKeys[operationArgs.userId()][operationArgs.fileId()]);

        switch (operationArgs.operationType()) {
            case WRITE -> { return accessKey.getWriteBit(); }
            case READ -> { return accessKey.getReadBit(); }
            case DELEGATE -> { return accessKey.getDelegateBit(); }
            default -> { throw new IllegalArgumentException("Unknown operation type"); }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < this.usersCount; i++) {
            for (int j = 0; j < this.filesCount; j++) {
                result.append(this.accessKeys[i][j]);

                if (j != this.filesCount - 1) result.append('\t');
            }

            if (i != this.usersCount - 1) result.append('\n');
        }

        return result.toString();
    }
}
