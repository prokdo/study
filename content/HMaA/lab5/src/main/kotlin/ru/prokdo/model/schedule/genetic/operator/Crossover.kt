package ru.prokdo.model.schedule.genetic.operator


import ru.prokdo.model.schedule.genetic.individual.Genotype
import ru.prokdo.model.schedule.genetic.individual.Individual
import ru.prokdo.model.util.random.NumberGenerator


object Crossover {
    operator fun invoke(first: Individual, second: Individual): Individual {
        val firstChild = Individual("${first.name}_1", Genotype(Array(first.genotype.size) { 0 }))
        val secondChild = Individual("${first.name}_2", Genotype(Array(first.genotype.size) { 0 }))

        val swapPoint = NumberGenerator(first.genotype.size - 1).toInt()

        for (i in firstChild.genotype.indices)
            if (i <= swapPoint) firstChild.genotype[i] = first.genotype[i]
            else firstChild.genotype[i] = second.genotype[i]   
            
        for (i in secondChild.genotype.indices)
            if (i <= swapPoint) firstChild.genotype[i] = second.genotype[i]
            else firstChild.genotype[i] = first.genotype[i]

        return when (NumberGenerator(1).toInt()) {
            0 -> firstChild
            1 -> secondChild
            else -> throw IllegalArgumentException()
        }
    }
}