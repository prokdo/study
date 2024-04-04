package controller.screen

import view.terminal.TerminalCommand

class UserScreenController : ScreenController() {
    fun verifyCommand(command: String?): TerminalCommand? {
        return when (command) {
            "help" -> TerminalCommand.HELP
            "list" -> TerminalCommand.LIST
            "read" -> TerminalCommand.READ
            "write" -> TerminalCommand.WRITE
            "exit" -> TerminalCommand.EXIT
            else -> null
        }
    }
}