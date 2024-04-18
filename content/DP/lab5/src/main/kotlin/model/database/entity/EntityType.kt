package model.database.entity


import model.database.path.EntityPath


enum class EntityType {
    USER,
    PHRASE;

    fun toEntityPath(): String {
        return when (this) {
            USER -> EntityPath.USERS
            PHRASE -> EntityPath.PHRASES
        }
    }
}