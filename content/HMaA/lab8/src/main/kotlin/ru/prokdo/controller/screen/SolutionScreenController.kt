package ru.prokdo.controller.screen

import ru.prokdo.model.schedule.genetic.GeneticSolver
import ru.prokdo.model.schedule.genetic.info.ProblemInfo
import ru.prokdo.model.schedule.genetic.info.ResultInfo

class SolutionScreenController(val problemInfo: ProblemInfo) : ScreenController() {
    fun calculateResult(): ResultInfo = GeneticSolver(problemInfo)
}
