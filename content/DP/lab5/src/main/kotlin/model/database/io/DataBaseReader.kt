package model.database.io


import java.io.File

import model.database.entity.Entity
import model.database.entity.EntityType
import model.database.entity.User
import model.database.entity.Phrase
import model.database.path.EntityPath


class DataBaseReader(private val path: String) {
    fun readEntity(entityType: EntityType, id: Int): Entity? {
        val entityPath = when (entityType) {
            EntityType.USER -> "${this.path}/${EntityPath.USERS}$id"
            EntityType.PHRASE -> "${this.path}/${EntityPath.PHRASES}$id"
        }

        val entityStringList: List<String>
        try { entityStringList = File(entityPath).readLines() }
        catch (ignored: Exception) { return null }

        try {
            return when (entityType) {
                EntityType.USER -> User(
                                        entityStringList[1],
                                        entityStringList[2].toInt(),
                                        entityStringList[3].toDouble())
                EntityType.PHRASE -> Phrase(entityStringList[0])
            }
        }
        catch (exception: Exception) { return null }
    }
}