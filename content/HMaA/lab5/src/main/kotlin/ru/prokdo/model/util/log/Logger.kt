package ru.prokdo.model.util.log


class Logger {
    private val _builder: StringBuilder

    constructor() {
        this._builder = StringBuilder()
    }

    constructor(initialContent: String) {
        this._builder = StringBuilder(initialContent)
    }

    fun append(string: String) { this._builder.append(string) }
    
    fun clear() { this._builder.clear() }

    fun save(path: String) {  }

    override fun toString(): String {
        return this._builder.toString()
    }
}