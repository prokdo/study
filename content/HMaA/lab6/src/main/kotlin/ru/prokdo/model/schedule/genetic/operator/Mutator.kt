package ru.prokdo.model.schedule.genetic.operator

import ru.prokdo.model.schedule.genetic.individual.Individual
import ru.prokdo.model.util.random.NumberGenerator

object Mutator {
    operator fun invoke(individual: Individual): Pair<Triple<Int, Int, Int>, Individual> {
        var genotype = individual.genotype.clone()

        val chromosomeNumber = NumberGenerator(genotype.size - 1)
        val chromosome = genotype[chromosomeNumber].toString(2).toCharArray()

        val firstGeneNumber = NumberGenerator(chromosome.size - 1)
        var secondGeneNumber = NumberGenerator(chromosome.size - 1)

        while (secondGeneNumber == firstGeneNumber) secondGeneNumber =
                NumberGenerator(chromosome.size - 1)

        chromosome[firstGeneNumber] =
                chromosome[secondGeneNumber].also {
                    chromosome[secondGeneNumber] = chromosome[firstGeneNumber]
                }

        genotype[chromosomeNumber] = chromosome.concatToString().toInt(2)

        return Pair(
                Triple(
                        chromosomeNumber + 1,
                        chromosome.size - (firstGeneNumber + 2),
                        chromosome.size - (secondGeneNumber + 2)
                ),
                Individual(individual.index, genotype)
        )
    }
}
