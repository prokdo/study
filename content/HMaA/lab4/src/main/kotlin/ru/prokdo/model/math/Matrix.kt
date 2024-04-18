package ru.prokdo.model.math


 /**
  * Class representing a mathematical number matrix object and some operations on it.
  * @property height height (rows number) of matrix.
  * @property width width (columns number) of matrix.
  * @property size a pair of height and width of matrix.
  * @property data copy of data stored in matrix.
  * @property [rowIndices] [data] param indices.
  * @property [columnIndices] firs row of [data] param indices.
 */
class Matrix<T>
 /**
  * @param data data to store in matrix. Must be 2D array of doubles with same number of elements in each row.
  *
  * @throws IllegalArgumentException if [data] 2D array has not same number of elements in each row or if [data]
  * array or arrays inside of it is empty.
  */ private constructor(data: Array<Array<T>>) where T : Number,
                                                      T : Comparable<T> {
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
    private val _data: Array<Array<T>>

     /**
      * Getter property for access to clones of stored in matrix values.
      */
    val data: Array<Array<T>>
         get() { return this._data.map { row -> row.clone() }.toTypedArray() }

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

         this._transposed = null
     }

     /**
      * Companion object for implementing static methods of matrix class.
      */
     companion object {
         inline operator fun <reified T> invoke(data: Array<Array<T>>) {
            return Matrix<T>()

         }

         inline operator fun <reified T> invoke(height: Int, width: Int) {

         }
         /**
          * The function takes an array of matrices and returns a matrix with a number of rows equal to the maximum
          * number of rows of matrices in the given array and with a number of columns equal to the sum of the number of
          * columns of matrices in the given array. Content of matrices stored in array is being combined as well.
          *
          * @param matricesToCombine array of matrices to combine.
          */
        fun <T> combineHorizontally(matricesToCombine: Array<Matrix<T>>): Matrix<T> where T : Number,
                                                                                          T : Comparable<T> {
            val height = matricesToCombine.maxOf { matrix -> matrix.height }
            val width = matricesToCombine.sumOf { matrix -> matrix.width }

            val result = Matrix<T>(height, width)
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
        fun <T> combineVertically(matricesToCombine: Array<Matrix<T>>): Matrix<T> where T: Number,
                                                                                        T : Comparable<T> {
            val height = matricesToCombine.sumOf { matrix -> matrix.height }
            val width = matricesToCombine.maxOf { matrix -> matrix.width }

            val result = Matrix<T>(height, width)
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

    fun getColumnSum(i: Int): T {
        if (this.transposed == null) this.transpose()

       return this.transposed!!._data[i].sum()
    }

    fun getRowSum(i: Int): Double {
        return this._data[i].sum()
    }

    fun getColumnsSums(): Array<Double> {
        if (this.transposed == null) this.transpose()

        val result = Array(this.transposed!!.height) { 0.0 }
        for (i in this.transposed!!.rowIndices) {
            var sum = 0.0
            for(j in this.transposed!!.columnIndices)
                sum += this.transposed!![i, j]

            result[i] = sum
        }

        return result
    }

    fun getRowsSums(): Array<Double> {
        val result = Array(this.height) { 0.0 }
        for (i in this._values.indices) {
            var sum = 0.0
            for(j in this._values[i].indices)
                sum += this._values[i][j]

            result[i] = sum
        }

        return result
    }

    fun transpose(): Matrix {
        if (this.transposed != null) return this.transposed!!

        val transposedValues = Array(this.width) { Array(this.height) { 0.0 } }
        for (i in this._values.indices)
            for (j in this._values[0].indices)
                transposedValues[j][i] = this._values[i][j]

        this.transposed = Matrix(transposedValues)
        this.transposed!!.transposed = this

        return this.transposed!!
    }

    fun clone(): Matrix {
        val values = Array(this.height) { Array(this.width) { 0.0 } }
        for (i in this._values.indices)
            values[i] = this._values[i].clone()

        return Matrix(values)
    }

    operator fun get(i: Int): Array<Double> {
        return this._values[i].clone()
    }

    operator fun get(i: Int, j: Int): Double {
        return this._values[i][j]
    }

    operator fun set(i: Int, value: Array<Double>) {
         this._values[i] = value.clone()
     }

    operator fun set(i: Int, j: Int, value: Double) {
        this._values[i][j] = value

        if (this.transposed != null) this.transposed!![j, i] = value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix

        if (height != other.height) return false
        if (width != other.width) return false
        if (!values.contentDeepEquals(other.values)) return false

        return true
    }

    override fun hashCode(): Int {
        return this._values.hashCode() and this.height.hashCode() and this.width.hashCode()
    }

    override fun toString(): String {
        var isInt = true
        for (valuesRow in this._values)
            for (value in valuesRow)
                if (value - value.toInt() != 0.0) {
                    isInt = false
                    break
                }

        val builder = StringBuilder()
        for (i in this._values.indices) {
            for (j in this._values[i].indices) {
                if (isInt) builder.append(this._values[i][j].toInt())
                else builder.append(this._values[i][j])

                if (j != this._values[0].size - 1) builder.append('\t')
            }

            if (i != this._values.size - 1) builder.append('\n')
        }

        return builder.toString()
    }
}