package ru.prokdo.model.schedule


import kotlin.math.pow
import kotlin.system.measureTimeMillis

import ru.prokdo.model.criterion.CriterionType
import ru.prokdo.model.math.Matrix
import ru.prokdo.model.util.info.ProblemInfo
import ru.prokdo.model.util.info.ResultInfo


object Solver {
    fun solve(problemInfo: ProblemInfo): ResultInfo {
        val resultMatrix: Matrix
        val elapsedTime = measureTimeMillis {
            resultMatrix = when (problemInfo.criterionType!!) {
                CriterionType.MINIMAX -> { solveMinimax(problemInfo) }
                CriterionType.QUADRATIC -> { solveQuadratic(problemInfo) }
                CriterionType.CUBIC -> { solveCubic(problemInfo) }
            }
        }
        val maxLoad = resultMatrix.transpose().getRowsSums().max()

        return ResultInfo(
            problemInfo.criterionType!!,
            problemInfo.sortOrder!!,
            resultMatrix,
            maxLoad,
            elapsedTime)
    }

    private fun solveMinimax(problemInfo: ProblemInfo): Matrix {
        val result = Matrix(problemInfo.weightMatrix!!.height, problemInfo.weightMatrix!!.width)

        val processorsLoad = Array(result.width) { 0.0 }
        for (rowIndex in result.rowIndices) {
            val prediction = processorsLoad.clone()
            for (columnIndex in result.columnIndices)
                prediction[columnIndex] += problemInfo.weightMatrix!![rowIndex, columnIndex]

            val minLoadIndex = prediction.indexOfFirst { load -> load == prediction.min() }

            result[rowIndex, minLoadIndex] = problemInfo.weightMatrix!![rowIndex, minLoadIndex]
            processorsLoad[minLoadIndex] += problemInfo.weightMatrix!![rowIndex, minLoadIndex]
        }

        return  result
    }

    private fun solveQuadratic(problemInfo: ProblemInfo): Matrix {
        val result = Matrix(problemInfo.weightMatrix!!.height, problemInfo.weightMatrix!!.width)

        val processorsLoad = Array(result.width) { 0.0 }
        for (rowIndex in result.rowIndices) {
            val prediction = Array(problemInfo.weightMatrix!!.width) { 0.0 }
            for (predictionIndex in prediction.indices)
                for (columnIndex in result.columnIndices)
                    if (columnIndex == predictionIndex)
                        prediction[predictionIndex] += (processorsLoad[columnIndex] +
                                problemInfo.weightMatrix!![rowIndex][columnIndex]).pow(2)
                    else prediction[predictionIndex] += processorsLoad[columnIndex].pow(2)

            val minLoadIndex = prediction.indexOfFirst { load -> load == prediction.min() }

            result[rowIndex, minLoadIndex] = problemInfo.weightMatrix!![rowIndex, minLoadIndex]
            processorsLoad[minLoadIndex] += problemInfo.weightMatrix!![rowIndex, minLoadIndex]
        }

        return result
    }

    private fun solveCubic(problemInfo: ProblemInfo): Matrix {
        val result = Matrix(problemInfo.weightMatrix!!.height, problemInfo.weightMatrix!!.width)

        val processorsLoad = Array(result.width) { 0.0 }
        for (rowIndex in result.rowIndices) {
            val prediction = Array(problemInfo.weightMatrix!!.width) { 0.0 }
            for (predictionIndex in prediction.indices)
                for (columnIndex in result.columnIndices)
                    if (columnIndex == predictionIndex)
                        prediction[predictionIndex] += (processorsLoad[columnIndex] +
                                problemInfo.weightMatrix!![rowIndex][columnIndex]).pow(3)
                    else prediction[predictionIndex] += processorsLoad[columnIndex].pow(3)

            val minLoadIndex = prediction.indexOfFirst { load -> load == prediction.min() }

            result[rowIndex, minLoadIndex] = problemInfo.weightMatrix!![rowIndex, minLoadIndex]
            processorsLoad[minLoadIndex] += problemInfo.weightMatrix!![rowIndex, minLoadIndex]
        }

        return result
    }
}