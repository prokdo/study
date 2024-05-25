package ru.prokdo.model.util.log

import java.io.File
import java.time.LocalDateTime

class Logger {
    private val _date = LocalDateTime.now()

    operator fun invoke(text: String?): Boolean {
        if (text.isNullOrBlank() || text.isNullOrEmpty()) return false

        val file = File("$path/$_date.txt")
        if (!file.exists()) {
            try { file.writeText(text) }
            catch (_: Exception) { return false }
        } else
            try { file.appendText(text)} 
            catch (_: Exception) { return false }

        return true
    }

    companion object {
        var path = "/home/prokdo/Code/HMaA/lab7/log"
            set(value: String) {
                if (!value.isBlank() && !value.isEmpty()) field = value
            }
    }
}
