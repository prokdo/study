package controller.terminal


import model.database.DataBase
import view.screen.Screen
import view.screen.StartScreen


class TerminalController {
    val dataBase = DataBase("/home/prokdo/Code/DP/lab5/rsrc/database")

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