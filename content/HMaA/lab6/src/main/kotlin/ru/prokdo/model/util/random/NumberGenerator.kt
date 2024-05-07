package ru.prokdo.model.util.random

import kotlin.math.pow
import kotlin.random.Random

object NumberGenerator {
    operator fun invoke(until: Int): Int = when {
        until < 0 -> -Random.nextInt(-until - 1)
        until > 0 -> Random.nextInt(until + 1)
        else -> 0
    }

    operator fun invoke(until: Int, precision: UInt): Double {
        var integer = when {
            until < 0 -> -Random.nextInt(-until - 1)
            until > 0 -> Random.nextInt(until + 1)
            else -> 0
        }

        val fraction = Random.nextInt(10.0.pow(precision.toInt()).toInt())

        return integer.toDouble() + fraction.toDouble() / 10.0.pow(precision.toInt())
    }

    operator fun invoke(from: Int, until: Int): Int = when {
        from < until -> Random.nextInt(from, until + 1)
        from > until -> Random.nextInt(until, from + 1)
        else -> from
    }

    operator fun invoke(from: Int, until: Int, precision: UInt): Double {
        var integer = when {
            from < until -> Random.nextInt(from, until + 1)
            from > until -> Random.nextInt(until, from + 1)
            else -> from
        }

        val fraction = Random.nextInt(10.0.pow(precision.toInt()).toInt())

        return integer.toDouble() + fraction.toDouble() / 10.0.pow(precision.toInt())
    }
}