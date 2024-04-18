package view.screen


import kotlin.system.exitProcess

import controller.screen.UserScreenController
import view.terminal.Terminal


class UserScreen : Screen() {
    override val controller = UserScreenController()

    override fun show() {
        println("Добро пожаловать, ${Terminal.controller.currentUser}")
        println()

        println("Выберите опцию на исполнение: ")
        println()

        println("[ 1 ] Посмотреть информацию о себе")
        println("[ 2 ] Выйти из сессии пользователя")
        println("[ 3 ] Завершение работы программы")

        var command: TerminalCommand?
        while (true) {
            print(this.inputLine)

            val strCommand = readlnOrNull()

            command = this.controller.verifyCommand(strCommand)

            when (command) {
                null -> {
                    println("Неверный формат ввода. Повторите ввод")
                    println()

                    continue
                }
                else -> break
            }
        }

        when(command!!) {
            TerminalCommand.HELP -> TODO()
            TerminalCommand.LIST -> TODO()
            TerminalCommand.READ -> TODO()
            TerminalCommand.WRITE -> TODO()
            TerminalCommand.EXIT -> TODO()
        }
    }
}