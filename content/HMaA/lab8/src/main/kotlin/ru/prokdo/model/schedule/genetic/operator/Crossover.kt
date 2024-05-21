package ru.prokdo.model.schedule.genetic.operator

import ru.prokdo.model.schedule.genetic.individual.Genotype
import ru.prokdo.model.schedule.genetic.individual.Individual
import ru.prokdo.model.schedule.genetic.info.CrossoverInfo
import ru.prokdo.model.util.random.NumberGenerator

object Crossover {
    operator fun invoke(first: Individual, second: Individual): CrossoverInfo  {
        val firstChild = Individual(first.index, Genotype(IntArray(first.genotype.size)))
        val secondChild = Individual(first.index, Genotype(IntArray(first.genotype.size)))

        val swapPoint = NumberGenerator(1, first.genotype.size - 2)

        first.genotype.forEachIndexed { index, value -> run { 
            if (index <= swapPoint) {
                firstChild.genotype[index] = value
                secondChild.genotype[index] = second.genotype[index]
            } else {
                firstChild.genotype[index] = second.genotype[index]
                secondChild.genotype[index] = value
            }
        } }

        return  CrossoverInfo(
                                Pair(first, second), 
                                swapPoint, 
                                Pair(firstChild, secondChild)
                )
    }
}
