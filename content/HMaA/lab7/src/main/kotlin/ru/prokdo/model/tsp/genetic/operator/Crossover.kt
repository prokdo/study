package ru.prokdo.model.tsp.genetic.operator

import ru.prokdo.model.tsp.genetic.info.CrossoverInfo
import ru.prokdo.model.tsp.genetic.individual.Genotype
import ru.prokdo.model.tsp.genetic.individual.Individual
import ru.prokdo.model.util.random.NumberGenerator

object Crossover {
    operator fun invoke(first: Individual, second: Individual): CrossoverInfo  {
        val firstChild = Individual(first.index, Genotype(IntArray(first.genotype.size)))
        val secondChild = Individual(first.index, Genotype(IntArray(first.genotype.size)))

        val swapPoint = NumberGenerator(1, first.genotype.size - 2)

        first.genotype.forEachIndexed { index, firstValue ->
            if (index <= swapPoint || index == first.genotype.size - 1) {
                firstChild.genotype[index] = firstValue
                secondChild.genotype[index] = second.genotype[index]
            } else {
                if (!firstChild.genotype.contains(second.genotype[index]))
                    firstChild.genotype[index] = second.genotype[index]
                else {
                    var firstIndex = swapPoint + 1
                    while (firstChild.genotype.contains(first.genotype[firstIndex])) {
                        if (firstIndex >= first.genotype.size - 2) {
                            firstChild.genotype[index] = firstValue
                            break
                        }
                        firstIndex++
                    }
                    firstChild.genotype[index] = first.genotype[firstIndex]
                }

                if (!secondChild.genotype.contains(firstValue))
                    secondChild.genotype[index] = firstValue
                else {
                    var secondIndex = swapPoint + 1
                    while (secondChild.genotype.contains(second.genotype[secondIndex])) {
                        if (secondIndex >= first.genotype.size - 2) {
                            secondChild.genotype[index] = second.genotype[index]
                            break
                        }
                        secondIndex++
                    }
                    secondChild.genotype[index] = second.genotype[secondIndex]
                }
            }
        }

        return  CrossoverInfo(
                            Pair(first, second), 
                            swapPoint, 
                            Pair(firstChild, secondChild)
                )
    }
}
