package ru.prokdo.controller

import ru.prokdo.model.math.IntMatrix
import ru.prokdo.model.tsp.info.ProblemInfo
import ru.prokdo.model.util.random.DataGenerator

class StartScreenController: ScreenController() {
    val problemInfo = ProblemInfo()

    fun generateMatrix(): IntMatrix = DataGenerator(problemInfo)
}