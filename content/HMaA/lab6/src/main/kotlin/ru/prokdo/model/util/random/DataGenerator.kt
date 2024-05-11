package ru.prokdo.model.util.random

import ru.prokdo.model.math.IntMatrix
import ru.prokdo.model.schedule.genetic.info.ProblemInfo

object DataGenerator {
    operator fun invoke(problemInfo: ProblemInfo): IntMatrix {
        val matrix = IntMatrix(problemInfo.tasksNumber, problemInfo.processorsNumber)
        matrix.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, _ ->
                matrix[rowIndex, columnIndex] =
                        NumberGenerator(
                                problemInfo.weightBounds.first,
                                problemInfo.weightBounds.second
                        )
            }
        }

        return matrix
    }
}
