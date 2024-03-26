package ru.prokdo.view.screen


import ru.prokdo.controller.CriterionController
import ru.prokdo.model.util.info.ProblemInfo
import ru.prokdo.view.Terminal


class CriterionScreen(problemInfo: ProblemInfo) : Screen() {
    override val controller = CriterionController()

    init {
        this.controller.problemInfo = problemInfo
    }

    override fun show() {
        println("=".repeat(150))
        println("Выбор критерия оптимальности распределения")
        println()

        println("Характеристика решаемой задачи:\n")
        println(controller.problemInfo)
        println()

        println("Возможные критерии:")
        println("[ 1 ] Минимаксный")
        println("[ 2 ] Квадратичный")
        println("[ 3 ] Кубический")

        println()

        while (true) {
            print("Выберите критерий: ")
            val criterionType = this.controller.getCriterionType()

            if (criterionType == null)  {
                println()
                println("Неверный формат ввода!\n")
                continue
            }

            this.controller.problemInfo!!.criterionType = criterionType
            break
        }

        println("=".repeat(150))
        println()

        Terminal.setScreen(SortScreen(this.controller.problemInfo!!))
    }
}