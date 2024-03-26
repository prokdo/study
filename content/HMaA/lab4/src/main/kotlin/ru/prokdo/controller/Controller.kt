package ru.prokdo.controller


import ru.prokdo.view.Terminal
import ru.prokdo.view.screen.Screen


abstract class Controller {
    fun changeScreen(screen: Screen) {
        Terminal.setScreen(screen)
    }
}