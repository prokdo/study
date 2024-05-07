package ru.prokdo.controller.screen

import ru.prokdo.model.util.info.ProblemInfo

class GeneticScreenController(val problemInfo: ProblemInfo) : ScreenController() {
    fun verifyUInt(strInt: String?): Int? {
        if (strInt == null) return null

        if (strInt.isEmpty() || strInt.isBlank()) return null

        try {
            val value = strInt.toInt()
            if (value < 0) return null

            return value
        } catch (exception: Exception) {
            return null
        }
    }
}
