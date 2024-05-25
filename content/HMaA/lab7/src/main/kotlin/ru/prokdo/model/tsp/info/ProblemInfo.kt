package ru.prokdo.model.tsp.info

import org.graphstream.graph.implementations.MultiGraph

import ru.prokdo.model.math.IntMatrix

data class  ProblemInfo(
                        var nodesNumber: Int = 0,
                        var startNode: Int = 0,
                        var weightBounds: Pair<Int, Int> = Pair(0, 0),
                        var weightMatrix: IntMatrix = IntMatrix(0, 0),
            ): Info() {
    
    var matrixGraph: MultiGraph = MultiGraph("")
        get() {
            val graph = MultiGraph(
                                    "Initial graph",
                                    false,
                                    false,
                                    nodesNumber,
                                    nodesNumber * (nodesNumber - 1)
                        )

            graph.setAttribute("ui.antialias")
            graph.setAttribute("ui.title", "Исходный граф")

            for (nodeIndex in weightMatrix.rowIndices)
                graph.addNode("${nodeIndex + 1}").setAttribute("ui.label", "${nodeIndex + 1}")

            run breaking@ { weightMatrix.forEachIndexed { rowIndex, _ -> 
                var columnIndex = rowIndex + 1
                while (columnIndex < nodesNumber) {
                    graph.addEdge("${rowIndex + 1}->${columnIndex + 1}", "${rowIndex + 1}", "${columnIndex + 1}")

                    columnIndex++
                }
            } }

            val stylePath = javaClass.getResource("/style/graph.css").toString()
            graph.setAttribute("ui.stylesheet", "url('$stylePath')")
            
            graph.getNode("$startNode").setAttribute("ui.class", "start")

            return graph
        }

    override fun toString(): String =
                                    """
                                        |Количество вершин (N): $nodesNumber
                                        |Начальная вершина (S): $startNode
                                        |Границы весов путей (T1, T2): ${weightBounds.first}, ${weightBounds.second}
                                        |Матрица весов путей (T):
                                        |$weightMatrix
                                    """.trimMargin()
}