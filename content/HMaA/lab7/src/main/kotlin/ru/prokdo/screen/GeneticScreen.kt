package ru.prokdo.screen

import org.graphstream.ui.view.Viewer

import ru.prokdo.controller.GeneticScreenController
import ru.prokdo.model.tsp.info.ProblemInfo
import ru.prokdo.model.util.log.Logger

class GeneticScreen(problemInfo: ProblemInfo): Screen() {
    override val _controller = GeneticScreenController()

    init { _controller.geneticProblemInfo.problemInfo = problemInfo }

    override var _header =  
                            """
                                |Лабораторная работа №7 "Алгоритмы на графах, задача о коммивояжере"
                                |Автор: ВПР32, Прокопенко Дмитрий

                                |Статус: ввод характеристик генетического модуля

                                |${_controller.geneticProblemInfo.problemInfo}
                            """.trimMargin()

    override var _invalidInputMessage =
                            """
                                |Неправильный формат, повторите ввод
                                |Для продолжения нажмите клавишу ENTER . . .
                            """.trimMargin()

    override fun show() {
        _controller.geneticProblemInfo.problemInfo.matrixGraph.display().setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY)
        
        _log.append("$_header\n\n")
        _controller.geneticProblemInfo.individualsNumber = _askUInt("Количество особей в поколении (Z): ").toInt()
        _controller.geneticProblemInfo.limitNumber = _askUInt("Предельное число повтора лучшей особи (K): ").toInt()
        _controller.geneticProblemInfo.crossoverProbability = _askUInt("Вероятность кроссовера (Pc, %): ").toInt()
        _controller.geneticProblemInfo.mutationProbability = _askUInt("Вероятность мутации (Pm, %): ").toInt()

        _log.append("\n")
        Logger.path = _askString("Укажите директорию для сохранения отчета о работе генетического модуля: ")
        _wait("\nДля начала расчета решения задачи нажмите клавишу ENTER . . .")

        _controller.changeScreen(SolutionScreen(_controller.geneticProblemInfo))
    }

}