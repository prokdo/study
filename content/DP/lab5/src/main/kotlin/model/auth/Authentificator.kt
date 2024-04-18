package model.auth


import kotlin.math.abs

import model.database.DataBase
import model.database.entity.EntityType
import model.database.entity.User


class Authentificator(private val dataBase: DataBase) {
    companion object {
        const val EPSILON = 1.00
        const val REGISTRATION_MEASUREMENTS_NUMBER  = 4
    }

    fun login(
                name: String,
                time: Double): AuthSignal {
        
        val user = dataBase.getEntityByName(EntityType.USER, name) as User?

        if (user == null) return AuthSignal.DENIED

        if (abs(user.accessValue - time) >= Authentificator.EPSILON) return AuthSignal.DENIED

        return AuthSignal.SUCCESS
    }

    fun registrate(
                    name: String,
                    phraseId: Int,
                    timeArray: Array<Double>): AuthSignal {
        
        if (dataBase.getEntityByName(EntityType.USER, name) != null) return AuthSignal.DENIED
        
        if (!dataBase.addEntity(User(name, phraseId, timeArray.average()))) return AuthSignal.DENIED
        
        return AuthSignal.SUCCESS
    }
}