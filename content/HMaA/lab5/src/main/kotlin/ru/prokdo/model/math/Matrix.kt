package ru.prokdo.model.math


 /**
  * Class representing a mathematical matrix object and some operations on it.
  * @property height height (rows number) of matrix.
  * @property width width (columns number) of matrix.
  * @property size size (both height and width) pair of matrix.
  * @property [rowIndices] [data] param indices.
  * @property [columnIndices] firs row of [data] param indices.
 */
class Matrix<T> @PublishedApi internal constructor(height: Int, width: Int, data: Array<Array<T>>) where T : Number, T : Comparable<T> {
     /**
     * Height (rows number) of the matrix.
     */
    val height: Int

     /**
     * Width (columns number) of the matrix.
     */
    val width: Int

     /**
      * Size (both height and width) pair of matrix.
      */
     val size: Pair<Int, Int>

     /**
      * Values stored in matrix.
      */
    private val _data: Array<Array<T>>

     /**
      * [Data][data] param indices.
      */
    val rowIndices: IntRange

     /**
      * Indices of first row of [data] param.
      */
    val columnIndices: IntRange

     /**
     * Transposed version of matrix. Initially is null and gets its value when [transpose] method being invoked
     */
    private var _transposed: Matrix<T>?

    init {
        this.height = height
        this.width = width
        this.size = Pair(height, width)

        this._data = data
        this.rowIndices = data.indices
        this.columnIndices = data[0].indices

        this._transposed = null
    }

     /**
     * @param height height (rows number) of matrix.
     * @param width width (columns number) of matrix.
     *
     * @throws IllegalArgumentException if [height] or [width] param is zero or negative.
     */
//     constructor(height: Int, width: Int) {
//        if (height <= 0 || width <= 0) throw IllegalArgumentException("Matrix dimensions cannot be negative")
//
//        this.height = height
//        this.width = width
//        this.size = Pair(height, width)
//
//        this._data = Array(this.height) { Array(this.width) { 0.0 } }
//        this.rowIndices = this._data.indices
//        this.columnIndices = this._data[0].indices
//
//        this._transposed = null
//    }

     /**
     * @param data values to store in matrix. Must be 2D array of doubles with same number of elements in each row.
      *
      * @throws IllegalArgumentException if [data] 2D array has not same number of elements in each row or if [data]
      * array or arrays inside of it is empty.
     */
//    constructor(data: Array<Array<Double>>) {
//        if (data.isEmpty() || data[0].isEmpty()) throw IllegalArgumentException("Matrix dimensions cannot be zero")
//
//        for (i in data.indices)
//            if (data[0].size != data[i].size)
//                throw IllegalArgumentException("Values param must has same number of elements in each row")
//
//        this.height = data.size
//        this.width = data[0].size
//        this.size = Pair(this.height, this.width)
//
//        this._data = Array(this.height) { Array(this.width) { 0.0 } }
//        for (i in data.indices)
//            this._data[i] = data[i].clone()
//
//        this.rowIndices = data.indices
//        this.columnIndices = data[0].indices
//
//        this._transposed = null
//    }

