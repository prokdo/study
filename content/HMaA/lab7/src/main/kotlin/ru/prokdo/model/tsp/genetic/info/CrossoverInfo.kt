package ru.prokdo.model.tsp.genetic.info

import ru.prokdo.model.tsp.genetic.individual.Individual

data class  CrossoverInfo(
                            val parents: Pair<Individual, Individual>,
                            val swapPoint: Int,
                            val children: Pair<Individual, Individual>
            ): GeneticInfo() {
    override fun toString(): String =   """
                                            |Кроссовер между особями №${parents.first.index + 1} и №${parents.second.index + 1} в точке №${swapPoint + 1}:

                                            |Родители:
                    
                                            |${parents.first}

                                            |${parents.second}

                                            |Потомки:
                    
                                            |${children.first}

                                            |${children.second}
                                        """.trimMargin()
}