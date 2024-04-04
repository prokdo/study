package view.screen

import controller.screen.ScreenController

abstract class Screen {
    protected abstract val controller: ScreenController

    abstract fun show()
}