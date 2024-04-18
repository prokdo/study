package model.database


import java.io.File

import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.io.path.Path

import model.database.entity.Entity
import model.database.entity.EntityType
import model.database.io.DataBaseReader
import model.database.io.DataBaseWriter
import model.database.path.EntityPath


class DataBase(private val dataBasePath: String) {
    private val reader: DataBaseReader = DataBaseReader(this.dataBasePath)
    private val writer: DataBaseWriter = DataBaseWriter(this.dataBasePath)

    var usersNumber: Int
        private set

    var phrasesNumber: Int
        private set

    init {
        if (this.dataBasePath.isEmpty() || this.dataBasePath.isBlank())
            throw IllegalArgumentException("Database paths cannot be empty or blank")

        if (!Path(this.dataBasePath).exists())
            throw IllegalArgumentException("At least one of the database paths does not exist")

        if (File(this.dataBasePath).listFiles() == null) 
            Path("${this.dataBasePath}/${EntityPath.USERS}").createDirectory()

        try { this.usersNumber = File("${this.dataBasePath}/${EntityPath.USERS}").listFiles().size }
        catch (ignored: NullPointerException) { this.usersNumber = 0 }

        try { this.phrasesNumber = File("${this.dataBasePath}/${EntityPath.PHRASES}").listFiles().size }
        catch (ignored: NullPointerException) { this.phrasesNumber = 0 }
    }

    fun addEntity(entity: Entity): Boolean {
        if (checkEntityExist(entity.type, entity.id)) return false

        if (!writer.writeEntity(entity)) return false

        when(entity.type) {
            EntityType.USER -> this.usersNumber++
            EntityType.PHRASE -> this.phrasesNumber++
        }

        return true
    }

    fun deleteEntity(entity: Entity): Boolean {
        if (getEntityById(entity.type, entity.id) != null) return false

       if (!writer.deleteEntity(entity)) return false

        when(entity.type) {
            EntityType.USER -> this.usersNumber--
            EntityType.PHRASE -> this.phrasesNumber--
        }

        return true
    }

    private fun checkEntityExist(entityType: EntityType, id: Int): Boolean {
        return Path("${this.dataBasePath}/${entityType.toEntityPath()}$id").exists()
    }

    fun getEntityById(entityType: EntityType, id: Int): Entity? {
        if (!this.checkEntityExist(entityType, id)) return null

        return reader.readEntity(entityType, id)
    }

    fun getEntityByName(entityType: EntityType, name: String): Entity? {
        return when (entityType) {
            EntityType.USER -> getEntityById(entityType, name.hashCode())
            EntityType.PHRASE -> null
        }
    }

    fun getRandomEntity(entityType: EntityType): Entity? {
        try {
            val id = File("${this.dataBasePath}/${entityType.toEntityPath()}").listFiles().random().name.toInt()

            return this.getEntityById(entityType, id)
        }
        catch (exception: Exception) { return null }
    }
}