package ru.prokdo.view.screen

import ru.prokdo.controller.screen.ScreenController
import ru.prokdo.view.util.log.Logger

abstract class Screen {
    abstract protected val _controller: ScreenController

    abstract protected val _log: Logger

    protected fun _clear(): Boolean {
        try {
            val os = System.getProperty("os.name")

            val builder: ProcessBuilder
            if (os.contains("Windows")) builder = ProcessBuilder("cmd", "/c", "cls")
            else builder = ProcessBuilder("clear")

            builder.inheritIO().start().waitFor()

            return true
        } catch (exception: Exception) { return false }
    }

    init { this._clear() }

    abstract fun show()
}
