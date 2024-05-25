package ru.prokdo.model.tsp.genetic

import kotlin.system.measureTimeMillis

import org.graphstream.graph.implementations.MultiGraph
import org.graphstream.graph.Edge

import ru.prokdo.model.math.IntMatrix
import ru.prokdo.model.util.log.Logger
import ru.prokdo.model.util.random.NumberGenerator
import ru.prokdo.model.tsp.genetic.info.*
import ru.prokdo.model.tsp.genetic.individual.*
import ru.prokdo.model.tsp.genetic.operator.*
import ru.prokdo.model.tsp.genetic.population.Population
import ru.prokdo.model.tsp.info.ResultInfo

object GeneticSolver {
    private val _log = Logger()

    lateinit private var _geneticProblemInfo: GeneticProblemInfo

    private fun _createOriginPopulation(): Population {
        val population = Population(0, _geneticProblemInfo.individualsNumber)

        population.forEachIndexed { individualIndex, _ -> run { 
            val genotype = IntArray(_geneticProblemInfo.problemInfo.nodesNumber + 1)
            genotype[0] = _geneticProblemInfo.problemInfo.startNode
            genotype[genotype.size - 1] = _geneticProblemInfo.problemInfo.startNode

            genotype.forEachIndexed loop@ { geneIndex, _ -> run {
                if (geneIndex == 0 || geneIndex == genotype.size - 1) return@loop

                var geneValue = NumberGenerator(1, _geneticProblemInfo.problemInfo.nodesNumber)
                while (genotype.contains(geneValue)) { geneValue = NumberGenerator(1, _geneticProblemInfo.problemInfo.nodesNumber) }
                genotype[geneIndex] = geneValue
            } }

            population[individualIndex] = Individual(individualIndex, Genotype(genotype))
        } }

        return population
    }

    operator fun invoke(geneticProblemInfo: GeneticProblemInfo): GeneticResultInfo {
        _geneticProblemInfo = geneticProblemInfo

        _log("$_geneticProblemInfo\n\n\n")

        val finalPopulation: Population
        val elapsedTime = measureTimeMillis {  
            var currentPopulation = _createOriginPopulation()
            _log("$currentPopulation\n\n\n")

            var count = 0
            while (count < _geneticProblemInfo.limitNumber) {
                _log("Формирование нового поколения:\n\n\n")

                val nextPopulation = Population(currentPopulation.index + 1, _geneticProblemInfo.individualsNumber)

                currentPopulation.forEach { individual -> run { 
                    val candidates = ArrayList<Individual>(4)
                    candidates.add(individual)

                    if (NumberGenerator(100) <= _geneticProblemInfo.crossoverProbability) {
                        var partner = currentPopulation.random()
                        while (partner == individual) partner = currentPopulation.random()

                        val crossoverInfo = Crossover(individual, partner)
                        candidates.add(crossoverInfo.children.first)
                        candidates.add(crossoverInfo.children.second)

                        _log("$crossoverInfo\n\n\n")
                    }

                    if (NumberGenerator(100) <= _geneticProblemInfo.mutationProbability) {
                        val mutationInfo = Mutator(individual)
                        candidates.add(mutationInfo.mutation)

                        _log("$mutationInfo\n\n\n")
                    }

                    nextPopulation[individual.index] = Selector(candidates)
                } }

                if (currentPopulation.best.fitness == nextPopulation.best.fitness) count++
                else count = 0

                currentPopulation = nextPopulation

                _log("$currentPopulation\n\n\n")
            }

            finalPopulation = currentPopulation
        }

        val resultInfo =    GeneticResultInfo(
                                            finalPopulation.index + 1,
                                            ResultInfo(
                                                        finalPopulation.best.genotype.toIntArray(),
                                                        finalPopulation.best.fitness,
                                                        finalPopulation.best.phenotype,
                                                        elapsedTime,
                                                        _log.toString()
                                            )
                            )

        _log("$resultInfo\n\n\n")

        return resultInfo
    }

    fun Genotype.toPhenotype(): MultiGraph {
        val graph = MultiGraph(
                                "phenotype_${this.hashCode()}",
                                false,
                                false,
                                _geneticProblemInfo.problemInfo.nodesNumber,
                                _geneticProblemInfo.problemInfo.nodesNumber * (_geneticProblemInfo.problemInfo.nodesNumber - 1)  
                    )
        graph.setAttribute("ui.antialias")
        graph.setAttribute("ui.title", "Генетическй алгоритм")

        for (node in _geneticProblemInfo.problemInfo.matrixGraph.nodes())
            graph.addNode(node.getId()).setAttribute("ui.label", "${node.getId()}")

        for (edge in _geneticProblemInfo.problemInfo.matrixGraph.edges())
            graph.addEdge(edge.getId(), edge.getSourceNode().getId(), edge.getTargetNode().getId())

        val stylePath = javaClass.getResource("/style/graph.css").toString()
        graph.setAttribute("ui.stylesheet", "url('$stylePath')")

        graph.getNode("${_geneticProblemInfo.problemInfo.startNode}").setAttribute("ui.class", "start")

        this.forEachIndexed loop@ { geneIndex, geneValue -> run { 
            if (geneIndex == 0) return@loop

            val sourceId = this[geneIndex - 1]
            val targetId = geneValue

            val edge = graph.getNode("$sourceId").getEdgeToward("$targetId")

            edge.setAttribute("ui.class", "path")
            edge.setAttribute("ui.label", "${_geneticProblemInfo.problemInfo.weightMatrix[sourceId - 1, targetId - 1]}")
        } }

        return graph
    }

    operator fun Selector.invoke(individual: Individual): Int {
        var result = 0
        individual.genotype.forEachIndexed loop@ { geneIndex, geneValue -> run {
            if (geneIndex == 0) return@loop
            result += _geneticProblemInfo.problemInfo.weightMatrix[geneValue - 1, individual.genotype[geneIndex - 1] - 1]
        } }

        return result
    }

    operator fun Selector.invoke(candidates: ArrayList<Individual>): Individual = candidates.minBy { it.fitness }

    operator fun Selector.invoke(population: Population): Individual = population.minBy { it.fitness }
}