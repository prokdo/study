package ru.prokdo.model.schedule.genetic.individual


import ru.prokdo.model.schedule.genetic.individual.Phenotype
import ru.prokdo.model.schedule.genetic.Solver.toPhenotype


class Individual {
    val index: Int

    val genotype: Genotype

    var phenotype: Phenotype?
        private set
    
        get() { 
            if (field == null) field = this.genotype.toPhenotype()

            return field
        }

    var fitness: Int = -1
        set(value: Int) {
            if (value <= 0) throw IllegalArgumentException("Individual fitness cannot be non-positive")

            field = value
        }

    constructor(index: Int, genotype: Genotype, phenotype: Phenotype? = null) { 
        this.index = index
        this.genotype = genotype
        this.phenotype = phenotype
     }

    fun clone(): Individual { 
        if (this.phenotype != null) return Individual(this.index, this.genotype.clone(), this.phenotype!!.clone())

        return Individual(this.index, this.genotype.clone())
    }

    override fun toString(): String {
        val builder = StringBuilder("Особь №${this.index}:\n")
        builder.append("\tГенотип: ${this.genotype}\n")
        builder.append("\tФенотип: ${this.phenotype}\n")
        builder.append("\tПриспособленность: ${this.fitness}")

        return builder.toString()
    }
}