package controller.screen


import view.terminal.Terminal
import model.database.entity.EntityType


class UserScreenController : ScreenController() {
    fun verifyInput(input: String?): Boolean {
        if (input == null) return false

        if (input.isBlank() || input.isEmpty()) return false

        return when (input) {
            "1", "2", "3" -> true
            else -> false
        }
    }

    fun getUserInfo(): String {
        return Terminal.controller.dataBase.getEntityByName(
                                                            EntityType.USER, 
                                                            Terminal.controller.currentUser!!
                                                            ).toString()
    }
}