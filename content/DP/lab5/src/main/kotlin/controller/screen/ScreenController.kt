package controller.screen


import view.terminal.Terminal
import view.screen.Screen


abstract class ScreenController {
    fun changeScreen(screen: Screen) = Terminal.changeScreen(screen)
}