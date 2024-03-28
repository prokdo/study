package ru.prokdo.view.screen


import ru.prokdo.controller.StartController


class StartScreen : Screen() {
    override val controller = StartController()

    override fun show() {
        println("=".repeat(150))
        println("Ввод исходных данных")
        println()

        while (true) {
            print("Количество задач (M): ")
            val number = this.controller.readNaturalNumber()

            if (number == null) {
                println()
                println("Неверный формат ввода!\n")
                continue
            }

            controller.tasksNumber = number
            break
        }

        while (true) {
            print("Количество процессоров (N): ")
            val number = this.controller.readNaturalNumber()

            if (number == null)  {
                println()
                println("Неверный формат ввода!\n")
                continue
            }

            this.controller.processorsNumber = number
            break
        }

        while (true) {
            print("Границы значений весов задач (T1, T2): ")
            val pair = controller.readNaturalPair()

            if (pair == null)  {
                println()
                println("Неверный формат ввода!\n")
                continue
            }

            this.controller.weightBounds = pair
            break
        }

        println()

        println("Запущена генерация матрицы весов задач (T) . . .")
        this.controller.createProblemInfo()

        println()

        println("Матрица весов задач (T):")
        println(this.controller.problemInfo!!.weightMatrix)

        println("=".repeat(150))
        println()

        this.controller.changeScreen(CriterionScreen(this.controller.problemInfo!!))
    }
}