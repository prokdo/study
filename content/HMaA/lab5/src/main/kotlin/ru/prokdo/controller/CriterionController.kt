package ru.prokdo.controller


import ru.prokdo.model.criterion.CriterionType
import ru.prokdo.model.util.info.ProblemInfo


class CriterionController : Controller() {
    var problemInfo: ProblemInfo? = null

    fun getCriterionType(): CriterionType? {
        val choice: String
        try { choice = readln() }
        catch (exception: Exception) { return null }

        return when (choice) {
            "1" -> { CriterionType.MINIMAX }
            "2" -> { CriterionType.QUADRATIC }
            "3" -> { CriterionType.CUBIC }
            else -> { null }
        }
    }
}