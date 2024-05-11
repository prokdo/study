package ru.prokdo.view.screen

import ru.prokdo.controller.Terminal
import ru.prokdo.controller.screen.SaveScreenController
import ru.prokdo.model.schedule.genetic.info.ResultInfo
import ru.prokdo.model.util.log.Logger

class SaveScreen(resultInfo: ResultInfo) : Screen() {
    override val _controller = SaveScreenController(resultInfo)

    private val _log =  Logger(
                                """
                                    |Лабораторная работа №6 "Теория разнородных расписаний, генетическая модель Холланда"
                                    |Автор: ВПР32, Прокопенко Дмитрий

                                    |Статус: демонстрация краткого решения задачи

                                    |${_controller.resultInfo}
                                """.trimMargin() + "\n\n"
                        )

    override fun show() {
        _showInfoFrame()
        _showSaveDialog()

        Terminal().changeScreen(EndScreen(_controller.resultInfo))
    }

    private fun _showInfoFrame() {
        print(_log)
        print("Для продолжения нажмите клавишу ENTER. . .")

        readLine()
        _log.clear()
        _clearTerminal()
    }

    private fun _showSaveDialog() {
        _log.append(
                    """
                        |Лабораторная работа №6 "Теория разнородных расписаний, генетическая модель Холланда"
                        |Автор: ВПР32, Прокопенко Дмитрий

                        |Статус: ожидание завершения сеанса

                        |${_controller.resultInfo}
                    """.trimMargin() + "\n\n"
        )

        val tempLog =   Logger(
                                """
                                    |Вы хотите сохранить отчет о работе программы?

                                    |[ 1 ] Да
                                    |[ 2 ] Нет

                                    |Выберите опцию: 
                                """.trimMargin()
                        )

        while (true) {
            _clearTerminal()

            print(_log)
            print(tempLog)

            val choice = _controller.verifyUInt(readlnOrNull())
            when (choice) {
                1 -> {
                    tempLog.append(choice.toString())
                    _log.append("${tempLog.toString()}\n\n")

                    tempLog.clear()

                    if (_showPathDialog()) break else continue
                }

                2 -> {
                    println()
                    print("Для продолжения намжите клавишу ENTER. . . ")

                    readLine()
                    break
                }

                else -> {
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

    private fun _showPathDialog(): Boolean {
        _log.append("Укажите путь до директории для сохранения отчета: ")

        while (true) {
            _clearTerminal()

            print(_log)

            val input = readlnOrNull()
            if (_controller.verifyPath(input)) {
                val saveLog = Logger("${_controller.resultInfo.problemInfo.toString()}\n\n")
                saveLog.append(_controller.resultInfo.solverLog)
                saveLog.append(_controller.resultInfo.toString())

                if (!saveLog.save(input!!)) {
                    println()
                    print(
                            """
                                |Во время сохранения отчета произошла ошибка
                                |Для продолжения нажмите клавишу ENTER. . .
                            """.trimMargin()
                    )

                    readLine()
                    return false
                }

                println()
                print(
                        """
                            |Отчет успешно сохранен по указанному пути
                            |Для продолжения нажмите клавишу ENTER. . .
                        """.trimMargin()
                )

                readLine()
                return true
            } else {
                println()
                print(
                        """
                            |Неверный формат, необходим повторный ввод
                            |Для продолжения нажмите клавишу ENTER. . .
                        """.trimMargin()
                )

                readLine()
                return false
            }
        }
    }
}
