package view.screen


import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis
import kotlin.io.readlnOrNull

import controller.screen.LoginScreenController
import view.terminal.Terminal


class LoginScreen : Screen() {
    override val controller = LoginScreenController()

    override fun show() {
        println("Вход в систему")
        println()

        if (!controller.checkSystemHasUsers()) {
            println("На данный момент в системе нет ни одного пользователя")
            println()

            Terminal.changeScreen(StartScreen())
        }

        println("""
                    Для входа в систему каждый пользователь должен пройти
                    процедуру следующего вида:

                    На экран будет выведена заркепленная за пользователем при 
                    регистрации фраза, состоящая из случайных символов. Пользователю 
                    необходимо без ошибок ввести эту строку.
                    По окончании вводоа система подсчитает скорость набора фразы 
                    и сравнит это значение со значением т.н. "клавиатурного почерка" 
                    пользователя, полученного при его регистрации. При приближенном 
                    равенстве этих значений пользователь получит доступ к системе.

                    Приготовьтесь к процедуре входа в систему. Для начала процедуры 
                    нажмите клавишу [ENTER].
                """.trimIndent())
        println()

        readlnOrNull()

        println("Инициализация процедуры входа . . .")
        println()

        var userName: String?
        while (true) {
            print("Введите имя пользователя: ")

            userName = readlnOrNull()
            println()

            if (!controller.verifyUserName(userName)) {
                println("Неверный формат имени пользователя или пользователь")
                println("c таким именем не найден. Повторите ввод")
                println()

                continue
            }

            break
        }

        val phrase = controller.getPhraseContent(userName!!)

        var time: Double
        var input: String?
        while (true) {
            println("Ваша кодовая фраза: $phrase")
            println()

            print("Ввод: ")

            time = measureTimeMillis { input = readlnOrNull() }.toDouble() / 1000
            println()

            if (!controller.verifyInput(input)) {
                println("Неверный формат ввода. Повторите ввод")
                println()

                continue
            }

            println("Успешный ввод! Ваше время ввода (сек.): $time")
            println()

            break
        }

        if (controller.tryLogin(userName, time)) {
            println("Успешный вход под пользователем $userName")
            println()

            Terminal.changeScreen(UserScreen())
        }
        else {
            println("При авторизации возникла ошибка или Вы не")
            println("подтвердили Ваш клавиатурный почерк")
            println()

            Terminal.changeScreen(StartScreen())
        }
    }
}