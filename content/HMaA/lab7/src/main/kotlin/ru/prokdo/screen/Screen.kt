package ru.prokdo.screen

import java.math.BigDecimal
import java.math.BigInteger

import ru.prokdo.controller.ScreenController
import ru.prokdo.screen.log.ScreenLogger

abstract class Screen {
    abstract protected val _controller: ScreenController

    protected val _log = ScreenLogger()

    open protected var _invalidInputMessage = 
                                                """
                                                    |Invalid input. Please, try again
                                                    |Press ENTER to continue . . .
                                                """.trimMargin()

    open protected var _header =
                                """
                                    |KotlinCLUI v0.0.1
                                    |Author: Prokopenko Dmitry
                                """.trimMargin()

    protected fun _clear(): Boolean {
        try {
            val os = System.getProperty("os.name")

            val builder: ProcessBuilder
            if (os.contains("Windows")) builder = ProcessBuilder("cmd", "/c", "cls")
            else builder = ProcessBuilder("clear")

            builder.inheritIO().start().waitFor()

            return true
        } 
        catch (_: Exception) { return false }
    }

    protected fun _wait(prompt: String? = null) {
        if (prompt != null) print(prompt)
        readlnOrNull()
    }

    protected fun _showInvalidInputMessage() {
        print(_invalidInputMessage)
        _wait()
    }

    init { this._clear() }

    abstract fun show()

    protected fun _askString(prompt: String? = null): String {
        if (prompt != null) _log.append(prompt)

        while (true) {
            _clear()
            print(_log)

            val input = readlnOrNull()
            if (!_controller.verifyString(input)) {
                println()
                _showInvalidInputMessage()

                continue
            }

            _log.append("$input\n")
            _clear()
            print(_log)
            return input!!
        }
    }

    protected fun _askUInt(prompt: String? = null): UInt {
        if (prompt != null) _log.append(prompt)

        while (true) {
            _clear()
            print(_log)

            val value = _controller.verifyUInt(readlnOrNull())
            when (value) {
                null -> {
                    println()
                    _showInvalidInputMessage()

                    continue
                }

                else -> {
                    _log.append("$value\n")
                    _clear()
                    print(_log)
                    return value
                }
            }
        }
    }

    protected fun _askInt(prompt: String? = null): Int {
        if (prompt != null) _log.append(prompt)

        while (true) {
            _clear()
            print(_log)

            val value = _controller.verifyInt(readlnOrNull())
            when (value) {
                null -> {
                    println()
                    _showInvalidInputMessage()

                    continue
                }

                else -> {
                    _log.append("$value\n")
                    _clear()
                    print(_log)
                    return value
                }
            }
        }
    }

    protected fun _askDouble(prompt: String? = null): Double {
        if (prompt != null) _log.append(prompt)

        while (true) {
            _clear()
            print(_log)

            val value = _controller.verifyDouble(readlnOrNull())
            when (value) {
                null -> {
                    println()
                    _showInvalidInputMessage()

                    continue
                }

                else -> {
                    _log.append("$value\n")
                    _clear()
                    print(_log)
                    return value
                }
            }
        }
    }

    protected fun _askFloat(prompt: String? = null): Float {
        if (prompt != null) _log.append(prompt)

        while (true) {
            _clear()
            print(_log)

            val value = _controller.verifyFloat(readlnOrNull())
            when (value) {
                null -> {
                    println()
                    _showInvalidInputMessage()

                    continue
                }

                else -> {
                    _log.append("$value\n")
                    _clear()
                    print(_log)
                    return value
                }
            }
        }
    }

    protected fun _askBigDecimal(prompt: String? = null): BigDecimal {
        if (prompt != null) _log.append(prompt)

        while (true) {
            _clear()
            print(_log)

            val value = _controller.verifyBigDecimal(readlnOrNull())
            when (value) {
                null -> {
                    println()
                    _showInvalidInputMessage()

                    continue
                }

                else -> {
                    _log.append("$value\n")
                    _clear()
                    print(_log)
                    return value
                }
            }
        }
    }

    protected fun _askBigInteger(prompt: String? = null): BigInteger {
        if (prompt != null) _log.append(prompt)

        while (true) {
            _clear()
            print(_log)

            val value = _controller.verifyBigInteger(readlnOrNull())
            when (value) {
                null -> {
                    println()
                    _showInvalidInputMessage()

                    continue
                }

                else -> {
                    _log.append("$value\n")
                    _clear()
                    print(_log)
                    return value
                }
            }
        }
    }

    protected fun _askStringPair(prompt: String? = null): Pair<String, String> {
        if (prompt != null) _log.append(prompt)

        while (true) {
            _clear()
            print(_log)

            val value = _controller.verifyStringPair(readlnOrNull())
            when (value) {
                null -> {
                    println()
                    _showInvalidInputMessage()

                    continue
                }

                else -> {
                    _log.append("${value.first}, ${value.second}\n")
                    _clear()
                    print(_log)
                    return value
                }
            }
        }
    }

    protected fun _askUIntPair(prompt: String? = null): Pair<UInt, UInt> {
        if (prompt != null) _log.append(prompt)

        while (true) {
            _clear()
            print(_log)

            val value = _controller.verifyUIntPair(readlnOrNull())
            when (value) {
                null -> {
                    println()
                    _showInvalidInputMessage()

                    continue
                }

                else -> {
                    _log.append("${value.first}, ${value.second}\n")
                    _clear()
                    print(_log)
                    return value
                }
            }
        }
    }

    protected fun _askIntPair(prompt: String? = null): Pair<Int, Int> {
        if (prompt != null) _log.append(prompt)

        while (true) {
            _clear()
            print(_log)

            val value = _controller.verifyIntPair(readlnOrNull())
            when (value) {
                null -> {
                    println()
                    _showInvalidInputMessage()

                    continue
                }

                else -> {
                    _log.append("${value.first}, ${value.second}\n")
                    _clear()
                    print(_log)
                    return value
                }
            }
        }
    }

    protected fun _askDoublePair(prompt: String? = null): Pair<Double, Double> {
        if (prompt != null) _log.append(prompt)

        while (true) {
            _clear()
            print(_log)

            val value = _controller.verifyDoublePair(readlnOrNull())
            when (value) {
                null -> {
                    println()
                    _showInvalidInputMessage()

                    continue
                }

                else -> {
                    _log.append("${value.first}, ${value.second}\n")
                    _clear()
                    print(_log)
                    return value
                }
            }
        }
    }

    protected fun _askFloatPair(prompt: String? = null): Pair<Float, Float> {
        if (prompt != null) _log.append(prompt)

        while (true) {
            _clear()
            print(_log)

            val value = _controller.verifyFloatPair(readlnOrNull())
            when (value) {
                null -> {
                    println()
                    _showInvalidInputMessage()

                    continue
                }

                else -> {
                    _log.append("${value.first}, ${value.second}\n")
                    _clear()
                    print(_log)
                    return value
                }
            }
        }
    }
}
