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

        val genotype = individual.genotype.clone()
        
        val chromosome = genotype[chromosomeIndex].toString(2).toCharArray()

        val geneIndex = NumberGenerator(chromosome.size - 1)

        if (chromosome[geneIndex] == '0') chromosome[geneIndex] = '1'
        else chromosome[geneIndex] = '0'

        genotype[chromosomeIndex] = chromosome.concatToString().toInt(2)

        return  MutationInfo(
                                individual,
                                chromosomeIndex,
                                chromosome.size - (geneIndex + 1), 										
                                Individual(individual.index, genotype)
                )


    }
}
