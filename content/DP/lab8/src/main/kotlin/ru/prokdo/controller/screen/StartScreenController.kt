package ru.prokdo.controller.screen

class StartScreenController : ScreenController() {
    fun verifyUInt(strInt: String?): Int? {
        if (strInt == null) return null

        if (strInt.isEmpty() || strInt.isBlank()) return null

        try {
            val value = strInt.toInt()
            if (value < 0) return null

            return value
        } catch (exception: Exception) { return null }
    }
}
