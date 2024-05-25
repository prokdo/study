package ru.prokdo.model.tsp.genetic.info

import ru.prokdo.model.math.IntMatrix
import ru.prokdo.model.tsp.info.ResultInfo

data class  GeneticResultInfo(
                            var generationsNumber: Int = 0,
                            var resultInfo: ResultInfo = ResultInfo()
            ): GeneticInfo() {

    override fun toString(): String =
                                    """
                                        |Количество поколений решения: $generationsNumber
                                        |$resultInfo
                                    """.trimMargin()
}
