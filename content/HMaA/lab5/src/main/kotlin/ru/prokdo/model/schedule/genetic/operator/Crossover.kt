package ru.prokdo.model.schedule.genetic.operator


import ru.prokdo.model.schedule.genetic.individual.Genotype
import ru.prokdo.model.schedule.genetic.individual.Individual
import ru.prokdo.model.util.random.NumberGenerator


object Crossover {
    operator fun invoke(first: Individual, second: Individual): Pair< Pair<Int, Int>, Pair<Individual, Individual> > {
        val firstChild = Individual(first.index, Genotype(IntArray(first.genotype.size)))
        val secondChild = Individual(first.index, Genotype(IntArray(first.genotype.size)))

        val swapPoint = NumberGenerator(1, first.genotype.size - 2).toInt()

        for (i in firstChild.genotype.indices)
            if (i <= swapPoint) firstChild.genotype[i] = first.genotype[i]
            else firstChild.genotype[i] = second.genotype[i]   
            
        for (i in secondChild.genotype.indices)
            if (i <= swapPoint) secondChild.genotype[i] = second.genotype[i]
            else secondChild.genotype[i] = first.genotype[i]

        return Pair( Pair(swapPoint, second.index), Pair(firstChild, secondChild) )
    }
}