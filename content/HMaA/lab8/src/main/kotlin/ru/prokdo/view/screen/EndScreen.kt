package ru.prokdo.view.screen

import kotlin.system.exitProcess

import ru.prokdo.controller.Terminal
import ru.prokdo.controller.screen.EndScreenController
import ru.prokdo.model.schedule.genetic.info.ResultInfo

class EndScreen(resultInfo: ResultInfo) : Screen() {
    override val _controller = EndScreenController(resultInfo)

    override fun show() {
        val isGoingRestart: Boolean
        while (true) {
            _clearTerminal()

            _showInfoFrame()
            _showExitDialog()

            val choice = readlnOrNull()
            when (_controller.verifyUInt(choice)) {
                1 -> {
                    isGoingRestart = true
                    break
                }

                2 -> {
                    print("\nЗавершение работы . . .")
                    isGoingRestart = false
                    break
                }

                else -> {
                    println()
                    print(
                            """
                                |Неверный формат, необходим повторный ввод
                                |Для продолжения нажмите клавишу ENTER. . .
                            """.trimMargin()
                    )

                    readLine()
                    continue
                }
            }
        }

        if (!isGoingRestart) exitProcess(0)

        Terminal().changeScreen(GeneticScreen(_controller.resultInfo.problemInfo))
    }

    private fun _showInfoFrame() {
        print(
                """
                    |Лабораторная работа №8 "Теория разнородных расписаний, генетическая модель Голдберга"
                    |Автор: ВПР32, Прокопенко Дмитрий

                    |Статус: ожидание завершение сеанса
                """.trimMargin() + "\n\n"
        )

        println(_controller.resultInfo)
        println()
    }

    private fun _showExitDialog() {
        print(
                """
                    |Каким образом завершить текущий сеанс расчета?

                    |[ 1 ] Вернуться в меню задания характеристик генетической модели Голдберга
                    |[ 2 ] Завершение работы программы

                    |Выберите опцию: 
                """.trimMargin()
        )
    }
}
