package ru.prokdo.controller.screen

import ru.prokdo.controller.Terminal
import ru.prokdo.view.screen.Screen

abstract class ScreenController {
    fun changeScreen(screen: Screen) { Terminal().changeScreen(screen) }
}
