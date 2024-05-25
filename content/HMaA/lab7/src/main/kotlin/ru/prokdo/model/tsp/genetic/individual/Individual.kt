package ru.prokdo.model.tsp.genetic.individual

import org.graphstream.graph.implementations.MultiGraph

import ru.prokdo.model.tsp.genetic.GeneticSolver.toPhenotype
import ru.prokdo.model.tsp.genetic.GeneticSolver.invoke
import ru.prokdo.model.tsp.genetic.operator.Selector

class Individual(var index: Int, val genotype: Genotype) : Comparable<Individual> {
    val phenotype: MultiGraph by lazy { this.genotype.toPhenotype() }

    val fitness: Int by lazy { Selector(this) }

    init {
        if (this.index < 0) throw IllegalArgumentException("Individual index cannot be negative")
    }

    fun clone(): Individual = Individual(this.index, this.genotype.clone())

    override fun toString(): String =
                                    """
                                        |Особь №${index + 1}:
                                        |   Генотип: $genotype
                                        |   Приспособленность: $fitness
                                    """.trimMargin()

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
