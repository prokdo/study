package ru.prokdo.model.schedule.genetic.individual

import ru.prokdo.model.schedule.genetic.GeneticSolver.toPhenotype
import ru.prokdo.model.schedule.genetic.GeneticSolver.invoke

import ru.prokdo.model.schedule.genetic.operator.Selector

class Individual(var index: Int, val genotype: Genotype) : Comparable<Individual> {
    val phenotype: Phenotype 
        get() = this.genotype.toPhenotype()

    val fitness: Int 
        get() = Selector(this)

    init {
        if (this.index < 0) throw IllegalArgumentException("Individual index cannot be negative")
    }

    fun clone(): Individual = Individual(this.index, this.genotype.clone())

    override fun toString(): String {
        val builder = StringBuilder("Особь №${this.index + 1}:\n")
        builder.append("\tГенотип: ${this.genotype}\n")
        builder.append("\tФенотип: ${this.phenotype}\n")
        builder.append("\tПриспособленность: ${this.fitness}")

        return builder.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (this.javaClass != other?.javaClass) return false

        other as Individual

        if (this.index != other.index) return false
        if (this.genotype != other.genotype) return false

        return true
    }

    override fun compareTo(other: Individual): Int = when {
        this.fitness < other.fitness -> -1
        this.fitness > other.fitness -> 1
        else -> 0
    }
}
