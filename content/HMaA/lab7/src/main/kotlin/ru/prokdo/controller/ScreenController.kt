package ru.prokdo.controller

import java.math.BigDecimal
import java.math.BigInteger
 
import ru.prokdo.KtCLUIApp
import ru.prokdo.screen.Screen

abstract class ScreenController {
    fun changeScreen(screen: Screen) = KtCLUIApp().changeScreen(screen)

    fun verifyString(string: String?) = !(string.isNullOrBlank() || string.isNullOrEmpty())

    fun verifyUInt(strUInt: String?) = strUInt?.toUIntOrNull()

    fun verifyInt(strInt: String?) = strInt?.toIntOrNull()

    fun verifyDouble(strDouble: String?) = strDouble?.toDoubleOrNull()

    fun verifyFloat(strFloat: String?) = strFloat?.toFloatOrNull()

    fun verifyBigDecimal(strBigDecimal: String?): BigDecimal? {
        if (!verifyString(strBigDecimal)) return null

        try { return BigDecimal(strBigDecimal) }
        catch (_: Exception) { return null }
    }

    fun verifyBigInteger(strBigInteger: String?): BigInteger? {
        if (!verifyString(strBigInteger)) return null

        try { return BigInteger(strBigInteger) }
        catch (_: Exception) { return null }
    }

    fun verifyStringPair(strPair: String?): Pair<String, String>? {
        if (!verifyString(strPair)) return null

        try {
            val strIntPairSplited = strPair!!.split(", ", ",", " ")
            if (strIntPairSplited.size != 2) return null

            val first = strIntPairSplited[0].replace("'", "")
            val second = strIntPairSplited[1].replace("'", "")

            if (!verifyString(first) || !verifyString(second)) return null

            return Pair(first, second)
        } catch (_: Exception) { return null }
    }

    fun verifyUIntPair(strUIntPair: String?): Pair<UInt, UInt>? {
        if (!verifyString(strUIntPair)) return null

        try {
            val strUIntPairSplited = strUIntPair!!.split(", ", ",", " ")
            if (strUIntPairSplited.size != 2) return null

            val first = strUIntPairSplited[0].toUIntOrNull()
            val second = strUIntPairSplited[1].toUIntOrNull()

            if (first == null || second == null) return null

            return Pair(first, second)
        } catch (_: Exception) { return null }
    }

    fun verifyIntPair(strIntPair: String?): Pair<Int, Int>? {
        if (!verifyString(strIntPair)) return null

        try {
            val strIntPairSplited = strIntPair!!.split(", ", ",", " ")
            if (strIntPairSplited.size != 2) return null

            val first = strIntPairSplited[0].toIntOrNull()
            val second = strIntPairSplited[1].toIntOrNull()

            if (first == null || second == null) return null

            return Pair(first, second)
        } catch (_: Exception) { return null }
    }

    fun verifyDoublePair(strDoublePair: String?): Pair<Double, Double>? {
        if (!verifyString(strDoublePair)) return null

        try {
            val strIntPairSplited = strDoublePair!!.split(", ", ",", " ")
            if (strIntPairSplited.size != 2) return null

            val first = strIntPairSplited[0].toDoubleOrNull()
            val second = strIntPairSplited[1].toDoubleOrNull()

            if (first == null || second == null) return null

            return Pair(first, second)
        } catch (_: Exception) { return null }
    }

    fun verifyFloatPair(strFloatPair: String?): Pair<Float, Float>? {
        if (!verifyString(strFloatPair)) return null

        try {
            val strIntPairSplited = strFloatPair!!.split(", ", ",", " ")
            if (strIntPairSplited.size != 2) return null

            val first = strIntPairSplited[0].toFloatOrNull()
            val second = strIntPairSplited[1].toFloatOrNull()

            if (first == null || second == null) return null

            return Pair(first, second)
        } catch (_: Exception) { return null }
    }
}
