package ru.prokdo.model.math

class IntMatrix : Iterable<IntArray> {
    val height: Int
    val width: Int

    val size: Pair<Int, Int>

    private val _data: Array<IntArray>
    val data: Array<IntArray>
        get() = this._data.map { row -> row.clone() }.toTypedArray()

    val rowIndices: IntRange
    val columnIndices: IntRange

    val indices: Pair<IntRange, IntRange>

    private var _transposed: IntMatrix?

    constructor(data: Iterable<Iterable<Int>>) {
        var height = 1
        val width = data.elementAt(0).count()
        data.forEachIndexed { rowIndex, row -> run {
            if (rowIndex != 0)
                if (row.count() != width) throw IllegalArgumentException("Data param must has same number of elements in each row")
            height++
        } }

        this.height = height
        this.width = width

        this.size = Pair(this.height, this.width)

        this._data = Array(this.height) { IntArray(this.width) }
        data.forEachIndexed { rowIndex, row -> row.forEachIndexed { columnIndex, value -> 
            this._data[rowIndex][columnIndex] = value 
        } }

        this.rowIndices = this._data.indices
        if (this.height != 0) this.columnIndices = this._data[0].indices
        else this.columnIndices = 0 .. -1

        this.indices = Pair(this.rowIndices, this.columnIndices)

        this._transposed = null
    }

    constructor(data: Array<Iterable<Int>>) {
        val height = data.size
        val width = data.elementAt(0).count()
        data.forEachIndexed { rowIndex, row ->
            if (rowIndex != 0)
                if (row.count() != width) throw IllegalArgumentException("Data param must has same number of elements in each row")
        }

        this.height = height
        this.width = width

        this.size = Pair(this.height, this.width)

        this._data = Array(this.height) { IntArray(this.width) }
        data.forEachIndexed { rowIndex, row -> row.forEachIndexed { columnIndex, value -> 
            this._data[rowIndex][columnIndex] = value
        } }

        this.rowIndices = this._data.indices
        if (this.height != 0) this.columnIndices = this._data[0].indices
        else this.columnIndices = 0 .. -1

        this.indices = Pair(this.rowIndices, this.columnIndices)

        this._transposed = null
    }

    constructor(data: Array<IntArray>) {
        val height = data.size
        val width = data[0].size
        data.forEachIndexed { rowIndex, row ->
            if (rowIndex != 0)
                if (row.size != width) throw IllegalArgumentException( "Data param must has same number of elements in each row")
        }

        this.height = height
        this.width = width

        this.size = Pair(this.height, this.width)

        this._data = Array(this.height) { IntArray(this.width) }
        data.forEachIndexed { rowIndex, row -> row.forEachIndexed { columnIndex, value -> 
            this._data[rowIndex][columnIndex] = value 
        } }

        this.rowIndices = this._data.indices
        if (this.height != 0) this.columnIndices = this._data[0].indices
        else this.columnIndices = 0 .. -1

        this.indices = Pair(this.rowIndices, this.columnIndices)

        this._transposed = null
    }

    constructor(height: Int, width: Int) {
        if (height < 0 || width < 0)
            throw IllegalArgumentException("Matrix dimensions cannot be less than zero")

        this.height = height
        this.width = width

        this.size = Pair(this.height, this.width)

        this._data = Array(this.height) { IntArray(this.width) }

        this.rowIndices = this._data.indices
        if (this.height != 0) this.columnIndices = this._data[0].indices
        else this.columnIndices = 0 .. -1

        this.indices = Pair(this.rowIndices, this.columnIndices)

        this._transposed = null
    }

    override fun iterator(): Iterator<IntArray> = this._data.iterator()

