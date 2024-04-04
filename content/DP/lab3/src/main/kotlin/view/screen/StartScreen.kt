package view.screen

import controller.screen.StartScreenController
import view.terminal.Terminal
import kotlin.system.exitProcess

class StartScreen : Screen() {
    override val controller = StartScreenController()

    override fun show() {
        println("=".repeat(100))

        println("Защита информации, лабораторная работа №3 \"Мандатная политика безопасности\"")
        println("Автор: ВПР32, Прокопенко Дмитрий")

        println("=".repeat(100))

        println()

        println("=".repeat(100))

        println("Вход в систему")
        println()

        println("Введите имя пользователя для входа в систему")
        println("(Для прекращения процесса авторизации и выхода из программы введите exit)")
        println()

        while (true) {
            print("username: ")

            val userName = readlnOrNull()

            when (this.controller.verifyUserName(userName)) {
                false -> {
                    println("Неверное имя пользователя. Повторите ввод")
                    println()

                    continue
                }
                true -> {
                    println("Успешная авторизация под пользователем $userName")
                    println("=".repeat(100))
                    println()

                    Terminal.controller.currentUser = userName

                    break
                }
                null -> {
                    println("Завершение работы . . .")
                    println("=".repeat(100))
                    exitProcess(0)
                }
            }
        }

        Terminal.changeScreen(UserScreen())
    }
}