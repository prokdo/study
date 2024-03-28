package ru.prokdo.view.screen


import ru.prokdo.controller.SortController
import ru.prokdo.model.util.info.ProblemInfo


class SortScreen(problemInfo: ProblemInfo) : Screen() {
    override val controller = SortController()

    init {
        this.controller.problemInfo = problemInfo
    }

    override fun show() {
        println("=".repeat(150))
        println("Выбор сортировки матрицы весов по сумме строк")
        println()

        println("Характеристика решаемой задачи:\n")
        println(controller.problemInfo)
        println()

        println("Возможные опции сортировки:")
        println("[ 1 ] По возрастанию")
        println("[ 2 ] По убыванию")
        println("[ 3 ] Случайно")

        println()

        while (true) {
            print("Выберите порядок сортировки: ")
            val sortOrder = this.controller.getSortOrder()

            if (sortOrder == null)  {
                println()
                println("Неверный формат ввода!\n")
                continue
            }

            this.controller.problemInfo!!.sortOrder = sortOrder
            break
        }

        println("=".repeat(150))
        println()

        this.controller.changeScreen(EndScreen(this.controller.problemInfo!!))
    }
}