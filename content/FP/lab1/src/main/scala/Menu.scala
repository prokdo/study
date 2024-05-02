import scala.io.StdIn.readLine

object Menu:
    def start = _showStartMenu

    private def _showStartMenu =
        while true do
            _showHeaderMessage

            print("""Меню доступных модулей:

                    |[ 1 ] Lists - модуль, содержащий методы для работы со стандартными списками
                    |[ 2 ] IntHelper - модуль вспомогательных математических функций и признаков для целых чисел
                    |[ 3 ] Logic - модуль, представляющий собой набор базовых булевых функций и инструмент для построения их таблиц истинности
                    |[ 4 ] RobotSimulator - модуль, эмулирующий работу программы для тестировки роботов-ходоков
                    |[ 5 ] BinaryTree - модуль, содержащий класс двоичного дерева поиска и все его компонентные классы, объекты и их методы
                    |[ 6 ] Завершение работы программы

                    |Выберите модуль для тестировки: """.stripMargin)

            val input = readLine()
            println

            if input == null then _showInvalidFormatMessage
            else if input.isBlank || input.isEmpty then _showInvalidFormatMessage
            else input match
                case "1" => _showListsMenu
                case "2" => _showIntHelperMenu
                case "3" => _showLogicMenu
                case "4" => _showRobotSimulatorMenu
                case "5" => _showBinaryTreeMenu
                case "6" => println
                            println("Завершение работы . . .")
                            sys.exit
                case _ =>   _showInvalidFormatMessage

    private def _showInvalidFormatMessage = 
        print("""Неправильный формат ввода, необходим повторный ввод
                |Нажмите клавишу ENTER для продолжения . . .""".stripMargin)
        readLine
        println

    private def _showHeaderMessage = 
        print("""Функциональное программирование, лабораторная работа №1
                |Автор: ВПР32, Прокопенко Дмитрий""".stripMargin)
        println
        println

    private def _showListsMenu =
        _showHeaderMessage

        println("Модуль Lists")
        println

        val testData = _getTestDataForLists
        if testData == null then _showTestDataInvalidFormatMessage
        else
            println
            println(s"Ваш функциональный параметр: ${testData._1}")
            println(s"Ваш список: ${testData._2}")

            println

            print(s"""Результат функции last: ${Lists.last(testData._2)}
                    |Результат функции penultimate: ${Lists.penultimate(testData._2)}
                    |Результат функции nth: ${Lists.nth(testData._1 - 1, testData._2)}
                    |Результат функции len: ${Lists.len(testData._2)}
                    |Результат функции reverse: ${Lists.reverse(testData._2)}
                    |Результат функции isPalindrome: ${Lists.isPalindrome(testData._2)}
                    |Результат функции flatten: ${Lists.flatten(testData._2)}
                    |Результат функции compress: ${Lists.compress(testData._2)}
                    |Результат функции pack: ${Lists.pack(testData._2)}
                    |Результат функции rleEncode: ${Lists.rleEncode(testData._2)}
                    |Результат функции duplicate: ${Lists.duplicate(testData._2)}
                    |Результат функции duplicateN: ${Lists.duplicateN(testData._1 - 1, testData._2)}
                    |Результат функции dropN: ${Lists.dropN(testData._1 - 1, testData._2)}
                    |Результат функции split: ${Lists.split(testData._1 - 1, testData._2)}
                    |Результат функции slice: ${Lists.slice(testData._1 - 1, testData._2.size - testData._1 - 1, testData._2)}
                    |Результат функции range: ${Lists.range(testData._1 - 1, testData._2.size - testData._1)}
                    |Результат функции randomSelect: ${Lists.randomSelect(testData._1 - 1, testData._2)}
                    |Результат функции lotto: ${Lists.lotto(testData._1 - 1, testData._2.size - testData._1 - 1)}

                    |Для продолжения нажмите клавишу ENTER. . .""".stripMargin)
            readLine
            println

    private def _showIntHelperMenu =
        _showHeaderMessage
        
        println("Модуль IntHelper")
        println

        val testData = _getTestDataForIntHelper
        if testData == null then _showTestDataInvalidFormatMessage
        else
            println
            println(s"Ваши функциональные параметры: $testData")

            println

            print(s"""|Результат функции isPrime: ${IntHelper.isPrime(testData._1)}
                    |Результат функции gcd: ${IntHelper.gcd(testData._1, testData._2)}
                    |Результат функции isCoprime: ${IntHelper.isCoprime(testData._1, testData._2)}
                    |Результат функции totient: ${IntHelper.totient(testData._1)}
                    |Результат функции primeFactors: ${IntHelper.primeFactors(testData._1)}
                    |Результат функции listPrimesInRange: ${IntHelper.listPrimesInRange(testData._1 to testData._2)}
                    |Результат функции goldbach: ${IntHelper.goldbach(testData._1)}

                    |Для продолжения нажмите клавишу ENTER. . .""".stripMargin)
            readLine
            println

    private def _showLogicMenu =
        _showHeaderMessage

        println("Модуль Logic")
        println

        print(s"""Таблица истинности функции not:
                |${Logic.truthTable(Logic.not)}

                |Таблица истинности функции and:
                |${Logic.truthTable(Logic.and)}

                |Таблица истинности функции or:
                |${Logic.truthTable(Logic.or)}

                |Таблица истинности функции nand:
                |${Logic.truthTable(Logic.nand)}

                |Таблица истинности функции xor:
                |${Logic.truthTable(Logic.xor)}

                |Таблица истинности функции impl:
                |${Logic.truthTable(Logic.impl)}

                |Таблица истинности функции equ:
                |${Logic.truthTable(Logic.equ)}

                |Для продолжения нажмите клавишу ENTER. . .""".stripMargin)
        readLine
        println

    private def _showRobotSimulatorMenu =
        _showHeaderMessage
        
        println("Модуль RobotSimulator")
        println

        val testData = _getTestDataForRobotSimulator
        if testData == null then _showTestDataInvalidFormatMessage
        else
            println
            println(s"Ваши функциональные параметры: (${testData._1}, ${testData._2})")
            println(s"Ваша строка команд для робота: ${testData._3}")

            println

            val robot = RobotSimulator(testData._1, testData._2)
            println(robot)
            println

            for index <- testData._3.indices do 
                robot.walk(testData._3(index).toString())
                println(s"Текущая команда: ${testData._3(index)}")
                println(robot)
                println

            println("Для продолжения нажмите клавишу ENTER. . .")
            readLine
            println

    private def _showBinaryTreeMenu =
        _showHeaderMessage

        println
        println("Модуль BinaryTree")

        val testData = _getTestDataForBinaryTree
        if testData == null then _showTestDataInvalidFormatMessage
        else
            println
            println(s"Ваш функциональный параметр: ${testData._1}")
            println(s"Ваш список: ${testData._2}")

            println

            val testTree = Tree.makeBalanced(testData._2)

            print(s"""Результат функции makeBalances: $testTree
                    |Результат функции isSymmetric: ${testTree.isSymmetric}
                    |Результат функции addValue: ${testTree.addValue(testData._1)}
                    |Результат функции find: ${testTree.find(testData._1)}
                    |Результат функции remove: ${testTree.remove(testData._1)}
                    |Результат функции min: ${testTree.min}

                    |Для продолжения нажмите клавишу ENTER. . .""".stripMargin)
            readLine
            println

    private def _getTestDataForLists: (Int, List[Int]) =
        print("""Для тестировки модуля необходимы исходные данные в виде списка целых чисел и целого числа, входящего в диапазон [1; N/2], где N - размер списка

                |Введите все элементы списка через пробел без каких либо разделителей
                |Если хотя бы один элемент будет некорректен, тестировка модуля будет отменена

                |Введите элементы списка: """.stripMargin)
        val inputList = readLine
        
        println
        print("Введите функциональный параметр: ")
        val inputN = readLine

        if inputList == null || inputN == null then return null
        else if inputList.isBlank || inputList.isEmpty || 
                inputN.isBlank || inputN.isEmpty then return null
        else try 
            val list = inputList.split(" ").map(value => value.toInt).toList
            val n = inputN.toInt

            if n <= 0 || n > list.size / 2 then return null

            return (n, list)
        catch _ => return null

    private def _getTestDataForIntHelper: (Int, Int) =
        print("""Для тестировки модуля необходимы исходные данные в виде пары целых положительных чисел
                |Важно, чтобы первое значение было меньше второго

                |Введите пару целых чисел через пробел без каких либо разделителей
                |Если хотя бы один элемент будет некорректен, тестировка модуля будет отменена

                |Введите пару целых чисел: """.stripMargin)
        val input = readLine

        if input == null then return null
        else if input.isBlank || input.isEmpty then return null
        else try 
            val inputSplitted = input.split(" ").map(value => value.toInt)

            if inputSplitted.size != 2 then return null

            if inputSplitted(0) <= 0 || inputSplitted(1) <= 0 then return null

            if inputSplitted(0) >= inputSplitted(1) then return null

            return (inputSplitted(0), inputSplitted(1))
        catch _ => return null

    private def _getTestDataForRobotSimulator: (Int, Int, String) =
        print("""Для тестировки модуля необходимы исходные данные в виде пары целых чисел и строки, состоящей только из символов L, R, G

                |Введите пару целых чисел через пробел без каких либо разделителей
                |Если хотя бы один элемент будет некорректен, тестировка модуля будет отменена

                |Введите пару целых чисел: """.stripMargin)
        val inputPos = readLine

        println
        print("Введите строку команд для робота: ")
        val inputCmd = readLine

        if inputPos == null || inputCmd == null then return null
        else if inputPos.isBlank || inputPos.isEmpty || 
                inputCmd.isBlank || inputCmd.isEmpty then return null
        else try 
            val inputPosSplitted = inputPos.split(" ").map(value => value.toInt)

            if inputPosSplitted.size != 2 then return null

            var isCmdCorrect = true
            for command <- inputCmd do command match
                case 'L' | 'R' | 'G' => 
                case _ => isCmdCorrect = false

            if isCmdCorrect then return (inputPosSplitted(0), inputPosSplitted(1), inputCmd)
            else return null
        catch _ => return null

    private def _getTestDataForBinaryTree: (Int, List[Int]) =
        print("""Для тестировки модуля необходимы исходные данные в виде списка целых чисел и целого числа

                |Введите все элементы списка через пробел без каких либо разделителей
                |Если хотя бы один элемент будет некорректен, тестировка модуля будет отменена

                |Введите элементы списка: """.stripMargin)
        val inputList = readLine
        
        println
        print("Введите функциональный параметр: ")
        val inputN = readLine

        if inputList == null || inputN == null then return null
        else if inputList.isBlank || inputList.isEmpty || 
                inputN.isBlank || inputN.isEmpty then return null
        else try 
            val list = inputList.split(" ").map(value => value.toInt).toList
            val n = inputN.toInt

            return (n, list)
        catch _ => return null

    private def _showTestDataInvalidFormatMessage =
        println
        print("""Введенные тестовые данные имеют неправильный формат
                |Нажмите клавишу ENTER для продолжения. . .""".stripMargin)
        readLine
        println

