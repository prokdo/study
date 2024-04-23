package ru.prokdo.model.schedule.genetic.operator


import ru.prokdo.model.schedule.genetic.individual.Genotype
import ru.prokdo.model.schedule.genetic.individual.Individual
import ru.prokdo.model.util.random.NumberGenerator


object Crossover {
    operator fun invoke(first: Individual, second: Individual): Array<Any> {
        val firstChild = Individual(first.index, Genotype(IntArray(first.genotype.size)))
        val secondChild = Individual(first.index, Genotype(IntArray(first.genotype.size)))

        val swapPoint = NumberGenerator(first.genotype.size - 1).toInt()

        for (i in firstChild.genotype.indices)
            if (i <= swapPoint) firstChild.genotype[i] = first.genotype[i]
            else firstChild.genotype[i] = second.genotype[i]   
            
        for (i in secondChild.genotype.indices)
            if (i <= swapPoint) secondChild.genotype[i] = first.genotype[i]
            else second.genotype[i] = second.genotype[i]

        return arrayOf<Any>(swapPoint, Pair(firstChild, secondChild))
    }
}