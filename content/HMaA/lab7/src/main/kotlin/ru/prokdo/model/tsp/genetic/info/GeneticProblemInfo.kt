package ru.prokdo.model.tsp.genetic.info

import ru.prokdo.model.tsp.info.ProblemInfo

data class  GeneticProblemInfo(
                        var problemInfo: ProblemInfo = ProblemInfo(),
                        var individualsNumber: Int = 0,
                        var limitNumber: Int = 0,
                        var crossoverProbability: Int = 0,
                        var mutationProbability: Int = 0
            ): GeneticInfo() {

    override fun toString(): String =
                                    """
                                        |$problemInfo

                                        |Количество особей в поколении (Z): $individualsNumber
                                        |Предельное число повтора лучшей особи (K): $limitNumber
                                        |Вероятность кроссовера (Pc, %): $crossoverProbability
                                        |Вероятность мутации (Pm, %): $mutationProbability
                                    """.trimMargin()
}
