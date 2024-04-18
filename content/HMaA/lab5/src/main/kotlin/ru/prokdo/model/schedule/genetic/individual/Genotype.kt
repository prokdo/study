package ru.prokdo.model.schedule.genetic.individual


import ru.prokdo.model.schedule.genetic.individual.Genotype
import kotlin.collections.indices


data class Genotype(val data: Array<Int>) {
    val indices: IntRange
        get() { return this.data.indices }

    val size: Int
        get() { return this.data.size }

    companion object {
        var MAX_VALUE = 255
            set(value: Int) {
                if (value < 0) throw IllegalArgumentException("Max interval value cannot be negative")

                field = value
            }

        var INTERVALS_NUMBER = 0
            set(value: Int) {
                if (value < 0) throw IllegalArgumentException("Max interval value cannot be negative")

                field = value
            }

        var INTERVALS_BOUNDS: Array<Int>? = null
            private set()
            
            get() {
                if (field == null) field = Genotype.getIntervalsBounds()

                return field
            }

        private fun getIntervalsBounds(): Array<Int> {
            if (Genotype.INTERVALS_NUMBER == 0) return arrayOf(Genotype.MAX_VALUE)

            val result = Array(Genotype.INTERVALS_NUMBER - 1) { 0 }
            val boundStep = Genotype.MAX_VALUE + 1 / Genotype.INTERVALS_NUMBER
            val remainder = Genotype.MAX_VALUE + 1 % Genotype.INTERVALS_NUMBER

            for (i in result.indices) {
                result[i] = boundStep * (i + 1)

                if (i > result.size - remainder && remainder != 0) result[i]++
            }

            return result
        }
    }

    operator fun get(index: Int): Int { return this.data[index] }

    operator fun set(index: Int, value: Int) { this.data[index] = value }

    fun toPhenotype(): Phenotype {
        val result = Array(this.size) { 0 }

        for (dataIndex in this.indices)
            for (intervalIndex in Genotype.INTERVALS_BOUNDS!!.indices)
                if (this.data[dataIndex] <= Genotype.INTERVALS_BOUNDS!![intervalIndex]) {
                    result[dataIndex] = intervalIndex + 1

                    break
                }

        return Phenotype(data)
    }

    override fun toString(): String {
        val builder = StringBuilder("[")

        this.data.forEachIndexed { index, value -> run { builder.append(value)
                                                         if (index != this.data.size - 1) builder.append(", ")
                                                         else builder.append("]") } }

        return builder.toString()
    }
}