package model.database.io


import java.io.File

import model.database.entity.Entity
import model.database.entity.EntityType
import model.database.path.EntityPath


class DataBaseWriter(private val path: String) {
    fun writeEntity(entity: Entity): Boolean {
        val entityPath = when (entity.type) {
            EntityType.USER -> "${this.path}/${EntityPath.USERS}${entity.id}"
            EntityType.PHRASE -> "${this.path}/${EntityPath.PHRASES}${entity.id}"
        }

        try { File(entityPath).writeText(entity.toString()) }
        catch (ignored: Exception) { return false }

        return true
    }

    fun deleteEntity(entity: Entity): Boolean {
        val entityPath = when (entity.type) {
            EntityType.USER -> "${this.path}/${EntityPath.USERS}${entity.id}"
            EntityType.PHRASE -> "${this.path}/${EntityPath.PHRASES}${entity.id}"
        }

        try { File(entityPath).delete() }
        catch (ignored: Exception) { return false }

        return true
    }
}