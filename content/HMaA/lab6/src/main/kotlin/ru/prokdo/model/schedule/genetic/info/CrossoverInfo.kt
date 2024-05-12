package ru.prokdo.model.schedule.genetic.info

import ru.prokdo.model.schedule.genetic.GeneticSolver.toIntMatrix
import ru.prokdo.model.schedule.genetic.individual.Individual

data class  CrossoverInfo(
                            val parents: Pair<Individual, Individual>,
                            val swapPoints: Pair<Int, Int>,
                            val children: Pair<Individual, Individual>
            ): Info() {
    override fun toString(): String =   """
                                            |Кроссовер между особями №${parents.first.index + 1} и №${parents.second.index + 1} в точках №${swapPoints.first + 1} и №${swapPoints.second + 1}:

                                            |Родители:
                    
                                            |${parents.first}

                                            |Конфигурация решения:
                                            |${parents.first.phenotype.toIntMatrix()}
                    
                                            |${parents.second}

                                            |Конфигурация решения:
                                            |${parents.second.phenotype.toIntMatrix()}
                    
                                            |Потомки:
                    
                                            |${children.first}

                                            |Конфигурация решения:
                                            |${children.first.phenotype.toIntMatrix()}
                    
                                            |${children.second}

                                            |Конфигурация решения:
                                            |${children.second.phenotype.toIntMatrix()}
                                        """.trimMargin()
}