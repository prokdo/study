package ru.prokdo.model.math

class DoubleMatrix : Iterable<DoubleArray> {
    val height: Int
    val width: Int

    val size: Pair<Int, Int>

    private val _data: Array<DoubleArray>
    val data: Array<DoubleArray>
        get() = this._data.map { row -> row.clone() }.toTypedArray()

    val rowIndices: IntRange
    val columnIndices: IntRange

    val indices: Pair<IntRange, IntRange>

    private var _transposed: DoubleMatrix?

    constructor(data: Iterable<Iterable<Double>>) {
        var height = 1
        val width = data.elementAt(0).count()
        data.forEachIndexed { rowIndex, row ->
            run {
                if (rowIndex != 0)
                        if (row.count() != width)
                                throw IllegalArgumentException(
                                        "Data param must has same number of elements in each row"
                                )
                height++
            }
        }

        this.height = height
        this.width = width

        this.size = Pair(this.height, this.width)

        this._data = Array(this.height) { DoubleArray(this.width) }
        data.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, value -> this._data[rowIndex][columnIndex] = value }
        }

        this.rowIndices = this._data.indices
        this.columnIndices = this._data[0].indices

        this.indices = Pair(this.rowIndices, this.columnIndices)

        this._transposed = null
    }

    constructor(data: Array<Iterable<Double>>) {
        val height = data.size
        val width = data.elementAt(0).count()
        data.forEachIndexed { rowIndex, row ->
            if (rowIndex != 0)
                    if (row.count() != width)
                            throw IllegalArgumentException(
                                    "Data param must has same number of elements in each row"
                            )
        }

        this.height = height
        this.width = width

        this.size = Pair(this.height, this.width)

        this._data = Array(this.height) { DoubleArray(this.width) }
        data.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, value -> this._data[rowIndex][columnIndex] = value }
        }

        this.rowIndices = this._data.indices
        this.columnIndices = this._data[0].indices

        this.indices = Pair(this.rowIndices, this.columnIndices)

        this._transposed = null
    }

    constructor(data: Array<DoubleArray>) {
        val height = data.size
        val width = data[0].size
        data.forEachIndexed { rowIndex, row ->
            if (rowIndex != 0)
                    if (row.size != width)
                            throw IllegalArgumentException(
                                    "Data param must has same number of elements in each row"
                            )
        }

        this.height = height
        this.width = width

        this.size = Pair(this.height, this.width)

        this._data = Array(this.height) { DoubleArray(this.width) }
        data.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, value -> this._data[rowIndex][columnIndex] = value }
        }

        this.rowIndices = this._data.indices
        this.columnIndices = this._data[0].indices

        this.indices = Pair(this.rowIndices, this.columnIndices)

        this._transposed = null
    }

    constructor(height: Int, width: Int) {
        if (height < 0 || width < 0)
                throw IllegalArgumentException("Matrix dimensions cannot be less than zero")

        this.height = height
        this.width = width

        this.size = Pair(this.height, this.width)

        this._data = Array(this.height) { DoubleArray(this.width) }

        this.rowIndices = this._data.indices
        this.columnIndices = this._data[0].indices

        this.indices = Pair(this.rowIndices, this.columnIndices)

        this._transposed = null
    }

    override fun iterator(): Iterator<DoubleArray> = this._data.iterator()

    companion object {
        fun combineHorizontally(matricesToCombine: Array<DoubleMatrix>): DoubleMatrix {
            val height = matricesToCombine.maxOf { matrix -> matrix.height }
            val width = matricesToCombine.sumOf { matrix -> matrix.width }

            val result = DoubleMatrix(height, width)
            var filledWidth = 0
            matricesToCombine.forEach { matrix ->
                matrix.forEachIndexed { rowIndex, row ->
                    run {
                        row.forEachIndexed { columnIndex, value ->
                            result[rowIndex, columnIndex + filledWidth] = value
                        }

                        filledWidth += matrix.width
                    }
                }
            }

            return result
        }

        fun combineHorizontally(matricesToCombine: Iterable<DoubleMatrix>): DoubleMatrix {
            val height = matricesToCombine.maxOf { matrix -> matrix.height }
            val width = matricesToCombine.sumOf { matrix -> matrix.width }

            val result = DoubleMatrix(height, width)
            var filledWidth = 0
            matricesToCombine.forEach { matrix ->
                run {
                    matrix.forEachIndexed { rowIndex, row ->
                        run {
                            row.forEachIndexed { columnIndex, value ->
                                result[rowIndex, columnIndex + filledWidth] = value
                            }

                            filledWidth += matrix.width
                        }
                    }
                }
            }

            return result
        }

        fun combineVertically(matricesToCombine: Array<DoubleMatrix>): DoubleMatrix {
            val height = matricesToCombine.sumOf { matrix -> matrix.height }
            val width = matricesToCombine.maxOf { matrix -> matrix.width }

            val result = DoubleMatrix(height, width)
            var filledHeight = 0
            matricesToCombine.forEach { matrix ->
                run {
                    matrix.forEachIndexed { rowIndex, row ->
                        run {
                            row.forEachIndexed { columnIndex, value ->
                                result[rowIndex + filledHeight, columnIndex] = value
                            }

                            filledHeight += matrix.height
                        }
                    }
                }
            }

            return result
        }

        fun combineVertically(matricesToCombine: Iterable<DoubleMatrix>): DoubleMatrix {
            val height = matricesToCombine.sumOf { matrix -> matrix.height }
            val width = matricesToCombine.maxOf { matrix -> matrix.width }

            val result = DoubleMatrix(height, width)
            var filledHeight = 0
            matricesToCombine.forEach { matrix ->
                run {
                    matrix.forEachIndexed { rowIndex, row ->
                        run {
                            row.forEachIndexed { columnIndex, value ->
                                result[rowIndex + filledHeight, columnIndex] = value
                            }

                            filledHeight += matrix.height
                        }
                    }
                }
            }

            return result
        }
    }

    fun columnSum(index: Int): Double {
        if (this._transposed == null) this.transpose()
        return this._transposed!!._data[index].sum()
    }

    fun rowSum(index: Int): Double = this._data[index].sum()

    fun columnSums(): DoubleArray {
        if (this._transposed == null) this.transpose()

        val result = DoubleArray(this._transposed!!.height)
        this._transposed!!.forEachIndexed { rowIndex, row ->
            run {
                var sum = 0.0
                row.forEach { value -> sum += value }

                result[rowIndex] = sum
            }
        }

        return result
    }

    fun rowSums(): DoubleArray {
        val result = DoubleArray(this.height)
        this.forEachIndexed { rowIndex, row ->
            run {
                var sum = 0.0
                row.forEach { value -> sum += value }

                result[rowIndex] = sum
            }
        }

        return result
    }

    fun transpose(): DoubleMatrix {
        if (this._transposed != null) return this._transposed!!

        val transposedValues = Array(this.width) { DoubleArray(this.height) }
        this._data.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, value ->
                transposedValues[columnIndex][rowIndex] = value
            }
        }

        this._transposed = DoubleMatrix(transposedValues)
        this._transposed!!._transposed = this

        return this._transposed!!
    }

    fun clone(): DoubleMatrix {
        val values = Array(this.height) { DoubleArray(this.width) }
        this._data.forEachIndexed { rowIndex, row -> values[rowIndex] = row.clone() }

        return DoubleMatrix(values)
    }

    operator fun get(rowIndex: Int): DoubleArray = this._data[rowIndex].clone()

    operator fun get(rowIndex: Int, columnIndex: Int): Double = this._data[rowIndex][columnIndex]

    operator fun set(rowIndex: Int, values: DoubleArray) {
        if (values.size != this.width)
                throw IllegalArgumentException(
                        "Number of values elements must be equal to the width of the matrix"
                )

        this._data[rowIndex] = values.clone()
    }

    operator fun set(rowIndex: Int, values: Iterable<Double>) {
        values.forEachIndexed { columnIndex, value ->
            run {
                if (columnIndex >= this.width)
                        throw IllegalArgumentException(
                                "Number of values elements must be equal to the width of the matrix"
                        )

                this._data[rowIndex][columnIndex] = value
                if (this._transposed != null)
                        this._transposed!!._data[columnIndex][rowIndex] = value
            }
        }
    }

    operator fun set(rowIndex: Int, columnIndex: Int, value: Double) {
        this._data[rowIndex][columnIndex] = value

        if (this._transposed != null) this._transposed!!._data[columnIndex][rowIndex] = value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (this.javaClass != other?.javaClass) return false

        other as DoubleMatrix

        if (this.height != other.height) return false
        if (this.width != other.width) return false
        if (!this._data.contentDeepEquals(other._data)) return false

        return true
    }

    override fun hashCode(): Int = this._data.hashCode() and this.size.hashCode()

    override fun toString(): String = this.toString(0)

    fun toString(precision: Int): String {
        val builder = StringBuilder()
        this.forEachIndexed { rowIndex, row ->
            run {
                row.forEachIndexed { columnIndex, value ->
                    run {
                        builder.append(".${precision}f".format(value))

                        if (columnIndex != this.width - 1) builder.append('\t')
                    }
                }

                if (rowIndex != this.height - 1) builder.append('\n')
            }
        }

        return builder.toString()
    }
}
