package model.database.io


import java.io.File

import model.database.entity.Entity
import model.database.entity.EntityType
import model.database.entity.User
import model.database.entity.Phrase
import model.database.path.EntityPath


class DataBaseWriter(private val path: String) {
    fun writeEntity(entity: Entity): Boolean {
        val entityPath = "${this.path}/${entity.type.toEntityPath()}${entity.id}"

        try { 
            when (entity.type) {
                EntityType.USER -> { 
                    val user = entity as User

                    File(entityPath).writeText("${user.name}\n")
                    File(entityPath).appendText("${user.phraseId}\n")
                    File(entityPath).appendText("${user.accessValue}")
                }

                EntityType.PHRASE -> {
                    val phrase = entity as Phrase

                    File(entityPath).writeText("${phrase.content}")
                }
            }
        }
        catch (ignored: Exception) { return false }

        return true
    }

    fun deleteEntity(entity: Entity): Boolean {
        val entityPath = "${this.path}/${entity.type.toEntityPath()}${entity.id}"

        try { File(entityPath).delete() }
        catch (ignored: Exception) { return false }

        return true
    }
}