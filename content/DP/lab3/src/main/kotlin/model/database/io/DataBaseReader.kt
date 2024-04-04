package model.database.io

import java.io.File

import model.database.entity.Entity
import model.database.entity.EntityType
import model.database.entity.User
import model.database.path.EntityPath

class DataBaseReader(private val path: String) {
    fun readEntity(entityType: EntityType, id: Int): Entity? {
        val entityPath = when (entityType) {
            EntityType.FILE -> String.format("%s/%s%s", path, EntityPath.FILES, id)
            EntityType.USER -> String.format("%s/%s%s", path, EntityPath.USERS, id)
        }

        val entityStringList: List<String>
        try { entityStringList = File(entityPath).readLines() }
        catch (ignored: Exception) { return null }

        if (entityStringList.size != 3) return null

        return when (entityType) {
            EntityType.FILE -> model.database.entity.File(
                entityStringList[1],
                entityStringList[2].toInt())
            EntityType.USER -> User(
                entityStringList[1],
                entityStringList[2].toInt())
        }
    }
}