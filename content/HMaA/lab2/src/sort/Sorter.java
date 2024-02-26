package sort;

import java.util.ArrayList;
import java.util.Random;

import util.Matrix;
import util.Pair;

public final class Sorter {
    private static final Random RANDOM = new Random();

    private Sorter() {
    }

    public static void sort(Matrix matrix, SortType sortType) {
        ArrayList<Pair<Integer, Double>> rowsSum = Sorter.getRowsSum(matrix);

        if (sortType == SortType.SHAKE)
            Sorter.shake(rowsSum);
        else
            Sorter.mergeSort(rowsSum, sortType);

        Matrix matrixCopy = matrix.clone();
        for (int i = 0; i < rowsSum.size(); i++) {
            int currentIndex = rowsSum.get(i).first;
            matrix.setRow(matrixCopy.getRow(currentIndex), i);
        }
    }

    private static ArrayList<Pair<Integer, Double>> getRowsSum(Matrix matrix) {
        ArrayList<Pair<Integer, Double>> result = new ArrayList<Pair<Integer, Double>>();

        for (int i = 0; i < matrix.getRowsCount(); i++) {
            double sum = 0;
            for (int j = 0; j < matrix.getColumnsCount(); j++)
                sum += matrix.getValue(i, j);

            result.add(new Pair<Integer, Double>(i, sum));
        }
        
        return result;
    }

    private static void mergeSort(ArrayList<Pair<Integer, Double>> list, SortType sortType) {
        if (list.size() < 2)
            return;

        int mid = list.size() / 2;

        ArrayList<Pair<Integer, Double>> left = new ArrayList<Pair<Integer, Double>>();
        while (left.size() != mid) left.add(new Pair<Integer, Double>(-1, -1.0));

        ArrayList<Pair<Integer, Double>> right = new ArrayList<Pair<Integer, Double>>();
        while (right.size() != list.size() - mid) right.add(new Pair<Integer, Double>(-1, -1.0));

        for (int i = 0; i < mid; i++)
            left.set(i, list.get(i));

        for (int i = mid; i < list.size(); i++)
            right.set(i - mid, list.get(i));

        mergeSort(left, sortType);
        mergeSort(right, sortType);
        merge(list, left, right, sortType);
    }

    private static void merge(ArrayList<Pair<Integer, Double>> list, ArrayList<Pair<Integer, Double>> left, ArrayList<Pair<Integer, Double>> right, SortType sortType) {
        int leftIndex = 0;
        int rightIndex = 0;
        int arrayIndex = 0;

        if (sortType == SortType.ASCENDING)
            while (leftIndex < left.size() && rightIndex < right.size())
                if (left.get(leftIndex).second.doubleValue() < right.get(rightIndex).second.doubleValue())
                    list.set(arrayIndex++, left.get(leftIndex++));
                else
                    list.set(arrayIndex++, right.get(rightIndex++));
        else
            while (leftIndex < left.size() && rightIndex < right.size())
                if (left.get(leftIndex).second.doubleValue() > right.get(rightIndex).second.doubleValue())
                    list.set(arrayIndex++, left.get(leftIndex++));
                else
                    list.set(arrayIndex++, right.get(rightIndex++));

        while (leftIndex < left.size())
            list.set(arrayIndex++, left.get(leftIndex++));

        while (rightIndex < right.size())
        list.set(arrayIndex++, right.get(rightIndex++));
    }

    private static void shake(ArrayList<Pair<Integer, Double>> list) {
        Pair<Integer, Double> temp;
        int firstIndex;
        int secondIndex;

        int permutationsCount = (int) (Math.log(list.size()) / Math.log(2)) * 1000;

        for (int i = 0; i < permutationsCount; i++) {
            firstIndex = RANDOM.nextInt(0, list.size());
            secondIndex = RANDOM.nextInt(0, list.size());

            temp = list.get(firstIndex);
            list.set(firstIndex, list.get(secondIndex));
            list.set(secondIndex, temp);
        }
    }
}
