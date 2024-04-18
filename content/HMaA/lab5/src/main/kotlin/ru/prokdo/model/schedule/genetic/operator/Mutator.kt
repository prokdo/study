package ru.prokdo.model.schedule.genetic.operator


import ru.prokdo.model.schedule.genetic.individual.Individual
import ru.prokdo.model.util.random.NumberGenerator


object Mutator {
    operator fun invoke(individual: Individual) {
        val chromosomeNumber = NumberGenerator(individual.genotype.size - 1).toInt()
        val chromosome = individual.genotype[chromosomeNumber].toString(2).toCharArray()
        
        val genNumber = NumberGenerator(chromosome.size - 1).toInt()
        if (chromosome[genNumber] == '0') chromosome[genNumber] = '1'
        else chromosome[genNumber] = '0'

        individual.genotype[chromosomeNumber] = chromosome.concatToString().toInt(2)
    }
}