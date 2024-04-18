package controller.screen


import kotlin.random.Random

import model.auth.Authentificator
import model.auth.AuthSignal
import model.database.entity.EntityType
import model.database.entity.Phrase
import view.terminal.Terminal


class RegistrationScreenController : ScreenController() {
    private val authentificator = Authentificator(Terminal.controller.dataBase)

    private val phrase: Phrase = Terminal.controller.dataBase.
        getEntityById(EntityType.PHRASE, Random.nextInt(Terminal.controller.dataBase.phrasesNumber)) as Phrase

    fun getAuthMeasurementssNumber(): Int = Authentificator.REGISTRATION_MEASUREMENTS_NUMBER

    fun getPhraseContent(): String = phrase.content

    fun verifyInput(input: String?): Boolean {
        if (input == null) return false

        if (input.isBlank() || input.isEmpty()) return false

        return input == phrase.content
    }

    fun verifyUserName(userName: String?): Boolean {
        if (userName == null) return false

        if (userName.isBlank() || userName.isEmpty()) return false

        return Terminal.controller.dataBase.getEntityByName(EntityType.USER, userName) == null
    }

    fun tryRegistrate(userName: String, timeArray: Array<Double>): Boolean {
        return this.authentificator.
            registrate(userName, this.phrase.id, timeArray) == AuthSignal.SUCCESS
    }
}