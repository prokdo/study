package ru.prokdo.model.util.random


import ru.prokdo.model.math.Matrix
import ru.prokdo.model.util.info.ProblemInfo
import ru.prokdo.model.util.random.NumberGenerator
import ru.prokdo.model.schedule.genetic.individual.Individual


/**
 * Object for random generation of a list with tasks' weights according to given parameters.
 */
object DataGenerator {
    /**
     * Function for generating the values of initial array of tasks weights in specified range.
     * @param problemInfo info about problem to solve.
     *
     * @see ProblemInfo
     */
    operator fun invoke(problemInfo: ProblemInfo): IntArray {
        val array = IntArray(problemInfo.tasksNumber)
        for (index in array.indices) 
            array[index] = NumberGenerator(from = problemInfo.weightBounds.first, until = problemInfo.weightBounds.second).toInt()

        return array
    }
}