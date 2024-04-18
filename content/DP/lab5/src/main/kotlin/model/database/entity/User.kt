package model.database.entity


class User(
            val name: String, 
            val phraseId: Int, 
            val accessValue: Double) : Entity(name.hashCode()) {
    
    override val type: EntityType

    init { 
        if (name.isBlank() || name.isEmpty()) throw IllegalArgumentException("")
        
        this.type = EntityType.USER 
    }

    override fun toString(): String {
        return "${super.toString()}\n\tName: ${this.name}\n\tUser secret phrase ID: ${this.phraseId}\n\tUser access value: ${this.accessValue}"
    }
}