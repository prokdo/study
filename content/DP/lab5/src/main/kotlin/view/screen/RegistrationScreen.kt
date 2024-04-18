package view.screen


import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis
import kotlin.io.readlnOrNull

import controller.screen.RegistrationScreenController
import view.terminal.Terminal


class RegistrationScreen : Screen() {
    override val controller = RegistrationScreenController()

    override fun show() {
        println("Регистрация нового пользователя")
        println()

        val authMeasurementsNumber = controller.getAuthMeasurementssNumber()

        println("""
                    Для регистрации в системе каждый пользователь должен пройти
                    процедуру следующего вида:

                    На экран будет выведена фраза, состоящая из случайных символов.
                    Пользователю необходимо без ошибок ввести эту строку $authMeasurementsNumber раз(а).
                    По окончании каждого из вводов система подсчитает скорость набора
                    фразы пользователем, а по окончании всех процедур ввода система
                    рассчитает среднее арифметическое временных характеристик каждой
                    попытки. Таким образом будет получено значение т.н. "клавиатурного 
                    почерка" пользователя, которое будет закреплено за ним в системе
                    вместе с выведенной на экран фразой.

                    Впоследствии для доступа к системе пользователю будет необходимо
                    подтвердить свой "клавиатурный почерк", повторив ввод фразы со 
                    скоростью, приближенно равной закрепленному за ним значению.

                    Приготовьтесь к процедуре регистрации. Для начала процедуры 
                    нажмите клавишу [ENTER].
                """.trimIndent())
        println()

        readlnOrNull()

        println("Инициализация процедуры регистрации . . .")
        println()

        val phrase = controller.getPhraseContent()

        val timeArray = Array(authMeasurementsNumber) { 0.0 }

        var input: String?
        var count = 0
        while (count < authMeasurementsNumber) {
            println("Ваша кодовая фраза: $phrase")
            println()

            print("Ввод №${count + 1}: ")

            val time = measureTimeMillis { input = readlnOrNull() }
            println()

            if (!controller.verifyInput(input)) {
                println("Неверный формат ввода. Повторите ввод")
                println()

                continue
            }

            println("Успешный ввод!")
            println()

            timeArray[count] = time.toDouble() / 1000

            count++
        }

        println("Поздравляем, Вы завершили процедуру формирования")
        println("клавиатурного почерка пользователя!")
        println()

        println("Для завершения регистрации, выберите себе имя в системе")
        println()

        var userName: String?
        while (true) {
            print("Имя пользователя: ")

            userName = readlnOrNull()
            println()

            if (!controller.verifyUserName(userName)) {
                println("Неверный формат имени пользователя или пользователь")
                println("c таким именем уже существует. Повторите ввод")
                println()

                continue
            }

            break
        }

        if (controller.tryRegistrate(userName!!, timeArray)) {
            println("Успешная регистрация")
            println()

            Terminal.changeScreen(StartScreen())
        }
        else {
            println("При регистрации возникла ошибка")
            println()

            Terminal.changeScreen(StartScreen())
        }
    }
}