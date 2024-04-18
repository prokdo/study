package ru.prokdo.model.schedule.genetic.individual


data class Phenotype(val data: Array<Int>) {
    val indices: IntRange
        get() { return this.data.indices }

    val size: Int
        get() { return this.data.size }
    
    operator fun get(index: Int): Int { return this.data[index] }
    
    override fun toString(): String {
        val builder = StringBuilder("[")

        this.data.forEachIndexed { index, value -> run { builder.append(value)
                                                         if (index != this.data.size - 1) builder.append(", ")
                                                         else builder.append("]") } }

        return builder.toString()
    }
}