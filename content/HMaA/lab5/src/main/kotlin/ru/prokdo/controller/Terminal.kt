package ru.prokdo.controller


import ru.prokdo.model.util.log.Logger
import ru.prokdo.view.screen.Screen
import ru.prokdo.view.screen.StartScreen


class Terminal private constructor() {
    private val log = Logger()

    private var currentScreen: Screen = StartScreen()

    fun start() = this.currentScreen.show()

    fun changeScreen(screen: Screen) {
        this.currentScreen = screen

        this.currentScreen.show()
    }

    companion object {
        private var instance: Terminal? = null

        operator fun invoke(): Terminal {
            if (Terminal.instance == null) {
                Terminal.instance = Terminal()

                return Terminal.instance!!
            }

            return Terminal.instance!!
        }
    }
}