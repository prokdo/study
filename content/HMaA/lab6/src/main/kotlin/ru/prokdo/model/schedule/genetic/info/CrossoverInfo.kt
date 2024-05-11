package ru.prokdo.model.schedule.genetic.info

import ru.prokdo.model.schedule.genetic.individual.Individual

data class  CrossoverInfo(
                            val parents: Pair<Individual, Individual>,
                            val swapPoints: Pair<Int, Int>,
                            val children: Pair<Individual, Individual>
            ): Info() {
    override fun toString(): String =   """
                                            |Кроссовер между особями №${parents.first.index + 1} и №${parents.second.index + 1} в точках ${swapPoints.first + 1} и ${swapPoints.second + 1}:

                                            |Родители:
                    
                                            |${parents.first}
                    
                                            |${parents.second}
                    
                                            |Потомки:
                    
                                            |${children.first}
                    
                                            |${children.second}
                                        """.trimMargin()
}