package ru.prokdo.model.tsp.genetic.info

import ru.prokdo.model.tsp.genetic.individual.Individual

data class  MutationInfo(
                            val individual: Individual,
                            val genesIndexes: Pair<Int, Int>,
                            val mutation: Individual
            ): GeneticInfo() {
    override fun toString(): String =   """
                                            |Мутация особи №${individual.index + 1}:

                                            |Исходная особь: 
                                            |$individual

                                            |Место мутации: хромосомы №${genesIndexes.first + 1} и №${genesIndexes.second + 1}
                                            |$mutation
                                        """.trimMargin()
}