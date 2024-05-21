package ru.prokdo.model.schedule.genetic.info

import ru.prokdo.model.math.IntMatrix

data class  ResultInfo(
                        var problemInfo: ProblemInfo = ProblemInfo(),
                        var generationsNumber: Int,
                        var matrix: IntMatrix = IntMatrix(0, 0),
                        var maxLoad: Int = 0,
                        var elapsedTime: Long = 0,
                        var solverLog: String = ""
            ) : Info() {

    override fun toString(): String {
        val builder = StringBuilder()

        builder.append("Информация о решенной задаче:\n\n")
        builder.append("$problemInfo\n")

        builder.append("Информация о решении задачи:\n\n")

        builder.append("Количество поколений решения: $generationsNumber\n")

        builder.append("Матрица решения (R):\n")
        builder.append("$matrix\n")

        builder.append("Максимальная нагрузка процессоров (max): $maxLoad\n")

        builder.append("Время решения задачи (t, мс): $elapsedTime")

        return builder.toString()
    }
}
