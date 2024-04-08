package ru.prokdo.model.sort


import kotlin.math.log2
import kotlin.random.Random

import ru.prokdo.model.util.info.ProblemInfo


/**
 * Object that implements quick sort for initial matrix of tasks weights.
 */
object Sorter {
    /**
     * Function for sorting the initial weight matrix by the sum of its rows according to the specified order.
     *
     * @param problemInfo a set of input data for the problem being solved. Weight matrix field must be non-null value.
     *
     * @throws NullPointerException if weight matrix in [problem info param][problemInfo] is null.
     *
     * @see ProblemInfo
     */
    fun sort(problemInfo: ProblemInfo) {
        if (problemInfo.weightMatrix == null) throw NullPointerException("Weight matrix cannot be null")

        val matrixRowsSum: Array<Pair<Int, Double>> = (problemInfo.weightMatrix!!.getRowsSums().mapIndexed { index, value -> Pair(index, value) }).toTypedArray()

        when (problemInfo.sortOrder!!) {
            SortOrder.ASCENDING, SortOrder.DESCENDING -> { mergeSort(matrixRowsSum, problemInfo.sortOrder!!) }
            SortOrder.SHAKE -> { shake(matrixRowsSum) }
        }

        val matrixCopy = problemInfo.weightMatrix!!.clone()
        matrixRowsSum.forEachIndexed { index, pair -> problemInfo.weightMatrix!![index] = matrixCopy[pair.first] }
    }

    /**
     * Function that implements merge sort for array that contains weight matrix's rows sums in specified order.
     *
     * @param array array of weight matrix's rows sums to sort.
     * @param sortOrder value from sort order enum that specify required sorting order for array.
     *
     * @see SortOrder
     */
    private fun mergeSort(array: Array<Pair<Int, Double>>, sortOrder: SortOrder) {
        if (array.size < 2) return

        val middleIndex = array.size / 2
        val leftArray = array.sliceArray(0 ..< middleIndex)
        val rightArray = array.sliceArray(middleIndex ..< array.size)

        mergeSort(leftArray, sortOrder)
        mergeSort(rightArray, sortOrder)
        merge(array, leftArray, rightArray, sortOrder)
    }

    /**
     * Helper function for merge sort that combines two parts of a partitioned array into one according to a specified order.
     *
     * @param array original array into which the combined array will be placed.
     * @param leftArray left part of partitioned array.
     * @param rightArray right part of partitioned array.
     * @param sortOrder value from sort order enum that specify required sorting order for array.
     *
     * @see SortOrder
     */
    private fun merge(
        array: Array<Pair<Int, Double>>,
        leftArray: Array<Pair<Int, Double>>,
        rightArray: Array<Pair<Int, Double>>,
        sortOrder: SortOrder) {
        var leftIndex = 0
        var rightIndex = 0
        var arrayIndex = 0

        when (sortOrder) {
            SortOrder.ASCENDING -> {
                while (leftIndex < leftArray.size && rightIndex < rightArray.size)
                    if (leftArray[leftIndex].second <= rightArray[rightIndex].second) array[arrayIndex++] = leftArray[leftIndex++]
                    else array[arrayIndex++] = rightArray[rightIndex++]
            }
            SortOrder.DESCENDING -> {
                while (leftIndex < leftArray.size && rightIndex < rightArray.size)
                    if (leftArray[leftIndex].second >= rightArray[rightIndex].second) array[arrayIndex++] = leftArray[leftIndex++]
                    else array[arrayIndex++] = rightArray[rightIndex++]
            }
            else -> throw IllegalArgumentException("Unknown sort type")
        }

        while (leftIndex < leftArray.size)
            array[arrayIndex++] = leftArray[leftIndex++]

        while (rightIndex < rightArray.size)
            array[arrayIndex++] = rightArray[rightIndex++]
    }

    /**
     * Function that implements pseudo-random values distribution (shake) for array that contains weight matrix's rows sums.
     *
     * @param array array to shake.
     */
    private fun shake(array: Array<Pair<Int, Double>>) {
        var firstIndex: Int
        var secondIndex: Int

        val permutationsNumber = log2(array.size.toDouble()).toInt() * 1000
        for (i in 1 .. permutationsNumber) {
            firstIndex = Random.nextInt(array.size)
            secondIndex = Random.nextInt(array.size)

            array[firstIndex] = array[secondIndex].also { array[secondIndex] = array[firstIndex] }
        }
    }
}