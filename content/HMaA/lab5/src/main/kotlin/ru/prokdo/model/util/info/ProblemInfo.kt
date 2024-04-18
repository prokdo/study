package ru.prokdo.model.util.info


import ru.prokdo.model.criterion.CriterionType
import ru.prokdo.model.math.Matrix
import ru.prokdo.model.sort.SortOrder


/**
 * Data class representing a set of input data for the problem being solved.
 *
 * @param processorsNumber number of processors that exist and work in the target system.
 * @param tasksNumber number of tasks that need to be distributed to processors.
 * @param weightBounds minimum (inclusive) and maximum (inclusive) permissible values of weights of tasks to be distributed.
 * @param weightList initial list of task weights of size [tasksNumvber]. Initially is null, must be filled manually or generated randomly.
 * @param individualsNumber number of individuals in population.
 * @param limitNumber limit number of of repetitions of the best individual.
 * @param crossingoverProbability probability of individuals crossing over.
 * @param mutationProbability probability of mutation of the offspring when crossing individuals.
 *
 */
data class ProblemInfo(
    var processorsNumber: Int,
    var tasksNumber: Int,
    var weightBounds: Pair<Int, Int>,
    var weightList: List<Int>? = null,
    var individualsNumber: Int = 0,
    var limitNumber: Int = 0,
    var crossoverProbability: Double = 0.0,
    var mutationProbability: Double = 0.0) : Info() {
    
        override fun toString(): String {
        val builder = StringBuilder()

        builder.append("Количество процессоров (M): ${this.processorsNumber}\n")

        builder.append("Количество задач (N): ${this.tasksNumber}\n")

        builder.append("Границы весов задач (T1, T2): ${this.weightBounds.first}, ${this.weightBounds.second}\n")

        builder.append("Список весов задач (T): [")
        this.weightList!!.forEachIndexed { index, weight -> run { 
                builder.append(weight)

                if (index != this.weightList!!.size - 1) builder.append(", ")
                else builder.append("]\n") 
            } 
        }

        builder.append("Количество особей в поколении (Z): ")
        builder.append("${this.individualsNumber}\n")

        builder.append("Предельное число повтора лучшей особи (K): ")
        builder.append("${this.limitNumber}\n")

        builder.append("Вероятность кроссовера (Pc, %): ")
        builder.append("${this.crossoverProbability}\n")

        builder.append("Вероятность мутации (Pm, %): ")
        builder.append("${this.crossoverProbability}\n")

        return builder.toString()
    }
}
