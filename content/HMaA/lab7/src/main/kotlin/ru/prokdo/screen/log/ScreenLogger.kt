package ru.prokdo.screen.log

class ScreenLogger(initialContent: Any? = null) {
    private val _builder: StringBuilder

    init { 
        if (initialContent == null) this._builder = StringBuilder()
        else this._builder = StringBuilder(initialContent.toString()) 
    }

    fun append(obj: Any?) { if (obj != null) this._builder.append(obj.toString()) }

    fun clear() { this._builder.clear() }

    override fun toString() = this._builder.toString()
}