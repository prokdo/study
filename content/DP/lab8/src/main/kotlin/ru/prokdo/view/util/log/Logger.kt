package ru.prokdo.view.util.log

import java.io.File
import java.time.LocalDateTime

class Logger(initialContent: Any? = null) {
    private val _builder: StringBuilder

    init { 
        if (initialContent == null) this._builder = StringBuilder()
        else this._builder = StringBuilder(initialContent.toString()) 
    }

    fun append(obj: Any?) { if (obj != null) this._builder.append(obj.toString()) }

    fun clear() { this._builder.clear() }

    fun save(path: String): Boolean {
        val date = LocalDateTime.now().toString()

        try { File(path + "/$date.txt").writeText(_builder.toString()) } 
        catch (exception: Exception) { return false }

        return true
    }

    override fun toString(): String = this._builder.toString()
}
