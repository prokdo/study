package ru.prokdo.model.tsp.genetic.operator

import ru.prokdo.model.tsp.genetic.individual.Individual
import ru.prokdo.model.tsp.genetic.info.MutationInfo
import ru.prokdo.model.util.random.NumberGenerator

object Mutator {
    operator fun invoke(individual: Individual): MutationInfo {
        val genotype = individual.genotype.clone()

        val firstGeneIndex = NumberGenerator(1, individual.genotype.size - 2)
        var secondGeneIndex = NumberGenerator(1, individual.genotype.size - 2)
        while (firstGeneIndex == secondGeneIndex) { secondGeneIndex = NumberGenerator(1, individual.genotype.size - 2) }

        genotype[firstGeneIndex] = genotype[secondGeneIndex].also { genotype[secondGeneIndex] = genotype[firstGeneIndex] }

        return when {
            firstGeneIndex < secondGeneIndex -> MutationInfo(
                                                            individual, 
                                                            Pair(firstGeneIndex, secondGeneIndex), 
                                                            Individual(individual.index, genotype)
                                                )
            
            firstGeneIndex > secondGeneIndex -> MutationInfo(
                                                            individual, 
                                                            Pair(secondGeneIndex, firstGeneIndex), 
                                                            Individual(individual.index, genotype)
                                                )

            else -> throw IllegalStateException()
        }
    }
}
