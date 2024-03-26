package ru.prokdo.model.math


 /**
  * Class representing a mathematical matrix object and some operations on it.
  * @property height height (rows number) of matrix.
  * @property width width (columns number) of matrix.
  * @property values values clones stored in matrix.
  * @property [rowIndices] [values] param indices.
  * @property [columnIndices] firs row of [values] param indices.
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
      * Values stored in matrix.
      */
    private val _values: Array<Array<Double>>

     /**
      * Getter property for access to clones of stored in matrix values.
      */
    val values: Array<Array<Double>>
         get() {
             return this._values.map { row -> row.clone() }.toTypedArray()
         }

     /**
      * [Values][values] param indices.
      */
    val rowIndices: IntRange

     /**
      * First row of [values] param indices.
      */
    val columnIndices: IntRange

     /**
     * Transposed version of matrix. Initially is null and gets its value when [transpose] method being invoked
     */
    private var transposed: Matrix? = null

     /**
     * @param height height (rows number) of matrix.
     * @param width width (columns number) of matrix.
     */
     constructor(height: Int, width: Int) {
        if (height <= 0 || width <= 0) throw IllegalArgumentException("Matrix dimensions cannot be negative")

        this.height = height
        this.width = width

        this._values = Array(this.height) { Array(this.width) { 0.0 } }
        this.rowIndices = this._values.indices
        this.columnIndices = this._values[0].indices
    }

     /**
     * @param values values to store in matrix. Must be 2D array of doubles with same number of elements in each row.
      *
      * @throws IllegalArgumentException if [values] 2D array has not same number of elements in each row or if [values]
      * array or arrays inside of it is empty.
     */
    constructor(values: Array<Array<Double>>) {
        if (values.isEmpty() || values[0].isEmpty()) throw IllegalArgumentException("Matrix dimensions cannot be zero")

        for (i in values.indices)
            if (values[0].size != values[i].size) throw IllegalArgumentException("Values param must has same number of elements in each row")

        this.height = values.size
        this.width = values[0].size

        this._values = Array(this.height) { Array(this.width) { 0.0 } }
        for (i in values.indices)
            this._values[i] = values[i].clone()

        this.rowIndices = values.indices
        this.columnIndices = values[0].indices
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
                for (i in matricesToCombine[matrixIndex].values.indices)
                    for (j in matricesToCombine[matrixIndex].values[i].indices)
                        result[i, j + filledWidth] = matricesToCombine[matrixIndex].values[i][j]

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
                for (i in matricesToCombine[matrixIndex].values.indices)
                    for (j in matricesToCombine[matrixIndex].values[i].indices)
                        result[i + filledHeight, j] = matricesToCombine[matrixIndex].values[i][j]

                filledHeight += matricesToCombine[matrixIndex].height
            }

            return result
        }
    }

    fun getColumnSum(i: Int): Double {
        if (this.transposed == null) this.transpose()

       return this.transposed!!.values[i].sum()
    }

    fun getRowSum(i: Int): Double {
        return this._values[i].sum()
    }

    fun getColumnsSums(): Array<Double> {
        if (this.transposed == null) this.transpose()

        val result = Array(this.transposed!!.height) { 0.0 }
        for (i in this.transposed!!.values.indices) {
            var sum = 0.0
            for(j in this.transposed!!.values[i].indices)
                sum += this.transposed!!.values[i][j]

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