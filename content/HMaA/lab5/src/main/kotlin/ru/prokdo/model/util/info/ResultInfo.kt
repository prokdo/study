package ru.prokdo.model.util.info


import ru.prokdo.model.criterion.CriterionType
import ru.prokdo.model.math.Matrix
import ru.prokdo.model.sort.SortOrder


/**
 * Data class representing a set of output data for the problem been solved.
 *
 * @param criterionType distribution optimality criterion for tasks weights.
 * @param sortOrder type of sorting of the initial matrix by the sum of the rows.
 * @param matrix result matrix with distributed tasks weights.
 * @param maxLoad maximum load among all processors.
 * @param elapsedTime elapsed time for solving the problem.
 *
 * @see CriterionType
 * @see SortOrder
 * @see Matrix
 */
data class ResultInfo(
    var criterionType: CriterionType,
    var sortOrder: SortOrder,
    var matrix: Matrix? = null,
    var maxLoad: Double = 0.0,
    var elapsedTime: Long = 0
) : Info() {
    override fun toString(): String {
        val builder = StringBuilder()

        builder.append("Вид критерия оптимальности распределения: ")
        when (this.criterionType) {
            CriterionType.MINIMAX -> { builder.append("минимаксный")}
            CriterionType.QUADRATIC -> { builder.append("квадратичный")}
            CriterionType.CUBIC -> { builder.append("кубический")}
        }
        builder.append('\n')

        builder.append("Вид сортировки исходной матрицы весов: ")
        when (this.sortOrder) {
            SortOrder.ASCENDING -> { builder.append("по возрастанию")}
            SortOrder.DESCENDING -> { builder.append("по убыванию")}
            SortOrder.SHAKE -> { builder.append("случайно")}
        }
        builder.append('\n')

        builder.append("Матрица решения (R):\n")
        builder.append("${this.matrix}\n")

        builder.append("Максимальная нагрузка процессоров (max): ${if (this.maxLoad - this.maxLoad.toInt() == 0.0) this.maxLoad.toInt() else this.maxLoad}\n")

        builder.append("Время решения задачи (t, мс): ${this.elapsedTime}")

        return builder.toString()
    }
}