package ru.prokdo.model.tsp.greedy

import kotlin.system.measureTimeMillis

import org.graphstream.graph.implementations.MultiGraph
import org.graphstream.graph.Edge

import ru.prokdo.model.tsp.info.ProblemInfo
import ru.prokdo.model.tsp.info.ResultInfo
import ru.prokdo.model.util.log.Logger

object GreedySolver {
    lateinit private var _problemInfo: ProblemInfo

    operator fun invoke(problemInfo: ProblemInfo): ResultInfo {
        _problemInfo = problemInfo

        val path = IntArray(_problemInfo.nodesNumber + 1)
        val elapsedTime = measureTimeMillis {
            val visited = BooleanArray(_problemInfo.nodesNumber)

            path[0] = _problemInfo.startNode
            path[path.size - 1] = _problemInfo.startNode
            visited[_problemInfo.startNode - 1] = true

            var pathIndex = 1
            var currentNodeIndex = _problemInfo.startNode - 1
            for (i in 1 ..< _problemInfo.nodesNumber) {
                var minIndex = -1
                var minWeight = Integer.MAX_VALUE

                for (columnIndex in _problemInfo.weightMatrix.columnIndices)
                    if (!visited[columnIndex] && _problemInfo.weightMatrix[currentNodeIndex, columnIndex] < minWeight) {
                        minIndex = columnIndex
                        minWeight = _problemInfo.weightMatrix[currentNodeIndex, columnIndex]
                    }

                currentNodeIndex = minIndex
                path[pathIndex++] = minIndex + 1
                visited[currentNodeIndex] = true
            }
        }

        return  ResultInfo(
                            path,
                            _calculatePathLength(path),
                            _buildGraph(path),
                            elapsedTime
                )
    }

    private fun _calculatePathLength(path: IntArray): Int {
        var length = 0
        path.forEachIndexed loop@ { pathIndex, value -> run {
            if (pathIndex == 0) return@loop
            length += _problemInfo.weightMatrix[value - 1, path[pathIndex - 1] - 1]
        } }

        return length
    }

    private fun _buildGraph(path: IntArray): MultiGraph {
        val graph = MultiGraph(
                                "greedy",
                                false,
                                false,
                                _problemInfo.nodesNumber,
                                _problemInfo.nodesNumber * (_problemInfo.nodesNumber - 1)  
                    )
        graph.setAttribute("ui.antialias")
        graph.setAttribute("ui.title", "Жадный алгоритм")

        for (node in _problemInfo.matrixGraph.nodes())
            graph.addNode(node.getId()).setAttribute("ui.label", "${node.getId()}")

        for (edge in _problemInfo.matrixGraph.edges())
            graph.addEdge(edge.getId(), edge.getSourceNode().getId(), edge.getTargetNode().getId())

        val stylePath = javaClass.getResource("/style/graph.css").toString()
        graph.setAttribute("ui.stylesheet", "url('$stylePath')")

        graph.getNode("${_problemInfo.startNode}").setAttribute("ui.class", "start")
        
        path.forEachIndexed loop@ { index, value -> run { 
            if (index == 0) return@loop

            val sourceId = value
            val targetId = path[index - 1]

            val edge = graph.getNode("$sourceId").getEdgeToward("$targetId")

            edge.setAttribute("ui.class", "path")
            edge.setAttribute("ui.label", "${_problemInfo.weightMatrix[sourceId - 1, targetId - 1]}")
        } }

        return graph
    }
}