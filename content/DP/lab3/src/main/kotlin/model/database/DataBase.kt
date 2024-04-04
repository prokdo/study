package model.database

import kotlin.io.path.exists
import kotlin.io.path.Path

import java.io.File

import model.database.entity.Entity
import model.database.entity.EntityType
import model.database.io.DataBaseReader
import model.database.io.DataBaseWriter
import model.database.operation.OperationType
import model.database.operation.OperationArgs
import model.database.path.EntityPath
import model.database.resource.ResourceType
import kotlin.io.path.createDirectory

class DataBase(private val dataBasePath: String, private val resourcePath: String) {
    private val reader: DataBaseReader = DataBaseReader(this.dataBasePath)
    private val writer: DataBaseWriter = DataBaseWriter(this.dataBasePath)

    private var _usersNumber: Int
    private var _filesNumber: Int

    val filesNumber: Int
        get() = this._filesNumber

    val usersNumber: Int
        get() = this._usersNumber

    init {
        if (this.dataBasePath.isEmpty() || this.dataBasePath.isBlank() ||
            this.resourcePath.isEmpty() || this.dataBasePath.isBlank())
            throw IllegalArgumentException("Database paths cannot be empty or blank")

        if (!Path(this.dataBasePath).exists() || !Path(this.resourcePath).exists())
            throw IllegalArgumentException("At least one of the database paths does not exist")

        if (File(this.dataBasePath).listFiles() == null) {
            Path("${this.dataBasePath}/${EntityPath.FILES}").createDirectory()
            Path("${this.dataBasePath}/${EntityPath.USERS}").createDirectory()
        }

        try { this._filesNumber = File(String.format("%s/%s", this.resourcePath, EntityPath.FILES)).listFiles().size }
        catch (ignored: NullPointerException) { this._filesNumber = 0 }

        try { this._usersNumber = File(String.format("%s/%s", this.resourcePath, EntityPath.USERS)).listFiles().size }
        catch (ignored: NullPointerException) { this._usersNumber = 0 }
    }

    fun addEntity(entity: Entity): Boolean {
        if (getEntityById(entity.entityType, entity.id) != null) return false

        if (!writer.writeEntity(entity)) return false

        when(entity.entityType) {
            EntityType.FILE -> {
                try { File(String.format("%s/%s", this.resourcePath, entity.name)).createNewFile() }
                catch (ignored: Exception) { writer.deleteEntity(entity); return false }

                this._filesNumber++
            }
            EntityType.USER -> this._usersNumber++
        }

        return true
    }

    fun deleteEntity(entity: Entity): Boolean {
        if (getEntityById(entity.entityType, entity.id) != null) return false

       if (!writer.deleteEntity(entity)) return false

        when(entity.entityType) {
            EntityType.FILE -> {
                try { File(String.format("%s/%s", this.resourcePath, entity.name)).delete() }
                catch (ignored: Exception) { writer.writeEntity(entity); return false }

                this._filesNumber--
            }
            EntityType.USER -> this._usersNumber--
        }

        return true
    }

    fun checkAccess(operationArgs: OperationArgs): Boolean {
        val user = this.getEntityById(EntityType.USER, operationArgs.userId)
        val file = this.getEntityById(EntityType.FILE, operationArgs.resourceId)

        if (user == null || file == null) return false

        if (user.accessType == file.accessType) return true

        return when (operationArgs.operationType) {
            OperationType.READ -> file.accessType < user.accessType
            OperationType.WRITE -> user.accessType < file.accessType
        }
    }

    fun checkEntityExist(entityType: EntityType, id: Int): Boolean {
        val entityPath = when (entityType) {
            EntityType.FILE -> String.format("%s/%s%s", this.dataBasePath, EntityPath.FILES, id)
            EntityType.USER -> String.format("%s/%s%s", this.dataBasePath, EntityPath.USERS, id)
        }

        return Path(entityPath).exists()
    }

    fun getResourceForEvaluation(operationArgs: OperationArgs): Any? {
        if (!checkAccess(operationArgs)) return null

        return when(operationArgs.resourceType) {
            ResourceType.FILE -> getResourceById(operationArgs.resourceType, operationArgs.resourceId)
        }
    }

    fun getEntityById(entityType: EntityType, id: Int): Entity? {
        if (!this.checkEntityExist(entityType, id)) return null

        return reader.readEntity(entityType, id)
    }

    fun getEntityByName(entityType: EntityType, name: String): Entity? {
        if (name.isEmpty() || name.isBlank()) throw IllegalArgumentException("Entity name cannot be empty or blank")

        if (!this.checkEntityExist(entityType, name.hashCode())) return null

        return reader.readEntity(entityType, name.hashCode())
    }

    fun getResourceById(resourceType: ResourceType, id: Int): Any? {
        if (!this.checkEntityExist(resourceType.toEntityType(), id)) return null

        return when (resourceType.toEntityType()) {
            EntityType.FILE -> try { File(String.format("%s/%s%s", this.resourcePath, EntityPath.FILES, id.toString())) }
                                catch (ignored: Exception) { return null }
            EntityType.USER -> getEntityById(resourceType.toEntityType(), id)
        }
    }

    fun getResourceByName(resourceType: ResourceType, name: String): Any? {
        if (!this.checkEntityExist(resourceType.toEntityType(), name.hashCode())) return null

        return when (resourceType.toEntityType()) {
            EntityType.FILE -> try { File(String.format("%s/%s%s", this.resourcePath, EntityPath.FILES, name.hashCode().toString())) }
                                catch (ignored: Exception) { return null }
            EntityType.USER -> getEntityById(resourceType.toEntityType(), name.hashCode())
        }
    }
}