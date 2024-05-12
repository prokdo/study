package ru.prokdo.model.schedule.genetic.info

import ru.prokdo.model.schedule.genetic.GeneticSolver.toIntMatrix
import ru.prokdo.model.schedule.genetic.individual.Individual

data class  MutationInfo(
                            val individual: Individual,
                            val chromosomeIndex: Int,
                            val genesIndexes: Pair<Int, Int>,
                            val mutation: Individual
            ): Info() {
    override fun toString(): String =   """
                                            |Мутация особи №${individual.index + 1}:

                                            |Исходная особь: 
                                            |$individual

                                            |Конфигурация решения:
                                            |${individual.phenotype.toIntMatrix()}

                                            |Место мутации: хромосома №${chromosomeIndex + 1}, гены №${genesIndexes.first} и №${genesIndexes.second}
                                            |$mutation

                                            |Конфигурация решения:
                                            |${mutation.phenotype.toIntMatrix()}
                                        """.trimMargin()
}