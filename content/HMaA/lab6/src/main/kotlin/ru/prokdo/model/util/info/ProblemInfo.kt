package ru.prokdo.model.util.info

import ru.prokdo.model.math.IntMatrix

data class ProblemInfo(
        var processorsNumber: Int = 0,
        var tasksNumber: Int = 0,
        var weightBounds: Pair<Int, Int> = Pair(0, 0),
        var weightMatrix: IntMatrix = IntMatrix(0, 0),
        var individualsNumber: Int = 0,
        var limitNumber: Int = 0,
        var crossoverProbability: Int = 0,
        var mutationProbability: Int = 0
) : Info() {

    override fun toString(): String {
        val builder = StringBuilder()

        builder.append("Количество процессоров (M): ${this.processorsNumber}\n")

        builder.append("Количество задач (N): ${this.tasksNumber}\n")

        builder.append(
                "Границы весов задач (T1, T2): ${this.weightBounds.first}, ${this.weightBounds.second}\n"
        )

        builder.append("Матрица весов задач (T):\n")
        builder.append("${this.weightMatrix}\n\n")

        builder.append("Количество особей в поколении (Z): ")
        builder.append("${this.individualsNumber}\n")

        builder.append("Предельное число повтора лучшей особи (K): ")
        builder.append("${this.limitNumber}\n")

        builder.append("Вероятность кроссовера (Pc, %): ")
        builder.append("${this.crossoverProbability}\n")

        builder.append("Вероятность мутации (Pm, %): ")
        builder.append("${this.mutationProbability}\n")

        return builder.toString()
    }
}
