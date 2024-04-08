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
 * @param weightMatrix initial matrix of task weights of size [tasksNumber] by [processorsNumber]. Initially is null, must be filled manually or generated randomly.
 * @param criterionType distribution optimality criterion for tasks weights.
 * @param sortOrder type of sorting of the initial matrix by the sum of the rows.
 *
 * @see Matrix
 * @see CriterionType
 * @see SortOrder
 */
data class ProblemInfo(
    var processorsNumber: Int,
    var tasksNumber: Int,
    var weightBounds: Pair<Int, Int>,
    var weightMatrix: Matrix? = null,
    var criterionType: CriterionType? = null,
    var sortOrder: SortOrder? = null
) : Info() {
    override fun toString(): String {
        val builder = StringBuilder()

        builder.append("Количество процессоров (M): ${this.processorsNumber}\n")

        builder.append("Количество задач (N): ${this.tasksNumber}\n")

        builder.append("Границы весов задач (T1, T2): ${this.weightBounds.first}, ${this.weightBounds.second}\n")

        builder.append("Матрица весов задач (T):\n")
        builder.append("${this.weightMatrix}\n")

        builder.append("Вид критерия оптимальности распределения: ")
        when (this.criterionType) {
            CriterionType.MINIMAX -> { builder.append("минимаксный")}
            CriterionType.QUADRATIC -> { builder.append("квадратичный")}
            CriterionType.CUBIC -> { builder.append("кубический")}
            else -> { builder.append("нет данных") }
        }
        builder.append('\n')

        builder.append("Вид сортировки исходной матрицы весов: ")
        when (this.sortOrder) {
            SortOrder.ASCENDING -> { builder.append("по возрастанию")}
            SortOrder.DESCENDING -> { builder.append("по убыванию")}
            SortOrder.SHAKE -> { builder.append("случайно")}
            else -> { builder.append("нет данных") }
        }

        return builder.toString()
    }
}
