package sort;

import java.util.Random;

public final class Sorter {
    private static final Random RANDOM = new Random();

    private Sorter() {
    }

    public static void sort(Double[] array, SortType sortType) {
        switch (sortType) {
            case ASCENDING -> mergeSort(array, SortType.ASCENDING);
            case DESCENDING -> mergeSort(array, SortType.DESCENDING);
            case SHAKE -> shake(array);
            default -> throw new IllegalArgumentException("Unknown sort type");
        }
    }

    private static void mergeSort(Double[] array, SortType sortType) {
        if (array.length < 2)
            return;

        int mid = array.length / 2;
        Double[] left = new Double[mid];
        Double[] right = new Double[array.length - mid];

        for (int i = 0; i < mid; i++)
            left[i] = array[i];

        for (int i = mid; i < array.length; i++)
            right[i - mid] = array[i];

        mergeSort(left, sortType);
        mergeSort(right, sortType);
        merge(array, left, right, sortType);
    }

    private static void merge(Double[] array, Double[] left, Double[] right, SortType sortType) {
        int leftIndex = 0;
        int rightIndex = 0;
        int arrayIndex = 0;

        if (sortType == SortType.ASCENDING)
            while (leftIndex < left.length && rightIndex < right.length)
                if (left[leftIndex].doubleValue() < right[rightIndex].doubleValue())
                    array[arrayIndex++] = left[leftIndex++];
                else
                    array[arrayIndex++] = right[rightIndex++];
        else
            while (leftIndex < left.length && rightIndex < right.length)
                if (left[leftIndex].doubleValue() > right[rightIndex].doubleValue())
                    array[arrayIndex++] = left[leftIndex++];
                else
                    array[arrayIndex++] = right[rightIndex++];

        while (leftIndex < left.length)
            array[arrayIndex++] = left[leftIndex++];

        while (rightIndex < right.length)
            array[arrayIndex++] = right[rightIndex++];
    }

    private static void shake(Double[] array) {
        Double temp;
        int firstIndex;
        int secondIndex;

        int permutationsCount = (int) (Math.log(array.length) / Math.log(2)) * 1000;

        for (int i = 0; i < permutationsCount; i++) {
            firstIndex = RANDOM.nextInt(0, array.length);
            secondIndex = RANDOM.nextInt(0, array.length);

            temp = array[firstIndex];
            array[firstIndex] = array[secondIndex];
            array[secondIndex] = temp;
        }
    }
}
