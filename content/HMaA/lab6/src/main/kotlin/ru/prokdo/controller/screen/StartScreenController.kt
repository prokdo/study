package ru.prokdo.controller.screen

import ru.prokdo.model.math.IntMatrix
import ru.prokdo.model.util.info.ProblemInfo
import ru.prokdo.model.util.random.DataGenerator

class StartScreenController : ScreenController() {
    val problemInfo = ProblemInfo()
    
    fun verifyUInt(strInt: String?): Int? {
        if (strInt == null) return null

        if (strInt.isEmpty() || strInt.isBlank()) return null

        try { 
            val value = strInt.toInt()
            if (value < 0) return null
            
            return value
         }
        catch (exception: Exception) { return null }
    }

    fun verifyUIntPair(strIntPair: String?): Pair<Int, Int>? {
        if (strIntPair == null) return null

        if (strIntPair.isEmpty() || strIntPair.isBlank()) return null

        try { 
            val strIntPairSplit = strIntPair.split(", ", ",", " ")

            if (strIntPairSplit.size != 2) return null

            val first = strIntPairSplit[0].toInt()
            val second = strIntPairSplit[1].toInt()
            if (first < 0 || second < 0) return null

            return Pair(first, second)
        }
        catch (exception: Exception) { return null }
    }

    fun generateWeightMatrix(): IntMatrix = DataGenerator(this.problemInfo)
}