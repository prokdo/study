package ru.prokdo.controller


import ru.prokdo.model.sort.SortOrder
import ru.prokdo.model.util.info.ProblemInfo


class SortController : Controller() {
    var problemInfo: ProblemInfo? = null

    fun getSortOrder(): SortOrder? {
        val choice: String
        try { choice = readln() }
        catch (exception: Exception) { return null }

        return when (choice) {
            "1" -> { SortOrder.ASCENDING }
            "2" -> { SortOrder.DESCENDING }
            "3" -> { SortOrder.SHAKE }
            else -> { null }
        }
    }
}