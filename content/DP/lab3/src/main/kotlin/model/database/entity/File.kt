package model.database.entity

import model.database.access.AccessType

class File(name: String, accessType: Int) : Entity() {
    override val entityType: EntityType
    override val id: Int
    override var name: String = ""
        set(value) {
            if (value.isEmpty() || value.isBlank()) throw IllegalArgumentException("Entity name cannot be empty or blank")

            field = value
        }
    override var accessType: Int = -1
        set(value) {
            if (!AccessType.ACCESS_RANGE.contains(value)) throw IllegalArgumentException("Unknown access type")

            field = value
        }

    init {
        if (name.isEmpty() || name.isBlank()) throw IllegalArgumentException("Entity name cannot be empty or blank")

        this.entityType = EntityType.FILE
        this.id = name.hashCode()
        this.name = name
        this.accessType = accessType
    }
}
