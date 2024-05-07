package ru.prokdo.model.schedule.genetic.individual

data class Phenotype(val data: IntArray) : Iterable<Int> {
    val indices: IntRange = this.data.indices

    val size: Int = this.data.size
    
    operator fun get(index: Int): Int = this.data[index] 
    
    fun clone(): Phenotype = Phenotype(this.data.clone())

    override fun toString(): String {
        val builder = StringBuilder("[")

        this.data.forEachIndexed { index, value -> run { builder.append(value)
                                                         if (index != this.data.size - 1) builder.append(" ")
                                                         else builder.append("]") } }

        return builder.toString()
    }

    override fun iterator(): Iterator<Int> = this.data.iterator()
}