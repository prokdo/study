package ru.prokdo.controller


import ru.prokdo.model.util.info.ProblemInfo
import ru.prokdo.model.util.random.DataGenerator


class StartController : Controller() {
    var tasksNumber = 0
    var processorsNumber = 0
    var weightBounds = Pair(0, 0)

    var problemInfo: ProblemInfo? = null

    fun readNaturalNumber(): Int? {
        val number: Int
        try { number = readln().toInt() }
        catch (exception: Exception) { return null}

        if (number < 1) return null

        return  number
    }

    fun readNaturalPair(): Pair<Int, Int>? {
        var pair: Pair<Int, Int>
        try {
            val splitted = readln().split(", ", ",")
            pair = Pair(splitted[0].toInt(), splitted[1].toInt())
        }
        catch (exception: Exception) { return null}

        if (pair.first < 1 || pair.second < 1) return null

        if (pair.first > pair.second) pair = Pair(pair.second, pair.first)

        return pair
    }

    fun createProblemInfo() {
        this.problemInfo = ProblemInfo(this.processorsNumber, this.tasksNumber, this.weightBounds)
        DataGenerator.generateMatrix(this.problemInfo!!)
    }
}