package model.database.io

import java.io.File

import model.database.entity.Entity
import model.database.entity.EntityType
import model.database.path.EntityPath

class DataBaseWriter(private val path: String) {
    fun writeEntity(entity: Entity): Boolean {
        val entityPath = when (entity.entityType) {
            EntityType.FILE -> String.format("%s/%s%s", this.path, EntityPath.FILES, entity.id.toString())
            EntityType.USER -> String.format("%s/%s%s", this.path, EntityPath.USERS, entity.id.toString())
        }

        try { File(entityPath).writeText(entity.toString()) }
        catch (ignored: Exception) { return false }

        return true
    }

    fun deleteEntity(entity: Entity): Boolean {
        val entityPath = when (entity.entityType) {
            EntityType.FILE -> String.format("%s/%s%s", this.path, EntityPath.FILES, entity.id.toString())
            EntityType.USER -> String.format("%s/%s%s", this.path, EntityPath.USERS, entity.id.toString())
        }

        try { File(entityPath).delete() }
        catch (ignored: Exception) { return false }

        return true
    }
}