package controller.terminal

import model.database.DataBase
import model.util.TaskInfo
import view.screen.Screen
import view.screen.StartScreen

class TerminalController {
    val dataBase = DataBase(TaskInfo.DATABASE_PATH, TaskInfo.RESOURCE_PATH)

    var currentUser: String? = null

    private var currentScreen: Screen? = null

    fun launch() {
        this.currentScreen = StartScreen()

        this.currentScreen!!.show()
    }

    fun changeScreen(screen: Screen) {
        this.currentScreen = screen

        this.currentScreen!!.show()
    }
}