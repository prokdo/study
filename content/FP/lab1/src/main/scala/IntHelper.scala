object IntHelper:
    /** Возвращает логический признак просты числа
     *
     * Например:
     * {{{
     *     scala> 7.isPrime
     *     res0: true
     * }}}
     */
    def isPrime(x: Int): Boolean =
      def isPrimeRecursion(x: Int, i: Int = 2): Boolean =
        i * i > x match
          case false if x % i == 0 => false
          case false => isPrimeRecursion(x, i + 1)
          case true if x < 2 => false
          case true => true

      isPrimeRecursion(x)

    /** Возвращает наибольший общий делитель двух чисел
     *
     * Например:
     * {{{
     *     scala> gcd(56, 16)
     *     res0: 8
     * }}}
     */
    def gcd(a: Int, b: Int): Int = if b == 0 then a else gcd(b, a % b)

    /** Определяет, являются ли два положительных числа взаимно простыми
     *
     * Два числа называют взаимно простыми, если их наибольший общий делитель равен 1.
     * Например:
     * {{{
     *     scala> isCoprime(35, 64)
     *     res0: true
     * }}}
     */
    def isCoprime(a:Int, b: Int): Boolean = gcd(a, b) == 1

    /** Возвращает значение функции Эйлера для числа
     *
     * Значением функции Эйлера (totient-функцией) числа N называется количество чисел лежащих
     * на интервале от 1 до N и взаимно простых с ним.
     *
     * Например:
     * {{{
     *     scala> totient(10)
     *     res0: 4
     * }}}
     * 
     * @param x Исходное число
     * @return Значение функции Эйлера
     */
    def totient(x: Int): Int =
      def totientRecursion(x: Int, i: Int = 1, count: Int = 0): Int =
        i >= x match
          case false if isCoprime(x, i) => totientRecursion(x, i + 1, count + 1)
          case false => totientRecursion(x, i + 1, count)
          case true => count

      totientRecursion(x)
        

    /** Возвращает список простых множителей числа
     *
     * Разложением числа на простые множители называется представление заданного числа в виде
     * произведения простых чисел. Такое разложение для каждого числа всегда единственно с точностью
     * до порядка следования простых чисел.
     *
     * Например:
     * {{{
     *     scala> primeFactors(124)
     *     res0: List(2, 2, 31)
     * }}}
     *
     * (!) При реализации функции нельзя использовать операторы циклов.
     *
     * @param x Исходное число
     * @return Список, содержащий простые множители числа [[x]] в порядке возрастания.
     */
    def primeFactors(x: Int): List[Int] =
      def primeFactorsRecursion(x: Int, i: Int = 2, list: List[Int] = Nil) : List[Int] = 
        i * i > x match
          case false if x % i == 0 => primeFactorsRecursion(x / i, i, i :: list)
          case false => primeFactorsRecursion(x, i + 1, list)
          case true => x :: list
        
      primeFactorsRecursion(x).reverse

    /** Возвращает список простых чисел в диапазоне
     *
     * Например:
     * {{{
     *     scala> listPrimesInRange(7 to 31)
     *     res0: List(7, 11, 13, 17, 19, 23, 29, 31)
     * }}}
     *
     * (!) При реализации функции нельзя использовать условные операторы (if, match) и операторы циклов.
     *
     * @param r Диапазон чисел
     * @return Список простых
     */
    def listPrimesInRange(r: Range): List[Int] = r.filter(isPrime).toList

    /** Возвращает два целых простых числа, дающих сумму заданного
     *
     * Гипотеза Гольдбаха гласит, что каждое положительное четное число больше 2 является суммой
     * двух простых чисел. Например, 28=5+23. Это один из самых известных фактов теории чисел,
     * правильность которого в общем случае не доказана, но численно подтверждено до очень очень
     * больших значений (намного больших, чем может представить Int в Scala).
     * Напишите функцию для нахождения двух простых чисел, сумма которых дает заданное четное целое число.
     *
     * Например:
     * {{{
     *     scala> goldbach(28)
     *     res0: (5,23)
     * }}}
     *
     * (!) При реализации функции нельзя использовать операторы циклов.
     *
     * @param x Исходное числа
     * @return Пара простых чисел, дающих в сумме заданное
     */
    def goldbach(x: Int): (Int, Int) =
      def goldbachRecursion(x: Int, i: Int = 2): (Int, Int) =
        i * i > x match
          case false if isPrime(i) && isPrime(x - i) => (i, x - i)
          case false => goldbachRecursion(x, i + 1)
          case true => (0, x)

      goldbachRecursion(x)

end IntHelper