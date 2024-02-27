package schedule_problem;

import sort.SortType;
import sort.Sorter;
import util.Matrix;
import util.Pair;

public final class HeterogeneousScheduleSolver {
    private HeterogeneousScheduleSolver() {
    };

    // info[0] - Tasks complexity matrix;
    // info[1] - value from SortType enum

    // result[0] - Matrix matrix;
    // result[1] - double maxProcessorLoad;
    // result[2] - long elapsedTime (nano seconds)

    public static Object[] solve(Object[] info) {
        Object[] result = new Object[3];

        Matrix matrix = (Matrix) info[0];
        Sorter.sort(matrix, (SortType) info[1]);

        long startTime = System.nanoTime();
        
        Matrix resultMatrix = HeterogeneousScheduleSolver.solveImplementation(matrix);

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        double maxLoad = Double.MIN_VALUE;
        for (int i = 0; i < resultMatrix.getColumnsCount(); i++) {
            double processorLoad = 0;
            for (int j = 0; j < resultMatrix.getRowsCount(); j++)
                processorLoad += resultMatrix.getValue(j, i);

            if (maxLoad < processorLoad)
                maxLoad = processorLoad;
        }

        result[0] = resultMatrix;
        result[1] = maxLoad;
        result[2] = elapsedTime;

        return result;
    }

    private static Matrix solveImplementation(Matrix matrix) {
        Matrix result = new Matrix(matrix.getRowsCount(), matrix.getColumnsCount());
        Matrix matrixCopy = matrix.clone();

        for (int i = 0; i < matrixCopy.getRowsCount(); i++) {
            Pair<Integer, Double> minLoad = new Pair<Integer, Double>(-1, Double.MAX_VALUE);
            for (int j = 0; j < matrixCopy.getColumnsCount(); j++)
                if (matrixCopy.getValue(i, j) < minLoad.second)
                    minLoad = new Pair<Integer,Double>(j, matrixCopy.getValue(i, j));

            minLoad = new Pair<Integer,Double>(minLoad.first, matrix.getValue(i, minLoad.first));

            result.setValue(i, minLoad.first, minLoad.second);

            for (int j = i + 1; j < matrixCopy.getRowsCount(); j++)
                matrixCopy.setValue(j, minLoad.first, matrixCopy.getValue(j, minLoad.first) + minLoad.second);
        }

        return result;
    }
}
