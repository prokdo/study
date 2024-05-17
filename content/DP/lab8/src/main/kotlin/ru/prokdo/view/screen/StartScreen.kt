package ru.prokdo.view.screen

import ru.prokdo.controller.screen.StartScreenController
import ru.prokdo.view.util.log.Logger
import kotlin.system.exitProcess

class StartScreen : Screen() {
    override protected val _log =   Logger(
                                            """
                                                |Лабораторная работа №8 "Алгоритм цифровой подписи RSA"
                                                |Автор: ВПР32, Прокопенко Дмитрий
                                            """.trimMargin() + "\n\n"
                                    )

    override protected val _controller = StartScreenController()

    override fun show() {
        val choice =    _askUInt(
                                    """
                                        |Доступные режимы работы программы:

                                        |[ 1 ] Вычисление значения ЭЦП
                                        |[ 2 ] Проверка подлинности ЭЦП
                                        |[ 3 ] Завершение работы

                                        |Выберите опцию на исполнение: 
                                    """.trimMargin()
                        )

        when (choice) {
            1 -> _controller.changeScreen(SignScreen())
            
            2 -> _controller.changeScreen(CheckScreen())

            3 -> { 
                println("Завершение работы . . .")
                exitProcess(0) 
            }
        }
    }

    private fun _askUInt(prompt: String): Int {
        _log.append(prompt)

        while (true) {
            _clear()

            print(_log)

            val input = readlnOrNull()

            val value = _controller.verifyUInt(input)
            if (value != null) {
                _log.append("$input\n")

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
}
