package ru.prokdo.model.util.log


import java.io.File
import java.time.LocalDateTime


class Logger {
    private val builder: StringBuilder

    constructor() {
        this.builder = StringBuilder()
    }

    constructor(initialContent: String) {
        this.builder = StringBuilder(initialContent)
    }

    fun append(string: String) { this.builder.append(string) }

    fun clear() { this.builder.clear() }

    fun save(path: String): Boolean {
        val date = LocalDateTime.now().toString()

        try { 
            File(path + "/$date.txt").writeText(builder.toString())
        }
        catch (exception: Exception) { return false }

        return true
    }

    override fun toString(): String {
        return this.builder.toString()
    }
}