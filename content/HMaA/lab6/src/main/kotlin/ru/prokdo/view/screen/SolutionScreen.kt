package ru.prokdo.view.screen

import ru.prokdo.controller.Terminal
import ru.prokdo.controller.screen.SolutionScreenController
import ru.prokdo.model.util.info.ProblemInfo
import ru.prokdo.model.util.info.ResultInfo

class SolutionScreen(problemInfo: ProblemInfo) : Screen() {
    override val controller = SolutionScreenController(problemInfo)

    override fun show() {
        showInfoFrame()

        showWaitFrame()

        val resultInfo = controller.calculateResult()

        showResultFrame(resultInfo)

        Terminal().changeScreen(SaveScreen(resultInfo))
    }

    private fun showInfoFrame() {
        print(
                """
                    |Лабораторная работа №5 "Теория однородных расписаний, генетическая модель Голдберга"
                    |Автор: ВПР32, Прокопенко Дмитрий

                    |Статус: ожидание подтверждения начала расчета
              """.trimMargin() +
                        "\n\n"
        )

        println(controller.problemInfo)

        print("Для начала расчета, нажмите клавишу ENTER . . .")

        readLine()
        clearTerminal()
    }

    private fun showWaitFrame() {
        print(
                """
                    |Лабораторная работа №5 "Теория однородных расписаний, генетическая модель Голдберга"
                    |Автор: ВПР32, Прокопенко Дмитрий

                    |Статус: производится расчет решения задачи
              """.trimMargin() +
                        "\n\n"
        )

        println(controller.problemInfo)
    }

    private fun showResultFrame(resultInfo: ResultInfo) {
        clearTerminal()

        print(
                """
                    |Лабораторная работа №5 "Теория однородных расписаний, генетическая модель Голдберга"
                    |Автор: ВПР32, Прокопенко Дмитрий

                    |Статус: демонстрация полного решения задачи
              """.trimMargin() +
                        "\n\n"
        )

        println(controller.problemInfo)

        print(resultInfo.solverLog)

        print(resultInfo)

        println("\n")

        print("Для перехода к краткому виду решения задачи, нажмите клавишу ENTER . . .")

        readLine()
        clearTerminal()
    }
}
