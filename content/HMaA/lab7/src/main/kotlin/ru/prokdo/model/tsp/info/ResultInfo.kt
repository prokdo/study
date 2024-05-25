package ru.prokdo.model.tsp.info

import org.graphstream.graph.implementations.MultiGraph

import ru.prokdo.model.math.IntMatrix

data class  ResultInfo(
                        var path: IntArray = IntArray(0),
                        var pathLength: Int = 0,
                        var pathGraph: MultiGraph = MultiGraph(""),
                        var elapsedTime: Long = 0,
                        var solverLog: String = ""
            ): Info() {
    
    override fun toString(): String {
        val pathStrBuilder = StringBuilder("[")
        path.forEachIndexed { index, value -> run {
            pathStrBuilder.append(value)
            if (index != path.size - 1) pathStrBuilder.append(" -> ")
            else pathStrBuilder.append("]")
        } }

        return  """
                    |Путь (R): $pathStrBuilder
                    |Длина пути (L): $pathLength
                    |Время решения задачи (t, мс): $elapsedTime
                """.trimMargin()
    }
}