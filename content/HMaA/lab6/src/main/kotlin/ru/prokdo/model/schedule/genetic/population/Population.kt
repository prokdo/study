package ru.prokdo.model.schedule.population

import ru.prokdo.model.schedule.genetic.individual.Individual
import ru.prokdo.model.schedule.genetic.individual.Genotype
import ru.prokdo.model.schedule.genetic.Solver

class Population(val index: Int, val size: Int, data: Array<Individual>? = null) : Iterable<Individual> {
    var indices: IntRange = 0 .. 0
        private set
    
    var best: Individual? = null
        private set

        get() {
            if (field != null) return field

            field = this.data!!.minBy { individual -> individual.fitness }

            return field
        }

    private var data: Array<Individual>? = null
        private set(value: Array<Individual>?) {
            if (value != null) this.indices = value.indices

            field = value
        }

    init {
        if (index < 0) throw IllegalArgumentException("Population index cannot be negative")

        if (size <= 0) throw IllegalArgumentException("Population size cannot be zero or negative")

        if (data == null) this.data = Array<Individual>(size) { Individual(-1, Genotype(IntArray(0))) }
        else this.data = data
    }

    operator fun get(index: Int) = this.data!![index]

    operator fun set(index: Int, value: Individual) { this.data!![index] = value }

    fun clone(): Population = Population(
                                         index, 
                                         size,
                                         this.data!!.clone())

    override fun toString(): String {
        val builder = StringBuilder("Популяция №${this.index}:\n\n")
        
        this.data!!.forEach { builder.append("${it}\n\n") }

        builder.append("Лучшая приспособленность в популяции: ${this.best!!.fitness}")

        return builder.toString()
    }

    override fun iterator(): Iterator<Individual> = this.data!!.iterator()
}