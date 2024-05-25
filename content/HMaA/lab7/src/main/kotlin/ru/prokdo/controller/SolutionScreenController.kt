package ru.prokdo.controller

import ru.prokdo.model.tsp.genetic.info.GeneticProblemInfo
import ru.prokdo.model.tsp.genetic.info.GeneticResultInfo
import ru.prokdo.model.tsp.genetic.GeneticSolver
import ru.prokdo.model.tsp.info.ResultInfo
import ru.prokdo.model.tsp.greedy.GreedySolver

class SolutionScreenController: ScreenController() {
    lateinit var geneticProblemInfo: GeneticProblemInfo

    lateinit var greedyResultInfo: ResultInfo
    lateinit var geneticResultInfo: GeneticResultInfo

    fun solveGenetic(): GeneticResultInfo = GeneticSolver(geneticProblemInfo)

    fun solveGreedy(): ResultInfo = GreedySolver(geneticProblemInfo.problemInfo)
}