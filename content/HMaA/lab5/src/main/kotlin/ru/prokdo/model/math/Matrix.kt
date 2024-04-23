package ru.prokdo.model.math


 /**
  * Class representing a mathematical number matrix object and some operations on it.
  *
  * @property height height (rows number) of matrix.
  * @property width width (columns number) of matrix.
  * @property size a pair of height and width of matrix.
  * @property data copy of data stored in matrix.
  * @property rowIndices [data] param indices.
  * @property columnIndices first row of [data] param indices.
 */
class Matrix {
     /**
     * Height (rows number) of the matrix.
     */
    val height: Int

     /**
     * Width (columns number) of the matrix.
     */
    val width: Int

     /**
      * Size (both height and width) of the matrix.
      */
    val size: Pair<Int, Int>

     /**
      * Values stored in matrix.
      */
    private val _data: Array<DoubleArray>

     /**
      * Getter property for access to clones of stored in matrix values.
      */
    val data: Array<DoubleArray>
         get() = this._data.map { row -> row.clone() }.toTypedArray()

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
    private var transposed: Matrix?

    constructor(data: Array<DoubleArray>) {
         if (data.isEmpty() || data[0].isEmpty()) throw IllegalArgumentException("Matrix dimensions cannot be zero")

         for (i in data.indices)
             if (data[0].size != data[i].size)
                 throw IllegalArgumentException("Values param must has same number of elements in each row")

         this.height = data.size
         this.width = data[0].size
         this.size = Pair(this.height, this.width)
         
         this._data = data

         this.rowIndices = data.indices
         this.columnIndices = data[0].indices

         this.transposed = null
     }

    constructor(height: Int, width: Int) {
        if (height <= 0 || width <= 0) throw IllegalArgumentException("Matrix dimensions cannot be equal or less than zero")

        this.height = height
        this.width = width
        this.size = Pair(this.height, this.width)

        this._data = Array(this.height) { DoubleArray(this.width) }

        this.rowIndices = this._data.indices
        this.columnIndices = this._data[0].indices

        this.transposed = null
    }

     /**
      * Companion object for implementing static methods of matrix class.
      */
     companion object {
         /**
          * The function takes an array of matrices and returns a matrix with a number of rows equal to the maximum
          * number of rows of matrices in the given array and with a number of columns equal to the sum of the number of
          * columns of matrices in the given array. Content of matrices stored in array is being combined as well.
          *
          * @param matricesToCombine array of matrices to combine.
          */
        fun combineHorizontally(matricesToCombine: Array<Matrix>): Matrix {
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
        if (this.transposed == null) this.transpose()

       return this.transposed!!._data[i].sum()
    }

    fun getRowSum(i: Int): Double {
        return this._data[i].sum()
    }

    fun getColumnsSums(): DoubleArray {
        if (this.transposed == null) this.transpose()

        val result = DoubleArray(this.transposed!!.height)
        for (i in this.transposed!!.rowIndices) {
            var sum = 0.0
            for(j in this.transposed!!.columnIndices)
                sum += this.transposed!![i, j]

            result[i] = sum
        }

        return result
    }

    fun getRowsSums(): DoubleArray {
        val result = DoubleArray(this.height)
        for (i in this.rowIndices) {
            var sum = 0.0
            for(j in this.columnIndices)
                sum += this._data[i][j]

            result[i] = sum
        }

        return result
    }

    fun transpose(): Matrix {
        if (this.transposed != null) return this.transposed!!

        val transposedValues = Array(this.width) { DoubleArray(this.height) }
        for (i in this.rowIndices)
            for (j in this.columnIndices)
                transposedValues[j][i] = this._data[i][j]

        this.transposed = Matrix(transposedValues)
        this.transposed!!.transposed = this

        return this.transposed!!
    }

    fun clone(): Matrix {
        val values = Array(this.height) { DoubleArray(this.width) }
        for (i in this.rowIndices)
            values[i] = this._data[i].clone()

        return Matrix(values)
    }

    operator fun get(i: Int): DoubleArray {
        return this._data[i].clone()
    }

    operator fun get(i: Int, j: Int): Double {
        return this._data[i][j]
    }

    operator fun set(i: Int, value: DoubleArray) {
         this._data[i] = value.clone()
     }

    operator fun set(i: Int, j: Int, value: Double) {
        this._data[i][j] = value

        if (this.transposed != null) this.transposed!!._data[j][i] = value
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
        for (row in this._data)
            for (value in row)
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