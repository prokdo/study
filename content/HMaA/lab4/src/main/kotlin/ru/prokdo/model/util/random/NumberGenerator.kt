package ru.prokdo.model.util.random


import kotlin.math.pow
import kotlin.random.Random


/**
* Wrapper object for generating pseudo-random integers and doubles in specified range and precision.
*/
object NumberGenerator {
    /**
     * Wrapper function for generating pseudo-random integers in [0, [until]] range or in [[until], 0] range if [until] is negative.
     * @param until included bound of generator.
     * @param precision double fractional precision. If it is zero (by default), an integer will be generated. Must be non-negative.
     *
     * @return pseudo-random double value in range [[until], 0] or [0, [until]] with or without fractional part (depends on [precision]).
     *
     * @throws IllegalArgumentException if specified precision is negative.
     */
    fun generate(until: Int, precision: Int = 0): Double {
        if (precision < 0) throw IllegalArgumentException("Precision of double number cannot be zero or negative")

        var integer = 0
        if (until > 0) integer = Random.nextInt(until + 1)
        if (until < 0) integer = -Random.nextInt(-until + 1)

        if (precision == 0) return integer.toDouble()

        val fraction = Random.nextInt(10.0.pow(precision).toInt())

        return integer.toDouble() + fraction.toDouble() / 10.0.pow(precision)
    }

    /**
     * Wrapper function for generating pseudo-random integers in [[from], [until]] range or in [[until], [from]] range if [until] is less than [from].
     * @param from included bound of generator.
     * @param until included bound of generator.
     * @param precision double fractional precision. If it is zero (by default), an integer will be generated. Must be non-negative.
     *
     * @return pseudo-random double value in range [[from], [until]] or [[until], [from]] with or without fractional part (depends on [precision]).
     *
     * @throws IllegalArgumentException if specified precision is negative.
     */
    fun generate(from: Int, until: Int, precision: Int = 0): Double {
        if (precision < 0) throw IllegalArgumentException("Precision of double number cannot be zero or negative")

        var integer = from
        if (from < until) integer = Random.nextInt(from, until + 1)
        if (from > until) integer = Random.nextInt(until, from + 1)

        if (precision == 0) return integer.toDouble()

        val fraction = Random.nextInt(10.0.pow(precision).toInt())

        return integer.toDouble() + fraction.toDouble() / 10.0.pow(precision)
    }
}