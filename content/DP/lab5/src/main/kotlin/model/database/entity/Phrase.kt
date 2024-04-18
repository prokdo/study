package model.database.entity


class Phrase(val content: String) : Entity(content.hashCode()) {
    override val type: EntityType

    init { this.type = EntityType.PHRASE }

    override fun toString(): String {
        return "${super.toString()}\n\tContent: ${this.content}"
    }
}