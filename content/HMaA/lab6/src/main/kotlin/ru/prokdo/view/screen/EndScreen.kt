package ru.prokdo.view.screen

import kotlin.system.exitProcess

import ru.prokdo.model.util.info.ResultInfo
import ru.prokdo.controller.screen.EndScreenController
import ru.prokdo.controller.Terminal

class EndScreen(resultInfo: ResultInfo) : Screen() {
    override val controller = EndScreenController(resultInfo)

    override fun show() { 
        val isGoingRestart: Boolean
        while (true) {
            clearTerminal()

            showInfoFrame()
            showExitDialog()

            val choice = readlnOrNull()
            when (controller.verifyUInt(choice)) {
                1 -> { isGoingRestart = true; break }

                2 -> { print("\nЗавершение работы . . ."); isGoingRestart = false; break }

                else -> {
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

        if (!isGoingRestart) exitProcess(0)

        Terminal().changeScreen(GeneticScreen(controller.resultInfo.problemInfo))
    }

    private fun showInfoFrame() {
        print("""
                    |Лабораторная работа №5 "Теория однородных расписаний, генетическая модель Голдберга"
                    |Автор: ВПР32, Прокопенко Дмитрий

                    |Статус: ожидание завершение сеанса
              """.trimMargin() + "\n\n")

        println(controller.resultInfo)
        println()
    }
    
    private fun showExitDialog() {
        print("""
                    |Каким образом завершить текущий сеанс расчета?

                    |[ 1 ] Вернуться в меню задания характеристик генетической модели Голдберга
                    |[ 2 ] Завершение работы программы

                    |Выберите опцию: 
              """.trimMargin())
    }
}