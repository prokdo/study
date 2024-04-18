package model.database.entity

abstract class Entity {
    abstract val type: EntityType
    
    val id: Int

    constructor(id: Int) {
        this.id = id
    }

    override fun toString(): String {
        return "${this.type}:\n\tID: ${this.id}"
    }
}
