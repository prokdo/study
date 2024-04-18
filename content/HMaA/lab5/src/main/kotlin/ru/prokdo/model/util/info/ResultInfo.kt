package ru.prokdo.model.util.info


import ru.prokdo.model.criterion.CriterionType
import ru.prokdo.model.math.Matrix
import ru.prokdo.model.sort.SortOrder


/**
 * Data class representing a set of output data for the problem been solved.
 *
 * @param generationsNumber number of generations used to solve the problem.
 * @param matrix result matrix with distributed tasks weights.
 * @param maxLoad maximum load among all processors.
 * @param elapsedTime elapsed time for solving the problem.
 *
 * @see Matrix
 */
data class ResultInfo(
    var generationsNumber: Int,
    var matrix: Matrix? = null,
    var maxLoad: Double = 0.0,
    var elapsedTime: Long = 0) : Info() {
    
        override fun toString(): String {
        val builder = StringBuilder()

        builder.append("Количество поколений решения:\n")
        builder.append("${this.generationsNumber}\n")

        builder.append("Матрица решения (R):\n")
        builder.append("${this.matrix}\n")

        builder.append("Максимальная нагрузка процессоров (max): ${if (this.maxLoad - this.maxLoad.toInt() == 0.0) this.maxLoad.toInt() else this.maxLoad}\n")

        builder.append("Время решения задачи (t, мс): ${this.elapsedTime}")

        return builder.toString()
    }
}