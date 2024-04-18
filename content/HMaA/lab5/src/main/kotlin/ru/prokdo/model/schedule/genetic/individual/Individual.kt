package ru.prokdo.model.schedule.genetic.individual


import ru.prokdo.model.schedule.genetic.individual.Phenotype


class Individual {
    var name: String = "null"
        set(value: String) {
            if (value.isBlank() || value.isEmpty()) throw IllegalArgumentException("Individual name cannot be blank or empty")
            
            field = value
        }

    val genotype: Genotype

    var phenotype: Phenotype?
        private set()
    
        get() { 
            if (field == null) field = this.genotype.toPhenotype()

            return field
        }

    var fitness: Int = -1
        private set()

        get() {
            if (field == -1) field = this.
        }

    constructor(name: String, genotype: Genotype, phenotype: Phenotype? = null) { 
        if (name.isBlank() || name.isEmpty()) throw IllegalArgumentException("Individual name cannot be blank or empty")

        this.name = name
        this.genotype = genotype
        this.phenotype = phenotype
     }

    override fun toString(): String {
        val builder = StringBuilder("Особь ${this.name}:\n")
        builder.append("\tГенотип: ${this.genotype}\n")
        builder.append("\tФенотип: ${this.phenotype}")

        return builder.toString()
    }
}