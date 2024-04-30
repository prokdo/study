package ru.prokdo.model.schedule.genetic.operator


import ru.prokdo.model.schedule.genetic.individual.Individual
import ru.prokdo.model.util.random.NumberGenerator


object Mutator {
    operator fun invoke(individual: Individual): Pair< Pair<Int, Int>, Individual > {
        var genotype = individual.genotype.clone()

        val chromosomeNumber = NumberGenerator(genotype.size - 1).toInt()
        val chromosome = genotype[chromosomeNumber].toString(2).toCharArray()
        
        val geneNumber = NumberGenerator(chromosome.size - 1).toInt()
        if (chromosome[geneNumber] == '0') chromosome[geneNumber] = '1'
        else chromosome[geneNumber] = '0'

        genotype[chromosomeNumber] = chromosome.concatToString().toInt(2)

        return Pair( Pair( chromosomeNumber + 1, chromosome.size - (geneNumber + 2) ), Individual(individual.index, genotype) )
    }
}