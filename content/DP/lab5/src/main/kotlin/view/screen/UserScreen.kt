package view.screen


import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

import controller.screen.UserScreenController
import view.terminal.Terminal


class UserScreen : Screen() {
    override val controller = UserScreenController()

    override fun show() {
        println("Добро пожаловать, ${Terminal.controller.currentUser}")
        println()

        var input: String?
        while (true) {
            println("Доступные действия: ")
            println()

            println("[ 1 ] Посмотреть информацию о себе")
            println("[ 2 ] Выйти из сессии пользователя")
            println("[ 3 ] Завершение работы программы")
            println()

            print("Выберите опцию на исполнение: ")

            val time = measureTimeMillis { input = readlnOrNull() }.toDouble() / 1000
            println()

            if (time > 300) {
                println("Достигнут предел времени бездействия пользователя")
                println("Завершение сеанса пользователя . . .")
                println()

                Terminal.controller.currentUser = null
                Terminal.changeScreen(StartScreen())
            }

            if (!this.controller.verifyInput(input)) {
                println("Неверный формат ввода. Повторите ввод")
                println()

                continue
            }

            when (input) {
                "1" -> {
                    println("Запрос информации о пользователе ${Terminal.controller.currentUser} . . .")
                    println()

                    println(this.controller.getUserInfo())
                    println()
                }

                "2" -> {
                    println("Завершение сеанса пользователя . . .")
                    println()

                    Terminal.controller.currentUser = null
                    Terminal.changeScreen(StartScreen())
                }

                "3" -> {
                    println("Завершение работы . . .")
                    println()

                    exitProcess(0)
                }
            }
        }
    }
}