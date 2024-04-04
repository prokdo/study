package model.database.operation

import model.database.resource.ResourceType

data class OperationArgs(
    val operationType: OperationType,
    val userId: Int,
    val resourceType: ResourceType,
    val resourceId: Int)

