package ru.prokdo.view.screen

import ru.prokdo.controller.Terminal
import ru.prokdo.controller.screen.SolutionScreenController
import ru.prokdo.model.schedule.genetic.info.ProblemInfo
import ru.prokdo.model.schedule.genetic.info.ResultInfo

class SolutionScreen(problemInfo: ProblemInfo) : Screen() {
    override protected val _controller = SolutionScreenController(problemInfo)

    override fun show() {
        _showInfoFrame()

        _showWaitFrame()

        val resultInfo = _controller.calculateResult()

        _showResultFrame(resultInfo)

        Terminal().changeScreen(SaveScreen(resultInfo))
    }

    private fun _showInfoFrame() {
        print(
                """
                    |Лабораторная работа №8 "Теория разнородных расписаний, генетическая модель Голдберга"
                    |Автор: ВПР32, Прокопенко Дмитрий

                    |Статус: ожидание подтверждения начала расчета
                """.trimMargin() + "\n\n"
        )

        println(_controller.problemInfo)

        print("Для начала расчета, нажмите клавишу ENTER . . .")

        readLine()
        _clearTerminal()
    }

    private fun _showWaitFrame() {
        print(
                """
                    |Лабораторная работа №8 "Теория разнородных расписаний, генетическая модель Голдберга"
                    |Автор: ВПР32, Прокопенко Дмитрий

                    |Статус: производится расчет решения задачи
                """.trimMargin() + "\n\n"
        )

        println(_controller.problemInfo)
    }

    private fun _showResultFrame(resultInfo: ResultInfo) {
        _clearTerminal()

        print(
                """
                    |Лабораторная работа №8 "Теория разнородных расписаний, генетическая модель Голдберга"
                    |Автор: ВПР32, Прокопенко Дмитрий

                    |Статус: демонстрация полного решения задачи
                """.trimMargin() + "\n\n"
        )

        println(_controller.problemInfo)

        print(resultInfo.solverLog)

        print(resultInfo)

        println("\n")

        print("Для перехода к краткому виду решения задачи, нажмите клавишу ENTER . . .")

        readLine()
        _clearTerminal()
    }
}
