package ru.prokdo.model.util.random

import ru.prokdo.model.math.IntMatrix
import ru.prokdo.model.tsp.info.ProblemInfo
import ru.prokdo.model.util.random.NumberGenerator

object DataGenerator {
    operator fun invoke(problemInfo: ProblemInfo): IntMatrix {
        val matrix = IntMatrix(problemInfo.nodesNumber, problemInfo.nodesNumber)
        run breaking@ { matrix.forEachIndexed { rowIndex, _ -> run {            
            var columnIndex = rowIndex + 1
            while (columnIndex < problemInfo.nodesNumber) {
                matrix[rowIndex, columnIndex] = NumberGenerator(problemInfo.weightBounds.first, problemInfo.weightBounds.second)
                matrix[columnIndex, rowIndex] = matrix[rowIndex, columnIndex]

                columnIndex++
            }
        } } }

        return matrix
    }
}