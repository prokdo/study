package ru.prokdo.view.screen

import ru.prokdo.controller.Terminal
import ru.prokdo.controller.screen.StartScreenController
import ru.prokdo.model.util.log.Logger

class StartScreen : Screen() {
    private val _log =  Logger(
                                """
                                    |Лабораторная работа №8 "Теория разнородных расписаний, генетическая модель Голдберга"
                                    |Автор: ВПР32, Прокопенко Дмитрий

                                    |Статус: ввод характеристик задачи
                                """.trimMargin() + "\n\n"
                        )

    override protected val _controller = StartScreenController()

    override fun show() {
        _controller.problemInfo.processorsNumber = _askUInt("Количество процессоров (M): ")
        _controller.problemInfo.tasksNumber = _askUInt("Количество задач (N): ")
        _controller.problemInfo.weightBounds = _askUIntPair("Границы весов задач (T1, T2): ")
        _controller.problemInfo.weightMatrix = _controller.generateWeightMatrix()

        Terminal().changeScreen(GeneticScreen(_controller.problemInfo))
    }

    private fun _askUInt(prompt: String): Int {
        _log.append(prompt)

        while (true) {
            _clearTerminal()

            print(_log)

            val input = readlnOrNull()

            val value = _controller.verifyUInt(input)
            if (value != null) {
                _log.append("${input}\n")

                return value
            } else {
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

    private fun _askUIntPair(prompt: String): Pair<Int, Int> {
        _log.append(prompt)

        while (true) {
            _clearTerminal()

            print(_log)

            val input = readlnOrNull()

            val value = _controller.verifyUIntPair(input)
            if (value != null) {
                _log.append("${input}\n")

                return value
            } else {
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
}
