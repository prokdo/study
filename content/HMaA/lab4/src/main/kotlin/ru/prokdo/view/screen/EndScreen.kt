package ru.prokdo.view.screen


import kotlin.system.exitProcess

import ru.prokdo.controller.EndController
import ru.prokdo.model.util.info.ProblemInfo


class EndScreen(problemInfo: ProblemInfo) : Screen() {
    override val controller = EndController()

    init {
        this.controller.problemInfo = problemInfo
    }

    override fun show() {
        println("=".repeat(150))
        println("Ршение поставленной задачи")
        println()

        println("Характеристика решаемой задачи:")
        println(controller.problemInfo)
        println()

        println("Производится сортировка исходной матрицы весов . . .")
        this.controller.launchSorter()

        println()

        println("Отсортированная матрица весов (T'):")
        println(this.controller.problemInfo!!.weightMatrix)

        println()

        println("Производится решение задачи . . . ")
        this.controller.launchSolver()

        println()

        println("Результат работы алгоритма:\n")
        println(this.controller.resultInfo)

        println()

        println("Возможные следующее действия:")
        println("[ 1 ] Вернуться в меню выбора критерия оптимальности")
        println("[ 2 ] Завершение работы")

        println()

        var nextScreen: Screen?
        while (true) {
            print("Выберите опцию на исполнение: ")
            nextScreen = this.controller.getNextScreen()

            if (nextScreen == null)  {
                println()
                println("Неверный формат ввода!\n")
                continue
            }

            break
        }

        println("=".repeat(150))
        println()

        when (nextScreen) {
            is CriterionScreen -> { this.controller.changeScreen(CriterionScreen(this.controller.problemInfo!!)) }
            is EndScreen -> { exitProcess(0) }
        }
    }
}