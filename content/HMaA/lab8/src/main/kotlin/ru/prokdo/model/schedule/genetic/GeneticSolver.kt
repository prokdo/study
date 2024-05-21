package ru.prokdo.model.schedule.genetic

import kotlin.system.measureTimeMillis

import ru.prokdo.model.math.IntMatrix
import ru.prokdo.model.util.log.Logger
import ru.prokdo.model.util.random.NumberGenerator
import ru.prokdo.model.schedule.genetic.info.*
import ru.prokdo.model.schedule.genetic.individual.*
import ru.prokdo.model.schedule.genetic.operator.*
import ru.prokdo.model.schedule.genetic.population.Population
import ru.prokdo.model.schedule.genetic.GeneticSolver

object GeneticSolver {
    private const val _MAX_GENOTYPE_VALUE = 255
    private const val _OPERATOR_RETRIES = 3
    
    private val _log = Logger()

    lateinit var _problemInfo: ProblemInfo

    private val _genotypeIntervalsBounds: IntArray by lazy { _calculateIntervalsBounds() }
        
    private fun _calculateIntervalsBounds(): IntArray {
        val remainder = (_MAX_GENOTYPE_VALUE + 1) % GeneticSolver._problemInfo.processorsNumber
        val result = IntArray(_problemInfo.processorsNumber - 1)
        val incPos = result.size - (remainder + 1)

        result.forEachIndexed { index, _ -> run {
            result[index] = (index + 1) * (_MAX_GENOTYPE_VALUE / _problemInfo.processorsNumber)

            if (index > incPos) result[index]++
        } }

        return result
    }

    operator fun invoke(problemInfo: ProblemInfo): ResultInfo {
        _problemInfo = problemInfo

        val finalPopulation: Population
        val elapsedTime = measureTimeMillis { 
            var output: String

            var currentPopulation = Population(0, problemInfo.individualsNumber)
            currentPopulation.forEachIndexed { individualIndex, _ -> run {
                currentPopulation[individualIndex] = Individual(individualIndex, Genotype(IntArray(_problemInfo.tasksNumber) { 0 }))

                currentPopulation[individualIndex].genotype.forEachIndexed { geneIndex, _ ->
                    do currentPopulation[individualIndex].genotype[geneIndex] = NumberGenerator(_MAX_GENOTYPE_VALUE)
                    while (currentPopulation[individualIndex].phenotype.pheneToWeight(geneIndex) == IntMatrix.POSITIVE_INFINITY)
                }
            } }
            
            output = "$currentPopulation\n\n\n"
            _log.append(output)
            print(output)

            var count = 1
            while (count < _problemInfo.limitNumber) {
                output = "Формирование нового поколения:\n\n\n"
                _log.append(output)
                print(output)

                val nextPopulation = Population(currentPopulation.index + 1, problemInfo.individualsNumber)

                currentPopulation.forEach { individual -> run { 
                    val candidates = ArrayList<Individual>(4)
                    candidates.add(individual)

                    if (NumberGenerator(100) <= _problemInfo.crossoverProbability) {
                        var partner = currentPopulation.random()
                        while (partner == individual) { partner = currentPopulation.random() }

                        var crossoverInfo: CrossoverInfo?
                        var retriesCount = 0
                        var crossoverContainsInfinity = false
                        do {
                            crossoverInfo = Crossover(individual, partner)
                            run breaking@ { crossoverInfo!!.children.first.phenotype.forEachIndexed { pheneIndex, _ ->
                                if (
                                    crossoverInfo!!.children.first.phenotype.pheneToWeight(pheneIndex) == IntMatrix.POSITIVE_INFINITY ||
                                    crossoverInfo!!.children.second.phenotype.pheneToWeight(pheneIndex) == IntMatrix.POSITIVE_INFINITY
                                ) {
                                    crossoverContainsInfinity = true
                                    retriesCount++
                                    crossoverInfo = null
                                    return@breaking
                                }
                            } }
                        } while (crossoverContainsInfinity && retriesCount < _OPERATOR_RETRIES)

                        if (crossoverInfo != null) {
                            candidates.add(crossoverInfo!!.children.first)
                            candidates.add(crossoverInfo!!.children.second)

                            output = "$crossoverInfo\n\n\n"
                            _log.append(output)
                            print(output)
                        }
                    }

                    if (NumberGenerator(100) <= _problemInfo.mutationProbability) {
                        var mutationInfo: MutationInfo?
                        var retriesCount = 0
                        var mutationContainsInfiniity = false
                        do {
                            mutationInfo = Mutator(individual)
                            run breaking@ { mutationInfo!!.mutation.phenotype.forEachIndexed { pheneIndex, _ ->
                                if (mutationInfo!!.mutation.phenotype.pheneToWeight(pheneIndex) == IntMatrix.POSITIVE_INFINITY) {
                                    mutationContainsInfiniity = true
                                    retriesCount++
                                    mutationInfo = null
                                    return@breaking
                                }
                            } }
                        } while (mutationContainsInfiniity && retriesCount < _OPERATOR_RETRIES)

                        if (mutationInfo != null) {
                            candidates.add(mutationInfo!!.mutation)

                            output = "$mutationInfo\n\n\n"
                            _log.append(output)
                            print(output)
                        }
                    }

                    nextPopulation[individual.index] = Selector(candidates)
                } }

                if (currentPopulation.best.fitness == nextPopulation.best.fitness) count++
                else count = 1

                currentPopulation = nextPopulation
                
                output = "$currentPopulation\n\n\n"
                _log.append(output)
                print(output)
            }

            finalPopulation = currentPopulation
        }

        val result = finalPopulation.best.phenotype.toIntMatrix()

        return  ResultInfo(
                            problemInfo, 
                            finalPopulation.index, 
                            result, 
                            finalPopulation.best.fitness, 
                            elapsedTime, 
                            _log.toString()
                )
    }

