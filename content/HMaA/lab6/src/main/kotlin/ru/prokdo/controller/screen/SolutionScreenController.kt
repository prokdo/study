package ru.prokdo.controller.screen

import ru.prokdo.model.schedule.genetic.Solver
import ru.prokdo.model.util.info.ProblemInfo
import ru.prokdo.model.util.info.ResultInfo

class SolutionScreenController(val problemInfo: ProblemInfo) : ScreenController() {
    fun calculateResult(): ResultInfo = Solver(problemInfo)
}
