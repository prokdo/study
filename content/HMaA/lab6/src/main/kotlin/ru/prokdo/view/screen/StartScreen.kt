package ru.prokdo.view.screen

import ru.prokdo.controller.screen.StartScreenController
import ru.prokdo.controller.Terminal
import ru.prokdo.model.util.log.Logger

class StartScreen : Screen() {
    private val log = Logger("""
                                |Лабораторная работа №5 "Теория однородных расписаний, генетическая модель Голдберга"
                                |Автор: ВПР32, Прокопенко Дмитрий

                                |Статус: ввод характеристик задачи
                             """.trimMargin() + "\n\n")

    override protected val controller = StartScreenController()

    override fun show() {
        controller.problemInfo.processorsNumber = askUInt("Количество процессоров (M): ")
        controller.problemInfo.tasksNumber = askUInt("Количество задач (N): ")
        controller.problemInfo.weightBounds = askUIntPair("Границы весов задач (T1, T2): ")
        controller.problemInfo.weightMatrix = controller.generateWeightMatrix()

        Terminal().changeScreen(GeneticScreen(controller.problemInfo))
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

    private fun askUIntPair(prompt: String): Pair<Int, Int> {
        log.append(prompt)

        while (true) {
            clearTerminal()
            
            print(log)

            val input = readlnOrNull()

            val value = controller.verifyUIntPair(input)
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