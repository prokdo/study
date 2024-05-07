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
        private val instance by lazy { Terminal() }

        operator fun invoke(): Terminal = Terminal.instance
    }
}