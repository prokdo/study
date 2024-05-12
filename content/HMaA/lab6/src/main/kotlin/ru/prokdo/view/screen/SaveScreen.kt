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
    
    private val _tempLog = Logger()

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

        while (true) {
            _clearTerminal()

            _tempLog.append(
                            """
                                |Вы хотите сохранить отчет о работе программы?

                                |[ 1 ] Да
                                |[ 2 ] Нет

                                |Выберите опцию: 
                            """.trimMargin()
            )

            print(_log)
            print(_tempLog)

            val choice = _controller.verifyUInt(readlnOrNull())
            when (choice) {
                1 -> {
                    _tempLog.append(choice)

                    if (_showPathDialog()) { 
                        _log.append("$_tempLog\n\n")
                        _tempLog.clear()
                        break
                    }

                        _tempLog.clear()
                        continue
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
        _tempLog.append("\n\nУкажите путь до директории для сохранения отчета: ")

        while (true) {
            _clearTerminal()

            print(_log)
            print(_tempLog)

            val input = readlnOrNull()
            if (_controller.verifyPath(input)) {
                val saveLog = Logger("${_controller.resultInfo.problemInfo.toString()}\n\n")
                saveLog.append(_controller.resultInfo.solverLog)
                saveLog.append(_controller.resultInfo)

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
