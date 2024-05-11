package ru.prokdo.model.schedule.genetic.individual

class Genotype : Iterable<Int> {
    private val _data: IntArray
    val indices: IntRange
    val size: Int

    constructor(size: Int) {
        if (size < 0) throw IllegalArgumentException("Phenotype size cannot be negative")

        this.size = size
        this._data = IntArray(size)
        this.indices = this._data.indices
    }

    constructor(data: IntArray) {
        this.size = data.size
        this._data = data
        this.indices = data.indices
    }

    constructor(data: Iterable<Int>) {
        this.size = data.count()
        this._data = data.map {value -> value}.toIntArray()
        this.indices = this._data.indices
    }

    fun random(): Int = this._data.random()

    operator fun get(index: Int): Int = this._data[index]

    operator fun set(index: Int, value: Int) { this._data[index] = value }

    fun clone(): Genotype = Genotype(this._data.clone())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (this.javaClass != other?.javaClass) return false

        other as Genotype

        if (this.size != other.size) return false
        if (!this._data.contentEquals(other._data)) return false

        return true
    }

    override fun hashCode(): Int = this._data.hashCode()

    override fun toString(): String {
        val builder = StringBuilder("[")

        this._data.forEachIndexed { index, value -> run {
                builder.append(value)
                if (index != this._data.size - 1) builder.append(" ") 
                else builder.append("]")
        } }

        return builder.toString()
    }

    override fun iterator(): Iterator<Int> = this._data.iterator()
}
