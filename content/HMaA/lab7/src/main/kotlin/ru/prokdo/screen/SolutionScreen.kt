package ru.prokdo.screen

import org.graphstream.ui.view.Viewer

import ru.prokdo.model.tsp.genetic.info.GeneticProblemInfo
import ru.prokdo.controller.SolutionScreenController

class SolutionScreen(geneticProblemInfo: GeneticProblemInfo): Screen() {
    override val _controller = SolutionScreenController()

    init { _controller.geneticProblemInfo = geneticProblemInfo}

    override var _header =  
                            """
                                |Лабораторная работа №7 "Алгоритмы на графах, задача о коммивояжере"
                                |Автор: ВПР32, Прокопенко Дмитрий
                            """.trimMargin()

    override var _invalidInputMessage =
                            """
                                |Неправильный формат, повторите ввод
                                |Для продолжения нажмите клавишу ENTER . . .
                            """.trimMargin()

    override fun show() { 
        print(_header)
        println("\n\nСтатус: решение задачи жадным алгоритмом")
        println()
        println("""
                    |Параметры задачи:

                    |${_controller.geneticProblemInfo.problemInfo}
                """.trimMargin() + "\n\n")

        _controller.greedyResultInfo = _controller.solveGreedy()
        _controller.greedyResultInfo.pathGraph.display().setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY)

        _clear()

        print(_header)
        println("\n\nСтатус: ожидание отклика от пользователя")
        println()
        println(
                """
                    |Параметры задачи:

                    |${_controller.geneticProblemInfo.problemInfo}
                """.trimMargin() + "\n\n"
        )

        print(  
                """
                    |Решение, полученное жадным алгоритмом:

                    |${_controller.greedyResultInfo}
                """.trimMargin()
        )

        _wait("\n\nДля продолжения нажмите клавишу ENTER . . .")

        _clear()

        print(_header)
        println("\n\nСтатус: решение задачи генетическим алгоритмом")

        println()
        println("""
                    |Параметры задачи:

                    |${_controller.geneticProblemInfo}
                """.trimMargin() + "\n\n")

        _controller.geneticResultInfo = _controller.solveGenetic()
        _controller.geneticResultInfo.resultInfo.pathGraph.display().setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY)

        _clear()

        print(_header)
        println("\n\nСтатус: ожидание отклика от пользователя")
        println()
        println(
                """
                    |Параметры задачи:

                    |${_controller.geneticProblemInfo}
                """.trimMargin() + "\n\n"
        )

        print(  
                """
                    |Решение, полученное жадным алгоритмом:

                    |${_controller.greedyResultInfo}
                """.trimMargin() + "\n\n"
        )

        print(
                """
                    |Решение, полученное генетическим алгоритмом:

                    |${_controller.geneticResultInfo}
                """.trimMargin() + "\n\n"
        )

        _wait("Для продолжения нажмите клавишу ENTER . . .")

        _controller.changeScreen(EndScreen(_controller.geneticProblemInfo.problemInfo))
    }
}