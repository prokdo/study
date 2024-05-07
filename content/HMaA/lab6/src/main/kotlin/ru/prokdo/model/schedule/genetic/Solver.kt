package ru.prokdo.model.schedule.genetic

import kotlin.system.measureTimeMillis

import ru.prokdo.model.math.IntMatrix
import ru.prokdo.model.util.info.ProblemInfo
import ru.prokdo.model.util.info.ResultInfo
import ru.prokdo.model.util.log.Logger
import ru.prokdo.model.util.random.NumberGenerator
import ru.prokdo.model.schedule.genetic.individual.Genotype
import ru.prokdo.model.schedule.genetic.individual.Phenotype
import ru.prokdo.model.schedule.genetic.individual.Individual
import ru.prokdo.model.schedule.genetic.operator.Selector
import ru.prokdo.model.schedule.genetic.operator.Crossover
import ru.prokdo.model.schedule.genetic.operator.Mutator
import ru.prokdo.model.schedule.population.Population

object Solver {
    private const val MAX_GENOTYPE_VALUE = 255
    
    private val log = Logger()

    private var problemInfo: ProblemInfo? = null

    private var genotypeIntervalsBounds: IntArray? = null
        get() {
            if (field != null) return field

            if (Solver.problemInfo == null) 
                throw NullPointerException("To translate a genotype into a phenotype, information about the problem being solved is needed")

            val remainder = (Solver.MAX_GENOTYPE_VALUE + 1) % Solver.problemInfo!!.processorsNumber
            val result = IntArray(Solver.problemInfo!!.processorsNumber - 1)
            val incPos = result.size - (remainder + 1)

            for (index in result.indices) {
                result[index] = (index + 1) * (Solver.MAX_GENOTYPE_VALUE / Solver.problemInfo!!.processorsNumber)

                if (index > incPos) result[index]++
            }

            return result
        }

    operator fun invoke(problemInfo: ProblemInfo): ResultInfo {
        Solver.problemInfo = problemInfo
        var output: String

        val finalPopulation: Population
        val elapsedTime = measureTimeMillis { 
            var index = 1
            var currentPopulation = Population(0, problemInfo.individualsNumber)

            for (individIndex in currentPopulation.indices) {
                val genes = IntArray(problemInfo.tasksNumber)
                for (geneIndex in genes.indices) genes[geneIndex] = NumberGenerator(Solver.MAX_GENOTYPE_VALUE).toInt()

                currentPopulation[individIndex] = Selector(mutableListOf(Individual(individIndex + 1, Genotype(genes))))
            }

            var count = 0
            while (count < problemInfo.limitNumber) {
                output = """
                            |${currentPopulation.toString()}


                            |Формирование нового поколения:
                         """.trimMargin()

                log.append("$output\n\n\n")
                print("$output\n\n\n")

                val nextPopulation = Population(index, problemInfo.individualsNumber)

                currentPopulation.forEachIndexed { individIndex, individual -> run {
                    val candidates = mutableListOf<Individual>(individual)

                    val crossoverProbability = NumberGenerator(100).toInt()
                    var crossover: Pair< Pair<Int, Int>, Pair<Individual, Individual> >? = null
                    if (crossoverProbability <= problemInfo.crossoverProbability) {
                        val partner: Individual
                        while (true) {
                            val partnerIndex = NumberGenerator(currentPopulation.size - 1).toInt()

                            if (partnerIndex == individIndex) continue
                            partner = currentPopulation[partnerIndex]
                            break
                        }

                        crossover = Crossover(individual, partner)
                        candidates.add(crossover.second.first)
                        candidates.add(crossover.second.second)
                    }

                    val mutationProbability = NumberGenerator(100).toInt()
                    var mutation: Pair< Pair<Int, Int>, Individual >? = null
                    if (mutationProbability <= problemInfo.mutationProbability) {
                        mutation = Mutator(individual)
                        candidates.add(mutation.second)
                    }

                    nextPopulation[individIndex] = Selector(candidates)

                    if (crossover != null) {
                        output = """
                                    |Кроссовер между особями №${individual.index} и №${crossover.first.second} в точке ${crossover.first.first + 1}:

                                    |Родители:

                                    |$individual

                                    |${currentPopulation[crossover.first.second - 1]}

                                    |Потомки:

                                    |${crossover.second.first}

                                    |${crossover.second.second}
                                 """.trimMargin()

                        log.append("$output\n\n\n")
                        print("$output\n\n\n")
                    }

                    if (mutation != null) {
                        output = """
                                    |Мутация особи №${individual.index}:

                                    |Исходная особь: 
                                    |$individual

                                    |Место мутации: хромосома №${mutation.first.first}, ген №${mutation.first.second + 1}
                                    |${mutation.second}
                                 """.trimMargin()

                        log.append("$output\n\n\n")
                        print("$output\n\n\n")
                    }
                } }

                if (currentPopulation.best!!.fitness == nextPopulation.best!!.fitness) count++
                else count = 0

                currentPopulation = nextPopulation
                index++
            }

            finalPopulation = currentPopulation

            output = "${currentPopulation.toString()}\n\n\n"
            log.append("$output")
            print(output)
        }

        val result = IntMatrix(problemInfo.tasksNumber, problemInfo.processorsNumber)
        finalPopulation.best!!.phenotype!!.forEachIndexed { 
            taskIndex, processorNumber -> result[taskIndex, processorNumber - 1] = Solver.problemInfo!!.weightMatrix[taskIndex, processorNumber - 1]
        }

        return ResultInfo(problemInfo, finalPopulation.index, result, finalPopulation.best!!.fitness.toDouble(), elapsedTime, log.toString())
    }

    fun Genotype.toPhenotype(): Phenotype {
        val result = IntArray(this.size)

        for (geneIndex in result.indices) 
            for (boundIndex in Solver.genotypeIntervalsBounds!!.indices) {
                if (this[geneIndex] > Solver.genotypeIntervalsBounds!![boundIndex])
                    if (boundIndex == Solver.genotypeIntervalsBounds!!.size - 1) result[geneIndex] = Solver.genotypeIntervalsBounds!!.size + 1
                    else continue
                else {
                    result[geneIndex] = boundIndex + 1
                    break
                }
            }

        return Phenotype(result)
    }

    operator fun Selector.invoke(candidates: MutableList<Individual>): Individual {
        candidates.forEach { candidate -> run { 
            val load = IntArray(Solver.problemInfo!!.processorsNumber)
            candidate.phenotype!!.forEachIndexed { taskIndex, processorNumber -> run { 
                load[processorNumber - 1] += Solver.problemInfo!!.weightMatrix[taskIndex, processorNumber - 1].toInt() } }
            
                candidate.fitness = load.max()
        } }

        return candidates.minBy { candidate -> candidate.fitness }
    }
}