     /**
      * Companion object for implementing static methods of matrix class.
      */
     companion object {
         inline operator fun <reified T> invoke(data: Array<Array<T>>): Matrix<T> where T : Number,
                                                                                        T : Comparable<T> {
            if (data.isEmpty() || data[0].isEmpty()) throw IllegalArgumentException("Matrix dimensions cannot be zero")

            for (i in data.indices) if (data[0].size != data[i].size)
                throw IllegalArgumentException("Data param must has same number of elements in each row")

            val dataClone = data.map { row -> row.clone() }.toTypedArray()

            return Matrix(dataClone.size, dataClone[0].size, dataClone)
         }

         inline operator fun <reified T> invoke(height: Int, width: Int): Matrix<T> where T : Number,
                                                                                          T : Comparable<T> {
             if (height <= 0 || width <= 0) throw IllegalArgumentException("Matrix dimensions cannot be zero or negative")

             val data = Array(height) { Array(width) { 0 as T } }

             return Matrix(height, width, data)
         }

         /**
          * The function takes an array of matrices and returns a matrix with a number of rows equal to the maximum
          * number of rows of matrices in the given array and with a number of columns equal to the sum of the number of
          * columns of matrices in the given array. Content of matrices stored in array is being combined as well.
          *
          * @param matricesToCombine array of matrices to combine.
          */
        fun combineHorizontally(matricesToCombine: Array<Matrix<T>>): Matrix {
            val height = matricesToCombine.maxOf { matrix -> matrix.height }
            val width = matricesToCombine.sumOf { matrix -> matrix.width }

            val result = Matrix(height, width)
            var filledWidth = 0
            for (matrixIndex in matricesToCombine.indices) {
                for (i in matricesToCombine[matrixIndex].rowIndices)
                    for (j in matricesToCombine[matrixIndex].columnIndices)
                        result[i, j + filledWidth] = matricesToCombine[matrixIndex][i, j]

                filledWidth += matricesToCombine[matrixIndex].width
            }

            return result
        }

         /**
          * The function takes as input an array of matrices and returns a matrix with a number of columns equal to the
          * maximum number of columns of matrices in the given array and with a number of rows equal to the sum of the
          * number of rows of matrices in the given array. Content of matrices stored in array is being combined as well.
          *
          * @param matricesToCombine array of matrices to combine.
          */
        fun combineVertically(matricesToCombine: Array<Matrix>): Matrix {
            val height = matricesToCombine.sumOf { matrix -> matrix.height }
            val width = matricesToCombine.maxOf { matrix -> matrix.width }

            val result = Matrix(height, width)
            var filledHeight = 0
            for (matrixIndex in matricesToCombine.indices) {
                for (i in matricesToCombine[matrixIndex].rowIndices)
                    for (j in matricesToCombine[matrixIndex].columnIndices)
                        result[i + filledHeight, j] = matricesToCombine[matrixIndex][i, j]

                filledHeight += matricesToCombine[matrixIndex].height
            }

            return result
        }
    }

    fun getColumnSum(i: Int): Double {
        if (this._transposed == null) this.transpose()

       return this._transposed!!._data[i].sum()
    }

    fun getRowSum(i: Int): Double {
        return this._data[i].sum()
    }

    fun getColumnsSums(): Array<Double> {
        if (this._transposed == null) this.transpose()

        val result = Array(this._transposed!!.height) { 0.0 }
        for (i in this._transposed!!.rowIndices) {
            var sum = 0.0
            for(j in this._transposed!!.columnIndices)
                sum += this._transposed!![i, j]

            result[i] = sum
        }

        return result
    }

    fun getRowsSums(): Array<Double> {
        val result = Array(this.height) { 0.0 }
        for (i in this.rowIndices) {
            var sum = 0.0
            for(j in this.columnIndices)
                sum += this._data[i][j]

            result[i] = sum
        }

        return result
    }

    fun transpose(): Matrix {
        if (this._transposed != null) return this._transposed!!

        val transposedValues = Array(this.width) { Array(this.height) { 0.0 } }
        for (i in this.rowIndices)
            for (j in this.columnIndices)
                transposedValues[j][i] = this._data[i][j]

        this._transposed = Matrix(transposedValues)
        this._transposed!!._transposed = this

        return this._transposed!!
    }

    fun clone(): Matrix {
        val data = Array(this.height) { Array(this.width) { 0.0 } }
        for (i in this.rowIndices)
            data[i] = this._data[i].clone()

        return Matrix(data)
    }

    operator fun get(i: Int): Array<Double> {
        return this._data[i].clone()
    }

    operator fun get(i: Int, j: Int): Double {
        return this._data[i][j]
    }

    operator fun set(i: Int, row: Array<Double>) {
         this._data[i] = row.clone()

        if (this._transposed != null) for (index in row.indices)
                this._transposed!!._data[index][i] = row[index]
     }

    operator fun set(i: Int, j: Int, value: Double) {
        this._data[i][j] = value

        if (this._transposed != null) this._transposed!![j, i] = value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix

        if (height != other.height) return false
        if (width != other.width) return false
        if (!_data.contentDeepEquals(other._data)) return false

        return true
    }

    override fun hashCode(): Int {
        return this._data.hashCode() and this.height.hashCode() and this.width.hashCode()
    }

    override fun toString(): String {
        var isInt = true
        for (dataRow in this._data)
            for (value in dataRow)
                if (value - value.toInt() != 0.0) {
                    isInt = false
                    break
                }

        val builder = StringBuilder()
        for (i in this.rowIndices) {
            for (j in this.columnIndices) {
                if (isInt) builder.append(this._data[i][j].toInt())
                else builder.append(this._data[i][j])

                if (j != this._data[0].size - 1) builder.append('\t')
            }

            if (i != this._data.size - 1) builder.append('\n')
        }

        return builder.toString()
    }
}