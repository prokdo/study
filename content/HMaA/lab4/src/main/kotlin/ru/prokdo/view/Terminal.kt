package ru.prokdo.view


import ru.prokdo.view.screen.Screen
import ru.prokdo.view.screen.StartScreen


object Terminal {
    private var currentScreen: Screen? = null

    fun launch() {
        println("=".repeat(150))
        println("Лабораторная работа №4 \"Теория разнородных расписаний. Алгоритм Плотникова-Зверева с различными критериями оптимальности\"")
        println("Автор: ВПР32, Прокопенко Дмитрий")
        println("=".repeat(150))
        println()
        setScreen(StartScreen())
    }

    fun setScreen(screen: Screen) {
        currentScreen = screen

        currentScreen!!.show()
    }
}