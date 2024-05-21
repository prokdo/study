package ru.prokdo.controller.screen

import java.io.File

import ru.prokdo.model.schedule.genetic.info.ResultInfo

class SaveScreenController(val resultInfo: ResultInfo) : ScreenController() {
    fun verifyUInt(strInt: String?): Int? {
        if (strInt == null) return null

        if (strInt.isEmpty() || strInt.isBlank()) return null

        try {
            val value = strInt.toInt()
            if (value < 0) return null

            return value
        } catch (exception: Exception) { return null }
    }

    fun verifyPath(path: String?): Boolean {
        if (path == null) return false

        if (path.isEmpty() || path.isBlank()) return false

        try { return File(path).exists() && File(path).isDirectory() } 
        catch (exception: Exception) { return false }
    }
}
