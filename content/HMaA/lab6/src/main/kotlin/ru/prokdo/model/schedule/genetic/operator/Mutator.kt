package ru.prokdo.model.schedule.genetic.operator

import kotlin.math.log2
import kotlin.math.ceil
import kotlin.math.floor

import ru.prokdo.model.schedule.genetic.individual.Individual
import ru.prokdo.model.schedule.genetic.info.MutationInfo
import ru.prokdo.model.util.random.NumberGenerator

object Mutator {
    operator fun invoke(individual: Individual): MutationInfo {
        val chromosomeIndex = NumberGenerator(individual.genotype.size - 1)

        val logarithm = log2(individual.genotype[chromosomeIndex] + 1.0)
        if (individual.genotype[chromosomeIndex] == 0 || floor(logarithm) == ceil(logarithm))
            return  MutationInfo(
                                    individual,
                                    chromosomeIndex,
                                    Pair(0, log2(individual.genotype[chromosomeIndex] + 1.0).toInt()),
                                    individual
                    )

        val genotype = individual.genotype.clone()
        
        val chromosome = genotype[chromosomeIndex].toString(2).toCharArray()

        val firstGeneIndex = NumberGenerator(chromosome.size - 1)
        var secondGeneIndex = NumberGenerator(chromosome.size - 1)

        while (secondGeneIndex == firstGeneIndex || chromosome[firstGeneIndex] == chromosome[secondGeneIndex]) 
            secondGeneIndex = NumberGenerator(chromosome.size - 1)

        chromosome[firstGeneIndex] = chromosome[secondGeneIndex]
            .also { chromosome[secondGeneIndex] = chromosome[firstGeneIndex] }

        genotype[chromosomeIndex] = chromosome.concatToString().toInt(2)

        return when {
            firstGeneIndex < secondGeneIndex ->   MutationInfo(
																	individual,
																	chromosomeIndex,
																	Pair(
																			chromosome.size - (secondGeneIndex + 1), 
																			chromosome.size - (firstGeneIndex + 1)
																	),
																	Individual(individual.index, genotype)
                                                    )
                
            firstGeneIndex > secondGeneIndex ->   MutationInfo(
																	individual,
																	chromosomeIndex,
																	Pair(
																			chromosome.size - (firstGeneIndex + 1), 
																			chromosome.size - (secondGeneIndex + 1)
																	),
																Individual(individual.index, genotype)
                                                    )

            else -> throw IllegalStateException()
        }
    }
}
