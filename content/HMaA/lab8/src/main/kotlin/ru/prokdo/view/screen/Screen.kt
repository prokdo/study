package ru.prokdo.view.screen

import ru.prokdo.controller.screen.ScreenController

abstract class Screen {
    abstract protected val _controller: ScreenController

    protected fun _clearTerminal(): Boolean {
        try {
            val os = System.getProperty("os.name")

            val builder: ProcessBuilder
            if (os.contains("Windows")) builder = ProcessBuilder("cmd", "/c", "cls")
            else builder = ProcessBuilder("clear")

            builder.inheritIO().start().waitFor()

            return true
        } catch (exception: Exception) { return false }
    }

    init { this._clearTerminal() }

    abstract fun show()
}
