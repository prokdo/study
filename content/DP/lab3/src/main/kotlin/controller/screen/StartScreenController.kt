package controller.screen

import model.database.entity.EntityType
import view.terminal.Terminal

class StartScreenController : ScreenController() {
    fun verifyUserName(userName: String?): Boolean? {
        if (userName == null) return false

        if (userName == "exit") return null

        if (Terminal.controller.dataBase.getEntityByName(EntityType.USER, userName) == null) return false

        return true
    }
}