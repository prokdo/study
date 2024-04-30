package ru.prokdo.view.screen


import ru.prokdo.controller.screen.GeneticScreenController
import ru.prokdo.controller.Terminal
import ru.prokdo.model.util.info.ProblemInfo
import ru.prokdo.model.util.log.Logger


class GeneticScreen(problemInfo: ProblemInfo) : Screen() {
    private val log = Logger("""
                                |Лабораторная работа №5 "Теория однородных расписаний, генетическая модель Голдберга"
                                |Автор: ВПР32, Прокопенко Дмитрий

                                |Статус: ввод параметров модели Голдберга
                             """.trimMargin() + "\n\n")

    override protected val controller = GeneticScreenController(problemInfo)

    init {
        log.append("""
                        |Количество процессоров (M): ${controller.problemInfo.processorsNumber}
                        |Количество задач (N): ${controller.problemInfo.tasksNumber}
                        |Границы весов задач (T1, T2): ${controller.problemInfo.weightBounds.first}, ${controller.problemInfo.weightBounds.second}
                        |Список весов задач (T): [
                   """.trimMargin())
        controller.problemInfo.weightList.forEachIndexed { index, value -> run {
            log.append(value.toString())

            if (index != controller.problemInfo.weightList.size - 1) log.append(" ")
        } }
        log.append("]\n\n")
    }

    override fun show() {
        controller.problemInfo.individualsNumber = askUInt("Количество особей в поколении (Z): ")
        controller.problemInfo.limitNumber = askUInt("Предельное число повтора лучшей особи (K): ")
        controller.problemInfo.crossoverProbability = askUInt("Вероятность кроссовера (Pc, %): ")
        controller.problemInfo.mutationProbability = askUInt("Вероятность мутации (Pm, %): ")

        Terminal().changeScreen(SolutionScreen(controller.problemInfo))
    }

    private fun askUInt(prompt: String): Int {
        log.append(prompt)
        
        while (true) {
            clearTerminal()
            
            print(log)

            val input = readlnOrNull()

            val value = controller.verifyUInt(input)
            if (value != null) {
                log.append("${input}\n")

                return value
            }
            else { 
                println()
                print("""
                            |Неверный формат, необходим повторный ввод
                            |Для продолжения нажмите клавишу ENTER. . .
                      """.trimMargin())

                readLine()
                continue
             }
        }
    }
}