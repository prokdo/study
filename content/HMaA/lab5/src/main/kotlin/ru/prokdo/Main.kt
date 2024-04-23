package ru.prokdo


import ru.prokdo.model.util.info.ProblemInfo
import ru.prokdo.model.util.random.DataGenerator
import ru.prokdo.model.schedule.Solver


fun main() {
    val problemInfo = ProblemInfo(7, 15, Pair(10, 95))

    DataGenerator(problemInfo)

    problemInfo.limitNumber = 50
    problemInfo.individualsNumber = 10
    problemInfo.crossoverProbability = 85
    problemInfo.mutationProbability = 45

    println(problemInfo)

    val resultInfo = Solver(problemInfo)

    println(resultInfo)
}
