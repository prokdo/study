package ru.prokdo.model.schedule.genetic.info

import ru.prokdo.model.schedule.genetic.individual.Individual

data class  MutationInfo(
                            val individual: Individual,
                            val chromosomeNumber: Int,
                            val genesNumber: Pair<Int, Int>,
                            val mutation: Individual
            ): Info() {
    override fun toString(): String =   """
                                            |Мутация особи №${individual.index + 1}:

                                            |Исходная особь: 
                                            |$individual

                                            |Место мутации: хромосома №$chromosomeNumber, гены №${genesNumber.first} и №${genesNumber.second}
                                            |$mutation
                                        """.trimMargin()
}