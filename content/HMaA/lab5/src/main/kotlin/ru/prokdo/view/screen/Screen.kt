package ru.prokdo.view.screen


import ru.prokdo.controller.Controller


abstract class Screen {
    abstract val controller: Controller

    abstract fun show()
}