    fun Genotype.toPhenotype(): Phenotype {
        val result = IntArray(this.size)

        result.forEachIndexed { geneIndex, _ -> run breaking@ {
            _genotypeIntervalsBounds.forEachIndexed boundLoop@ { boundIndex, boundValue ->
                if (this[geneIndex] > boundValue)
                    if (boundIndex == _genotypeIntervalsBounds.size - 1) result[geneIndex] = _genotypeIntervalsBounds.size
                    else return@boundLoop
                else {
                    result[geneIndex] = boundIndex
                    return@breaking
                }
            }
        } }

        return Phenotype(result)
    }

    fun Genotype.geneToPhene(geneIndex: Int): Int {
        _genotypeIntervalsBounds.forEachIndexed boundLoop@ { boundIndex, boundValue ->
            if (this[geneIndex] > boundValue)
                if (boundIndex == _genotypeIntervalsBounds.size - 1) _genotypeIntervalsBounds.size
                else return@boundLoop
            else return boundIndex
        }

        return -1
    }

    fun Phenotype.toIntMatrix(): IntMatrix {
        val result = IntMatrix(_problemInfo.tasksNumber, _problemInfo.processorsNumber)
        this.forEachIndexed { 
            taskIndex, processorNumber -> result[taskIndex, processorNumber] = _problemInfo.weightMatrix[taskIndex, processorNumber]
        }

        return result
    }

    fun Phenotype.pheneToWeight(pheneIndex: Int): Int = _problemInfo.weightMatrix[pheneIndex, this[pheneIndex]]

    operator fun Selector.invoke(individual: Individual): Int {
        val load = IntArray(_problemInfo.processorsNumber)
        individual.phenotype.forEachIndexed { taskIndex, processorIndex -> 
            load[processorIndex] += _problemInfo.weightMatrix[taskIndex, processorIndex]
        }

        return load.max()
    }

    operator fun Selector.invoke(candidates: ArrayList<Individual>): Individual = candidates.minBy { it.fitness }

    operator fun Selector.invoke(population: Population): Individual = population.minBy { it.fitness }
}