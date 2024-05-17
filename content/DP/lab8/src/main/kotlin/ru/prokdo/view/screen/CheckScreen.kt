package ru.prokdo.view.screen

import java.math.BigInteger

import ru.prokdo.controller.screen.CheckScreenController
import ru.prokdo.view.util.log.Logger
import ru.prokdo.model.crypto.rsa.key.Key

class CheckScreen : Screen() {
    protected val _header = """
                                |Лабораторная работа №8 "Алгоритм цифровой подписи RSA"
                                |Автор: ВПР32, Прокопенко Дмитрий
                            """.trimMargin() + "\n\n"

    override protected val _log = Logger(_header)

    override protected val _controller = CheckScreenController()

    override fun show() {
        _controller.publicKey = _askKey()
        _controller.messageHash = _askMessageHash()
        _controller.signature = _askSignature()

        _showCheckResult()
        _controller.changeScreen(StartScreen())
    }

    private fun _askUInt(prompt: String): Int {
        _log.append(prompt)

        while (true) {
            _clear()

            print(_log)

            val input = readlnOrNull()

            val value = _controller.verifyUInt(input)
            if (value != null) {
                _log.clear()
                _log.append(_header)

                return value
            } else {
                println()
                print(
                        """
                            |Неверный формат, необходим повторный ввод
                            |Для продолжения нажмите клавишу ENTER. . .
                        """.trimMargin()
                )

                readLine()
                continue
            }
        }
    }

    private fun _askMessageHash(): BigInteger {
        _log.append("Введите хэш-сумму сообщения: ")

        while (true) {
            _clear()

            print(_log)

            val input = readlnOrNull()

            val value = _controller.verifyBigInteger(input)
            if (value != null) {
                _log.clear()
                _log.append(_header)

                return value
            } else {
                println()
                print(
                        """
                            |Неверный формат, необходим повторный ввод
                            |Для продолжения нажмите клавишу ENTER. . .
                        """.trimMargin()
                )

                readLine()
                continue
            }
        }
    }

    private fun _askKey(): Key {
        _log.append(   
                    """
                        |Ввод открытого ключа подписи

                        |Введите модуль (n): 
                    """.trimMargin())

        val n: BigInteger
        while (true) {
            _clear()

            print(_log)

            val input = readlnOrNull()

            val value = _controller.verifyBigInteger(input)
            if (value != null) {
                _log.clear()
                _log.append(_header)

                n = value
                break
            } else {
                println()
                print(
                        """
                            |Неверный формат, необходим повторный ввод
                            |Для продолжения нажмите клавишу ENTER. . .
                        """.trimMargin()
                )

                readLine()
                continue
            }
        }

        _log.append("Введите экспоненту открытого ключа (e): ")
        val e: BigInteger
        while (true) {
            _clear()

            print(_log)

            val input = readlnOrNull()

            val value = _controller.verifyBigInteger(input)
            if (value != null) {
                _log.clear()
                _log.append(_header)

                e = value
                break
            } else {
                println()
                print(
                        """
                            |Неверный формат, необходим повторный ввод
                            |Для продолжения нажмите клавишу ENTER. . .
                        """.trimMargin()
                )

                readLine()
                continue
            }
        }

        return Key(e, n)
    }

    private fun _askSignature(): BigInteger {
        _log.append("Введите ЭЦП сообщения: ")

        while (true) {
            _clear()

            print(_log)

            val input = readlnOrNull()

            val value = _controller.verifyBigInteger(input)
            if (value != null) {
                _log.clear()
                _log.append(_header)

                return value
            } else {
                println()
                print(
                        """
                            |Неверный формат, необходим повторный ввод
                            |Для продолжения нажмите клавишу ENTER. . .
                        """.trimMargin()
                )

                readLine()
                continue
            }
        }
    }

    private fun _showCheckResult() {
        _clear()

        _log.append(
                    """
                        |Открытый ключ:
                        |${_controller.publicKey!!.toString(true)}

                        |Хэш-сумма сообщения:
                        |${_controller.messageHash}

                        |Значение ЭЦП по алгоритму RSA:
                        |${_controller.signature}

                        |Результат проверки: ${
                            if (_controller.verifySignature()) "подпись подлинна"
                            else "подпись фальсифицирована"
                        }

                        |Для продолжения нажмите клавишу ENTER. . .
                    """.trimMargin() + "\n\n"
            )

        print(_log)
        readln()
    }
}
