package ru.prokdo.model.util.info


import ru.prokdo.model.math.Matrix


/**
 * Data class representing a set of output data for the problem been solved.
 *
 * @param problemInfo information about problem that was solved.
 * @param generationsNumber number of generations used to solve the problem.
 * @param matrix result matrix with distributed tasks weights.
 * @param maxLoad maximum load among all processors.
 * @param elapsedTime elapsed time for solving the problem.
 * @param solverLog solver operation log.
 *
 * @see Matrix
 * @see ProbmlemInfo
 */
data class ResultInfo(
                        var problemInfo: ProblemInfo = ProblemInfo(),
                        var generationsNumber: Int,
                        var matrix: Matrix = Matrix(0, 0),
                        var maxLoad: Double = 0.0,
                        var elapsedTime: Long = 0,
                        var solverLog: String = "") : Info() {
    
        override fun toString(): String {
        val builder = StringBuilder()

        builder.append("Информация о решенной задаче:\n\n")
        builder.append("${this.problemInfo}\n")

        builder.append("Информация о решении задачи:\n\n")

        builder.append("Количество поколений решения: ${this.generationsNumber}\n")

        builder.append("Матрица решения (R):\n")
        builder.append("${this.matrix}\n")

        builder.append("Максимальная нагрузка процессоров (max): ${if (this.maxLoad - this.maxLoad.toInt() == 0.0) this.maxLoad.toInt() else this.maxLoad}\n")

        builder.append("Время решения задачи (t, мс): ${this.elapsedTime}")

        return builder.toString()
    }
}