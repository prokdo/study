package view.terminal

import controller.terminal.TerminalController
import view.screen.Screen

object Terminal {
    val controller = TerminalController()

    fun launch() = this.controller.launch()

    fun changeScreen(screen: Screen) {
        this.controller.changeScreen(screen)
    }
}