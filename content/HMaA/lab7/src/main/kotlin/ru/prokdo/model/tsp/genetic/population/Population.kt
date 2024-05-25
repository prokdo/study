package ru.prokdo.model.tsp.genetic.population

import ru.prokdo.model.tsp.genetic.GeneticSolver.invoke
import ru.prokdo.model.tsp.genetic.individual.Individual
import ru.prokdo.model.tsp.genetic.individual.Genotype
import ru.prokdo.model.tsp.genetic.operator.Selector

class Population : Iterable<Individual> {
    val index: Int

    val size: Int
    private val _data: Array<Individual>
    val indices: IntRange
    
    val best: Individual
        get() = Selector(this)

    constructor(index: Int, size: Int) {
        if (index < 0) throw IllegalArgumentException("Population index cannot be negative")

        if (size <= 0) throw IllegalArgumentException("Population size cannot be zero or negative")

        this.index = index
        this.size = size

        this._data = Array<Individual>(size) { Individual(0, Genotype(IntArray(0))) }
        this.indices = this._data.indices
    }

    constructor(index: Int, data: Array<Individual>) {
        if (index < 0) throw IllegalArgumentException("Population index cannot be negative")

        if (data.size == 0) throw IllegalArgumentException("Population cannot be empty")

        this.index = index
        this.size = data.size

        this._data = data
        this.indices = this._data.indices
    }

    constructor(index: Int, data: Iterable<Individual>) {
        if (index < 0) throw IllegalArgumentException("Population index cannot be negative")

        this.size = data.count()

        if (this.size == 0) throw IllegalArgumentException("Population cannot be empty")

        this.index = index

        this._data = data.map{individual -> individual}.toTypedArray()
        this.indices = this._data.indices
    }

    fun random(): Individual = this._data.random()

    operator fun get(index: Int) = this._data[index]

    operator fun set(index: Int, value: Individual) { this._data[index] = value }

    fun clone(): Population =   Population(
                                            index, 
                                            this._data.clone()
                                )

    override fun toString(): String {
        val builder = StringBuilder("Популяция №${this.index}:\n\n")
        
        this._data.forEach { builder.append("${it}\n\n") }

        builder.append("Лучшая приспособленность в популяции: ${this.best.fitness}")

        return builder.toString()
    }

    override fun iterator(): Iterator<Individual> = this._data.iterator()
}