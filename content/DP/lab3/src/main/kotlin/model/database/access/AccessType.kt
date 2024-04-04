package model.database.access

object AccessType {
    const val PUBLIC = 0
    const val SECRET = 1
    const val TOP_SECRET = 2

    val ACCESS_RANGE = PUBLIC..TOP_SECRET
}