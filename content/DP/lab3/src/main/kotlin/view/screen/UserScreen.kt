package view.screen

import controller.screen.UserScreenController
import view.terminal.Terminal
import view.terminal.TerminalCommand
import kotlin.system.exitProcess

class UserScreen : Screen() {
    override val controller = UserScreenController()

    private val inputLine: String = "${Terminal.controller.currentUser} > "

    override fun show() {
        println("=".repeat(100))

        println("Добро пожаловать, ${Terminal.controller.currentUser}")
        println()

        println("Для того, чтобы посмотреть список доступных команд, наберите команду help")

        println()

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