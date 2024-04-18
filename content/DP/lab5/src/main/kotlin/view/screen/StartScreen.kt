package view.screen


import kotlin.system.exitProcess

import controller.screen.StartScreenController
import view.terminal.Terminal


class StartScreen : Screen() {
    override val controller = StartScreenController()

    override fun show() {
        println("Защита информации, лабораторная работа №5")
        println("Автор: ВПР32, Прокопенко Дмитрий")
        println()

        println("Получение доступа к системе:")
        println()

        println("[ 1 ] Регистрация пользователя")
        println("[ 2 ] Вход под пользователем")
        println("[ 3 ] Завершение работы")
        println()

        var choice: String?
        while (true) {
            print("Выберите опцию: ")

            choice = readlnOrNull()

            if (!controller.verifyChoice(choice)) {
                println("Неверный формат ввода. Повторите ввод")
                println()

                continue
            }

            break
        }

        when (choice) {
            "1" -> Terminal.changeScreen(RegistrationScreen())
            "2" -> Terminal.changeScreen(LoginScreen())
            "3" -> exitProcess(0)
        }
    }
}