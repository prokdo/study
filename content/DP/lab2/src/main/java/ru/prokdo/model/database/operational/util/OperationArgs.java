package ru.prokdo.model.database.operational.util;

public final record OperationArgs(
    OperationType operationType,  
    int fileId, 
    int userId,
    int otherUserId,
    String inputStr) {
    } 
