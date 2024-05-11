package ru.prokdo.model.schedule.genetic.operator

import ru.prokdo.model.schedule.genetic.individual.Individual
import ru.prokdo.model.schedule.genetic.info.MutationInfo
import ru.prokdo.model.util.random.NumberGenerator

object Mutator {
    operator fun invoke(individual: Individual): MutationInfo {
        val genotype = individual.genotype.clone()

        val chromosomeNumber = NumberGenerator(genotype.size - 1)
        val chromosome = genotype[chromosomeNumber].toString(2).toCharArray()

        val firstGeneNumber = NumberGenerator(chromosome.size - 1)
        var secondGeneNumber = NumberGenerator(chromosome.size - 1)

        while (secondGeneNumber == firstGeneNumber || chromosome[firstGeneNumber] == chromosome[secondGeneNumber]) 
            secondGeneNumber = NumberGenerator(chromosome.size - 1)

        chromosome[firstGeneNumber] = chromosome[secondGeneNumber]
            .also { chromosome[secondGeneNumber] = chromosome[firstGeneNumber] }

        genotype[chromosomeNumber] = chromosome.concatToString().toInt(2)

        return when {
            firstGeneNumber < secondGeneNumber ->   MutationInfo(
																	individual,
																	chromosomeNumber,
																	Pair(
																			chromosome.size - (secondGeneNumber + 1), 
																			chromosome.size - (firstGeneNumber + 1)
																	),
																	Individual(individual.index, genotype)
                                                    )
                
            firstGeneNumber > secondGeneNumber ->   MutationInfo(
																	individual,
																	chromosomeNumber,
																	Pair(
																			chromosome.size - (firstGeneNumber + 1), 
																			chromosome.size - (secondGeneNumber + 1)
																	),
																Individual(individual.index, genotype)
                                                    )

            else -> throw IllegalStateException()
        }
    }
}
