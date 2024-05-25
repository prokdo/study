package ru.prokdo.screen

import ru.prokdo.controller.StartScreenController
import ru.prokdo.screen.GeneticScreen

class StartScreen: Screen() {
    override val _controller = StartScreenController()

    override var _header =  
                            """
                                |Лабораторная работа №7 "Алгоритмы на графах, задача о коммивояжере"
                                |Автор: ВПР32, Прокопенко Дмитрий

                                |Статус: ввод характеристик задачи
                            """.trimMargin()

    override var _invalidInputMessage =
                            """
                                |Неправильный формат, повторите ввод
                                |Для продолжения нажмите клавишу ENTER . . .
                            """.trimMargin()

    override fun show() { 
        _log.append("$_header\n\n")
        _controller.problemInfo.nodesNumber = _askUInt("Количество вершин (N): ").toInt()
        
        _log.append("Начальная вершина (S): ")
        while (true) {
            _clear()
            print(_log)

            val value = _controller.verifyUInt(readlnOrNull())
            when {
                value == null || value.toInt() > _controller.problemInfo.nodesNumber -> {
                    println()
                    _showInvalidInputMessage()

                    continue
                }

                else -> {
                    _controller.problemInfo.startNode = value.toInt()
                    _log.append("$value\n")
                    _clear()
                    print(_log)
                    break
                }
            }
        }

        _controller.problemInfo.weightBounds = _askIntPair("Границы весов путей (T1, T2): ")
        _controller.problemInfo.weightMatrix = _controller.generateMatrix()

        _controller.changeScreen(GeneticScreen(_controller.problemInfo))
    }
}