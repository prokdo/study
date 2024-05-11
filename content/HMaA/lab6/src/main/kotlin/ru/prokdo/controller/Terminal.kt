package ru.prokdo.controller

import ru.prokdo.model.util.log.Logger
import ru.prokdo.view.screen.Screen
import ru.prokdo.view.screen.StartScreen

class Terminal private constructor() {
    private val _log = Logger()

    private var _currentScreen: Screen = StartScreen()

    fun start() = this._currentScreen.show()

    fun changeScreen(screen: Screen) {
        this._currentScreen = screen

        this._currentScreen.show()
    }

    companion object {
        private val instance by lazy { Terminal() }

        operator fun invoke(): Terminal = Terminal.instance
    }
}
