package model.database.resource

import model.database.entity.EntityType

enum class ResourceType {
    FILE;

    fun toEntityType(): EntityType = when (this) {
        FILE -> EntityType.FILE
    }
}