package model.database.entity

abstract class Entity {
    abstract val entityType: EntityType
    abstract val id: Int
    abstract var name: String
    abstract var accessType: Int

    override fun toString(): String {
        return String.format("%d\n%s\n%d", this.id, this.name, this.accessType)
    }
}
