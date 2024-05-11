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

            var currentPopulation = Population(0, _problemInfo.individualsNumber)
            currentPopulation.forEachIndexed { individualIndex, _ -> run { 
                val genes = IntArray(_problemInfo.tasksNumber)
                genes.forEachIndexed { geneIndex, _ -> genes[geneIndex] = NumberGenerator(_MAX_GENOTYPE_VALUE) }

                currentPopulation[individualIndex] = Individual(individualIndex, Genotype(genes))
            } }
            
            output = "$currentPopulation\n\n\n"
            _log.append(output)
            print(output)

            var count = 0
            while (count < _problemInfo.limitNumber) {
                output = "Формирование нового поколения:\n\n\n"
                _log.append(output)
                print(output)

                val candidates = mutableListOf<Individual>()
                currentPopulation.forEach { individual -> candidates.add(individual) }

                currentPopulation.forEach { individual -> run { 
                    val crossoverRoll = NumberGenerator(100)
                    if (crossoverRoll <= _problemInfo.crossoverProbability) {
                        var partner = currentPopulation.random()
                        while (partner == individual) { partner = currentPopulation.random() }

                        val crossoverInfo = Crossover(individual, partner)
                        candidates.add(crossoverInfo.children.first)
                        candidates.add(crossoverInfo.children.second)

                        output = "$crossoverInfo\n\n\n"
                        _log.append(output)
                        print(output)
                    }

                    val mutationRoll = NumberGenerator(100)
                    if (mutationRoll <= _problemInfo.mutationProbability) {
                        val mutationInfo = Mutator(individual)
                        candidates.add(mutationInfo.mutation)

                        output = "$mutationInfo\n\n\n"
                        _log.append(output)
                        print(output)
                    }
                } }

                val nextPopulation = Population(currentPopulation.index + 1, Selector(candidates))

                if (currentPopulation.best.fitness == nextPopulation.best.fitness) count++
                else count = 0

                currentPopulation = nextPopulation
                
                output = "$currentPopulation\n\n\n"
                _log.append(output)
                print(output)
            }

            finalPopulation = currentPopulation
        }

        val result = IntMatrix(problemInfo.tasksNumber, problemInfo.processorsNumber)
        finalPopulation.best.phenotype.forEachIndexed { 
            taskIndex, processorNumber -> result[taskIndex, processorNumber] = _problemInfo.weightMatrix[taskIndex, processorNumber]
        }

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
            _genotypeIntervalsBounds.forEachIndexed boundLoop@ { boundIndex, boundValue -> run { 
                if (this[geneIndex] > boundValue)
                    if (boundIndex == _genotypeIntervalsBounds.size - 1) result[geneIndex] = _genotypeIntervalsBounds.size
                    else return@boundLoop
                else {
                    result[geneIndex] = boundIndex
                    return@breaking
                }
            } }
        } }

        return Phenotype(result)
    }

    operator fun Selector.invoke(individual: Individual): Int {
        val load = IntArray(_problemInfo.processorsNumber)
        individual.phenotype.forEachIndexed { taskIndex, processorIndex -> 
            load[processorIndex] += _problemInfo.weightMatrix[taskIndex, processorIndex]
        }

        return load.max()
    }

    operator fun Selector.invoke(candidates: MutableList<Individual>): List<Individual> { 
        candidates.sort()

        val individuals = candidates.slice(0 ..< _problemInfo.individualsNumber)
        individuals.forEachIndexed { individualIndex, individual -> individual.index = individualIndex }

        return individuals
    }

    operator fun Selector.invoke(population: Population): Individual = population.minBy { it.fitness }
}