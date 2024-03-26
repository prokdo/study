package ru.prokdo.controller


import ru.prokdo.model.criterion.CriterionType
import ru.prokdo.model.math.Matrix
import ru.prokdo.model.solution.Solver
import ru.prokdo.model.sort.SortOrder
import ru.prokdo.model.sort.Sorter
import ru.prokdo.model.util.info.ProblemInfo
import ru.prokdo.model.util.info.ResultInfo
import ru.prokdo.view.screen.CriterionScreen
import ru.prokdo.view.screen.EndScreen
import ru.prokdo.view.screen.Screen


class EndController : Controller() {
    var problemInfo: ProblemInfo? = null
    var resultInfo: ResultInfo? = null

    fun launchSorter() {
        Sorter.sort(this.problemInfo!!)
    }

    fun launchSolver() {
        this.resultInfo = Solver.solve(problemInfo!!)
    }

    fun getNextScreen(): Screen? {
        val choice: String
        try { choice = readln() }
        catch (exception: Exception) { return null }

        return when (choice) {
            "1" -> { CriterionScreen(this.problemInfo!!) }
            "2" -> { EndScreen(this.problemInfo!!) }
            else -> { null }
        }
    }
}