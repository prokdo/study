package ru.prokdo.model.util.random

import ru.prokdo.model.math.IntMatrix
import ru.prokdo.model.schedule.genetic.info.ProblemInfo

object DataGenerator {
    operator fun invoke(problemInfo: ProblemInfo): IntMatrix {
        val tasksWeights = IntArray(problemInfo.tasksNumber)
        tasksWeights.forEachIndexed { index, _ ->   
            tasksWeights[index] =   NumberGenerator(
                                                    problemInfo.weightBounds.first,
                                                    problemInfo.weightBounds.second
                                    ) 
        } 

        val matrix = IntMatrix(problemInfo.tasksNumber, problemInfo.processorsNumber)
        matrix.forEachIndexed { rowIndex, row -> run { 
            var infinityCount = 0
            row.forEachIndexed { columnIndex, _ -> run {
                matrix[rowIndex, columnIndex] = tasksWeights[rowIndex]

                val random = NumberGenerator(100)
                if (random <= 50 && infinityCount < row.size - 1) {
                    matrix[rowIndex, columnIndex] = IntMatrix.POSITIVE_INFINITY
                    infinityCount++
                }
            } }
        } }

        return matrix
    }
}
