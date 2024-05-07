package ru.prokdo.model.schedule.genetic.operator

import ru.prokdo.model.schedule.genetic.individual.Genotype
import ru.prokdo.model.schedule.genetic.individual.Individual
import ru.prokdo.model.util.random.NumberGenerator

object Crossover {
    operator fun invoke(first: Individual, second: Individual): Pair< Pair<Int, Int>, Pair<Individual, Individual> > {
        val firstChild = Individual(first.index, Genotype(IntArray(first.genotype.size)))
        val secondChild = Individual(first.index, Genotype(IntArray(first.genotype.size)))

        val firstSwapPoint = NumberGenerator(1, first.genotype.size - 2)
        var secondSwapPoint = NumberGenerator(1, first.genotype.size - 2)
        
        while (secondSwapPoint == firstSwapPoint) { secondSwapPoint = NumberGenerator(1, first.genotype.size - 2) }

        return when {
            firstSwapPoint < secondSwapPoint -> { 
                first.genotype.forEachIndexed { index, value ->
                    if (index <= firstSwapPoint || index > secondSwapPoint) firstChild.genotype[index] = value
                    else firstChild.genotype[index] = second.genotype[index]
                }

                second.genotype.forEachIndexed { index, value ->
                    if (index <= firstSwapPoint || index > secondSwapPoint) secondChild.genotype[index] = value
                    else secondChild.genotype[index] = first.genotype[index]
                }

                Pair( Pair(firstSwapPoint, secondSwapPoint), Pair(firstChild, secondChild) )
            }

            firstSwapPoint > secondSwapPoint -> {
                first.genotype.forEachIndexed { index, value ->
                    if (index <= secondSwapPoint || index > firstSwapPoint) firstChild.genotype[index] = value
                    else firstChild.genotype[index] = second.genotype[index]
                }

                second.genotype.forEachIndexed { index, value ->
                    if (index <= secondSwapPoint || index > firstSwapPoint) secondChild.genotype[index] = value
                    else secondChild.genotype[index] = first.genotype[index]
                }

                Pair( Pair(secondSwapPoint, firstSwapPoint), Pair(firstChild, secondChild) )
            }

            else -> throw IllegalStateException()
        }
    }
}