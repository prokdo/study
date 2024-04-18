package controller.screen


import kotlin.random.Random

import model.auth.Authentificator
import model.auth.AuthSignal
import model.database.entity.EntityType
import model.database.entity.User
import model.database.entity.Phrase
import view.terminal.Terminal


class LoginScreenController : ScreenController() {
    private val authentificator = Authentificator(Terminal.controller.dataBase)

    private var user: User? = null
    private var phrase: Phrase? = null

    fun checkSystemHasUsers(): Boolean = Terminal.controller.dataBase.usersNumber != 0

    fun verifyUserName(userName: String?): Boolean {
        if (userName == null) return false

        if (userName.isBlank() || userName.isEmpty()) return false

        return Terminal.controller.dataBase.getEntityByName(EntityType.USER, userName) != null
    }

    fun getPhraseContent(userName: String): String {
        this.user = Terminal.controller.dataBase.getEntityByName(EntityType.USER, userName) as User

        this.phrase = Terminal.controller.dataBase.getEntityById(EntityType.PHRASE, this.user!!.phraseId) as Phrase

        return this.phrase!!.content
    }

    fun verifyInput(input: String?): Boolean {
        if (input == null) return false

        if (input.isBlank() || input.isEmpty()) return false

        return input == phrase!!.content
    }

    fun tryLogin(userName: String, time: Double): Boolean {
        if (this.authentificator.login(userName, time) != AuthSignal.SUCCESS) return false

        Terminal.controller.currentUser = userName

        return true
    }
}