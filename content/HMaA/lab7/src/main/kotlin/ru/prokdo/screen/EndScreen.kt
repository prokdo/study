package ru.prokdo.screen

import ru.prokdo.model.tsp.info.ProblemInfo
import ru.prokdo.controller.EndScreenController
import kotlin.system.exitProcess

class EndScreen(problemInfo: ProblemInfo): Screen() {
    override val _controller = EndScreenController()

    init { _controller.problemInfo = problemInfo }

    override var _header =  
                            """
                                |Лабораторная работа №7 "Алгоритмы на графах, задача о коммивояжере"
                                |Автор: ВПР32, Прокопенко Дмитрий

                                |Статус: завершение сеанса
                            """.trimMargin()

    override var _invalidInputMessage =
                            """
                                |Неправильный формат, повторите ввод
                                |Для продолжения нажмите клавишу ENTER . . .
                            """.trimMargin()

    override fun show() { 
        _log.append(_header)

        _log.append(
                    """
                        |Работа программы завершена. Завершить сеанс?

                        |[ 1 ] Нет, вернуться в меню задания генетических характеристик
                        |[ 2 ] Да, завершить работу

                        |Выберите опцию на исполнение: 
                    """.trimMargin()
        )
        while (true) {
            _clear()
            print(_log)

            val value = _controller.verifyUInt(readlnOrNull())
            when {
                value == null || value.toInt() > 2 -> {
                    println()
                    _showInvalidInputMessage()

                    continue
                }

                value.toInt() == 2 -> {
                    println("Завершение работы . . .")
                    exitProcess(0)
                }

                else -> _controller.changeScreen(GeneticScreen(_controller.problemInfo))
            }
        }
    }
    
}