    companion object {
        const val POSITIVE_INFINITY = Int.MAX_VALUE
        const val NEGATIVE_INFINITY = Int.MIN_VALUE

        fun combineHorizontally(matricesToCombine: Array<IntMatrix>): IntMatrix {
            val height = matricesToCombine.maxOf { matrix -> matrix.height }
            val width = matricesToCombine.sumOf { matrix -> matrix.width }

            val result = IntMatrix(height, width)
            var filledWidth = 0
            matricesToCombine.forEach { matrix -> matrix.forEachIndexed { rowIndex, row -> run {
                row.forEachIndexed { columnIndex, value -> result[rowIndex, columnIndex + filledWidth] = value }

                filledWidth += matrix.width
            } } }

            return result
        }

        fun combineHorizontally(matricesToCombine: Iterable<IntMatrix>): IntMatrix {
            val height = matricesToCombine.maxOf { matrix -> matrix.height }
            val width = matricesToCombine.sumOf { matrix -> matrix.width }

            val result = IntMatrix(height, width)
            var filledWidth = 0
            matricesToCombine.forEach { matrix -> matrix.forEachIndexed { rowIndex, row -> run {
                row.forEachIndexed { columnIndex, value -> result[rowIndex, columnIndex + filledWidth] = value }

                filledWidth += matrix.width
            } } }

            return result
        }

        fun combineVertically(matricesToCombine: Array<IntMatrix>): IntMatrix {
            val height = matricesToCombine.sumOf { matrix -> matrix.height }
            val width = matricesToCombine.maxOf { matrix -> matrix.width }

            val result = IntMatrix(height, width)
            var filledHeight = 0
            matricesToCombine.forEach { matrix -> matrix.forEachIndexed { rowIndex, row -> run {
                row.forEachIndexed { columnIndex, value -> result[rowIndex + filledHeight, columnIndex] = value }

                filledHeight += matrix.height
            } } }

            return result
        }

        fun combineVertically(matricesToCombine: Iterable<IntMatrix>): IntMatrix {
            val height = matricesToCombine.sumOf { matrix -> matrix.height }
            val width = matricesToCombine.maxOf { matrix -> matrix.width }

            val result = IntMatrix(height, width)
            var filledHeight = 0
            matricesToCombine.forEach { matrix -> matrix.forEachIndexed { rowIndex, row -> run {
                row.forEachIndexed { columnIndex, value -> result[rowIndex + filledHeight, columnIndex] = value }

                filledHeight += matrix.height
            } } }

            return result
        }
    }

    fun columnSum(index: Int): Int {
        if (this._transposed == null) this.transpose()
        return this._transposed!!._data[index].sum()
    }

    fun rowSum(index: Int): Int = this._data[index].sum()

    fun columnSums(): IntArray {
        if (this._transposed == null) this.transpose()

        val result = IntArray(this._transposed!!.height)
        this._transposed!!.forEachIndexed { rowIndex, row -> run {
            var sum = 0
            row.forEach { value -> sum += value }

            result[rowIndex] = sum
        } }

        return result
    }

    fun rowSums(): IntArray {
        val result = IntArray(this.height)
        this.forEachIndexed { rowIndex, row -> run {
            var sum = 0
            row.forEach { value -> sum += value }

            result[rowIndex] = sum
        } }

        return result
    }

    fun transpose(): IntMatrix {
        if (this._transposed != null) return this._transposed!!

        val transposedValues = Array(this.width) { IntArray(this.height) }
        this._data.forEachIndexed { rowIndex, row -> row.forEachIndexed { columnIndex, value ->
                transposedValues[columnIndex][rowIndex] = value
        } }

        this._transposed = IntMatrix(transposedValues)
        this._transposed!!._transposed = this

        return this._transposed!!
    }

    fun clone(): IntMatrix {
        val values = Array(this.height) { IntArray(this.width) }
        this._data.forEachIndexed { rowIndex, row -> values[rowIndex] = row.clone() }

        return IntMatrix(values)
    }

    operator fun get(rowIndex: Int): IntArray = this._data[rowIndex].clone()

    operator fun get(rowIndex: Int, columnIndex: Int): Int = this._data[rowIndex][columnIndex]

    operator fun set(rowIndex: Int, values: IntArray) {
        if (values.size != this.width)
            throw IllegalArgumentException("Number of values elements must be equal to the width of the matrix")

        this._data[rowIndex] = values.clone()
    }

    operator fun set(rowIndex: Int, values: Iterable<Int>) {
        values.forEachIndexed { columnIndex, value -> run {
            if (columnIndex >= this.width)
                throw IllegalArgumentException("Number of values elements must be equal to the width of the matrix")

            this._data[rowIndex][columnIndex] = value
            if (this._transposed != null) this._transposed!!._data[columnIndex][rowIndex] = value
        } } }

    operator fun set(rowIndex: Int, columnIndex: Int, value: Int) {
        this._data[rowIndex][columnIndex] = value

        if (this._transposed != null) this._transposed!!._data[columnIndex][rowIndex] = value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (this.javaClass != other?.javaClass) return false

        other as IntMatrix

        if (this.height != other.height) return false
        if (this.width != other.width) return false
        if (!this._data.contentDeepEquals(other._data)) return false

        return true
    }

    override fun hashCode(): Int = this._data.hashCode() and this.size.hashCode()

    override fun toString(): String = this.toString(10)

    fun toString(radix: Int): String {
        val builder = StringBuilder()
        this.forEachIndexed { rowIndex, row -> run {
            row.forEachIndexed { columnIndex, value -> run {
                if (value == IntMatrix.POSITIVE_INFINITY || value == IntMatrix.NEGATIVE_INFINITY) builder.append("INF")
                else builder.append(value.toString(radix))

                if (columnIndex != this.width - 1) builder.append('\t')
            } }
        
            if (rowIndex != this.height - 1) builder.append('\n')
        } }

        return builder.toString()
    }
}
