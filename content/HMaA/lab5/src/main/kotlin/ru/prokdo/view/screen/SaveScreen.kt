package ru.prokdo.view.screen


import ru.prokdo.model.util.info.ResultInfo
import ru.prokdo.model.util.log.Logger
import ru.prokdo.controller.Terminal
import ru.prokdo.controller.screen.SaveScreenController


class SaveScreen(resultInfo: ResultInfo) : Screen() {
    override val controller = SaveScreenController(resultInfo)
    
    private val log = Logger("""
                                |Лабораторная работа №5 "Теория однородных расписаний, генетическая модель Голдберга"
                                |Автор: ВПР32, Прокопенко Дмитрий

                                |Статус: демонстрация краткого решения задачи

                                |${controller.resultInfo}
                             """.trimMargin() + "\n\n")

    override fun show() {
        showInfoFrame()
        showSaveDialog()

        Terminal().changeScreen(EndScreen(controller.resultInfo))
    }

    private fun showInfoFrame() {
        print(log)
        print("Для продолжения нажмите клавишу ENTER. . .")

        readLine()
        log.clear()
        clearTerminal()
    }

    private fun showSaveDialog() {
        log.append("""
                        |Лабораторная работа №5 "Теория однородных расписаний, генетическая модель Голдберга"
                        |Автор: ВПР32, Прокопенко Дмитрий

                        |Статус: ожидание завершения сеанса

                        |${controller.resultInfo}
                   """.trimMargin() + "\n\n")

        val tempLog = Logger("""
                                |Вы хотите сохранить отчет о работе программы?

                                |[ 1 ] Да
                                |[ 2 ] Нет

                                |Выберите опцию: 
                             """.trimMargin())

        while (true) {
            clearTerminal()

            print(log)
            print(tempLog)

            val choice = controller.verifyUInt(readlnOrNull())
            when (choice) {
                1 -> {
                    tempLog.append(choice.toString())
                    log.append("${tempLog.toString()}\n\n")

                    tempLog.clear()

                    if (showPathDialog()) break
                    else continue
                }

                2 -> {
                    println()
                    print("Для продолжения намжите клавишу ENTER. . . ")

                    readLine()
                    break
                }

                else -> {
                    println()
                    print("""
                                |Неверный формат, необходим повторный ввод
                                |Для продолжения нажмите клавишу ENTER. . .
                          """.trimMargin())

                    readLine()
                    continue
                }
            }
        }
    }

    private fun showPathDialog(): Boolean {
        log.append("Укажите путь до директории для сохранения отчета: ")

        while (true) {
            clearTerminal()

            print(log)

            val input = readlnOrNull()
            if (controller.verifyPath(input)) {
                val saveLog = Logger("${controller.resultInfo.problemInfo.toString()}\n\n")
                saveLog.append(controller.resultInfo.solverLog)
                saveLog.append(controller.resultInfo.toString())

                if (!saveLog.save(input!!)) {
                    println()
                    print("""
                                |Во время сохранения отчета произошла ошибка
                                |Для продолжения нажмите клавишу ENTER. . .
                          """.trimMargin())

                    readLine()
                    return false
                }

                println()
                print("""
                            |Отчет успешно сохранен по указанному пути
                            |Для продолжения нажмите клавишу ENTER. . .
                      """.trimMargin())
                
                readLine()
                return true
            }

            else {
                println()
                print("""
                            |Неверный формат, необходим повторный ввод
                            |Для продолжения нажмите клавишу ENTER. . .
                      """.trimMargin())

                readLine()
                return false
            }
        }
    }

    private fun askUInt(prompt: String): Int {
        log.append(prompt)
        
        while (true) {
            clearTerminal()
            
            print(log)

            val input = readlnOrNull()

            val value = controller.verifyUInt(input)
            if (value != null) {
                log.append("${input}\n")

                return value
            }
            else { 
                println()
                print("""
                            |Неверный формат, необходим повторный ввод
                            |Для продолжения нажмите клавишу ENTER. . .
                      """.trimMargin())

                readLine()
                continue
             }
        }
    }
} 