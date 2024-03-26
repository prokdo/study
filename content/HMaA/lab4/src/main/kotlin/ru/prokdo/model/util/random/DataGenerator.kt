package ru.prokdo.model.util.random


import ru.prokdo.model.math.Matrix
import ru.prokdo.model.util.info.ProblemInfo


/**
 * Object for random generation of a matrix with tasks' weights according to given parameters.
 */
object DataGenerator {
    /**
     * Function for generating the values of initial matrix of tasks weights in specified range. The generated matrix is returned to [problem info param][problemInfo].
     * @param problemInfo info about problem to solve.
     *
     * @see ProblemInfo
     */
    fun generateMatrix(problemInfo: ProblemInfo) {
        val matrix = Matrix(problemInfo.tasksNumber, problemInfo.processorsNumber)
        for (i in matrix.rowIndices)
            for (j in matrix.columnIndices)
                matrix[i, j] = NumberGenerator.generate(problemInfo.weightBounds.first, problemInfo.weightBounds.second, 0)

        problemInfo.weightMatrix = matrix
    }
}