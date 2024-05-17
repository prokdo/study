package ru.prokdo.view.screen

import java.math.BigInteger

import ru.prokdo.controller.screen.SignScreenController
import ru.prokdo.view.util.log.Logger
import ru.prokdo.model.crypto.rsa.key.Key

class SignScreen : Screen() {
    protected val _header = """
                                |Лабораторная работа №8 "Алгоритм цифровой подписи RSA"
                                |Автор: ВПР32, Прокопенко Дмитрий
                            """.trimMargin() + "\n\n"

    override protected val _log = Logger(_header)

    override protected val _controller = SignScreenController()

    override fun show() {
        val choice =    _askUInt(
                                    """
                                        |Для вычисления ЭЦП, необходимы данные о секретном ключе
                                        |Позволить системе сгенерировать ключ?

                                        |[ 1 ] Да, использовать случайный ключ
                                        |[ 2 ] Нет, произвести ввод секретного ключа

                                        |Выберите опцию на исполнение: 
                                    """.trimMargin()
                        )

        when (choice) {
            1 -> _controller.messageHash = _askMessageHash()
            
            2 -> { 
                _controller.secretKey = _askKey()
                _controller.messageHash = _askMessageHash()
            }
        }

        _showSignature()
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
                        |Ввод секретного ключа подписи

                        |Введите модуль (n): 
                    """.trimMargin())

        val n: BigInteger
        while (true) {
            _clear()

            print(_log)

            val input = readlnOrNull()

            val value = _controller.verifyBigInteger(input)
            if (value != null) {
                _log.append("$input\n")

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

        _log.append("Введите экспоненту секретного ключа (d): ")
        val d: BigInteger
        while (true) {
            _clear()

            print(_log)

            val input = readlnOrNull()

            val value = _controller.verifyBigInteger(input)
            if (value != null) {
                _log.clear()
                _log.append(_header)

                d = value
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

        return Key(d, n)
    }

    private fun _showSignature() {
        _clear()

        if (_controller.secretKey == null) _controller.generateKeys()

        _log.append(
                    """
                        |Секретный ключ:
                        |${_controller.secretKey!!.toString(true)}

                        |Хэш-сумма сообщения:
                        |${_controller.messageHash}

                        |Значение ЭЦП по алгоритму RSA:
                        |${_controller.calculateSignature()}

                        |Для продолжения нажмите клавишу ENTER. . .
                    """.trimMargin() + "\n\n"
            )

        print(_log)
        readln()
    }
}
