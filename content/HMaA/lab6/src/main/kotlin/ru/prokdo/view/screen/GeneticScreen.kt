package ru.prokdo.view.screen

import ru.prokdo.controller.Terminal
import ru.prokdo.controller.screen.GeneticScreenController
import ru.prokdo.model.schedule.genetic.info.ProblemInfo
import ru.prokdo.model.util.log.Logger

class GeneticScreen(problemInfo: ProblemInfo) : Screen() {
    private val _log = 
                        Logger(
                                """
                                    |Лабораторная работа №6 "Теория разнородных расписаний, генетическая модель Холланда"
                                    |Автор: ВПР32, Прокопенко Дмитрий

                                    |Статус: ввод параметров модели Холланда
                                """.trimMargin() + "\n\n"
                        )

    override protected val _controller = GeneticScreenController(problemInfo)

    init {
        _log.append(
                    """
                        |Количество процессоров (M): ${_controller.problemInfo.processorsNumber}
                        |Количество задач (N): ${_controller.problemInfo.tasksNumber}
                        |Границы весов задач (T1, T2): ${_controller.problemInfo.weightBounds.first}, ${_controller.problemInfo.weightBounds.second}
                        |Матрица весов задач (T):
                        |${_controller.problemInfo.weightMatrix}
                    """.trimMargin()
        )
        _log.append("\n\n")
    }

    override fun show() {
        _controller.problemInfo.individualsNumber = _askUInt("Количество особей в поколении (Z): ")
        _controller.problemInfo.limitNumber = _askUInt("Предельное число повтора лучшей особи (K): ")
        _controller.problemInfo.crossoverProbability = _askUInt("Вероятность кроссовера (Pc, %): ")
        _controller.problemInfo.mutationProbability = _askUInt("Вероятность мутации (Pm, %): ")

        Terminal().changeScreen(SolutionScreen(_controller.problemInfo))
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
}
