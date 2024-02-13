package schedule_problem;

import java.util.ArrayList;

import sort.SortType;
import sort.Sorter;
import util.Matrix;

public final class ScheduleSolver {
    private ScheduleSolver() {
    };

    // info[0] - Double[] tasks;
    // info[1] - int tasksCount;
    // info[2] - int processorsCount;
    // info[3] - null or value from SortType enum

    // algorithmType - value from AlgorithmType enum;

    // result[0] - Matrix matrix;
    // result[1] - double maxProcessorLoad;
    // result[2] - long elapsedTime (nano seconds)

    public static Object[] solve(Object[] info, AlgorithmType algorithmType) {
        Object[] result = new Object[3];

        Double[] tasks = (Double[]) info[0];
        int taskCount = (int) info[1];
        int processorsCount = (int) info[2];
        if (info[3] == null)
            Sorter.sort(tasks, SortType.DESCENDING);
        else
            Sorter.sort(tasks, (SortType) info[3]);

        long startTime = System.nanoTime();
        Matrix matrix;
        switch (algorithmType) {
            case CRITICAL_METHOD_PATH -> {
                matrix = ScheduleSolver.CMPImplementation(tasks, taskCount, processorsCount);
            }
            case HALF_DIVISION_MULTITUDE_TASKS -> {
                matrix = ScheduleSolver.HDMTImplementation(tasks, taskCount, processorsCount);
            }
            case ORDERED_FRAGMENTATION_MULTITUDE_TASKS -> {
                matrix = ScheduleSolver.OFMTImplementation(tasks, taskCount, processorsCount);
            }
            default -> throw new IllegalArgumentException("Unknown algorithm type");
        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        double maxLoad = Double.MIN_VALUE;
        for (int i = 0; i < matrix.getColumnsCount(); i++) {
            double processorLoad = 0;
            for (int j = 0; j < matrix.getRowsCount(); j++) {
                processorLoad += matrix.getValue(j, i);
            }

            if (maxLoad < processorLoad)
                maxLoad = processorLoad;
        }

        result[0] = matrix;
        result[1] = maxLoad;
        result[2] = elapsedTime;

        return result;
    }

    private static Matrix CMPImplementation(Double[] tasks, int taskCount, int processorsCount) {
        Matrix result = new Matrix(taskCount, processorsCount);

        double[] processorsLoad = new double[processorsCount];
        for (double task : tasks) {
            double minLoad = Double.MAX_VALUE;
            int minLoadIndex = -1;
            for (int i = processorsLoad.length - 1; i > -1; i--)
                if (processorsLoad[i] <= minLoad) {
                    minLoad = processorsLoad[i];
                    minLoadIndex = i;
                }

            processorsLoad[minLoadIndex] += task;

            int i = 0;
            while (result.getValue(i, minLoadIndex) != 0) {
                i++;
            }
            result.setValue(i, minLoadIndex, task);
        }

        return result;
    }

    private static Matrix HDMTImplementation(Double[] tasks, int taskCount, int processorsCount) {
        Matrix firstLayer = CMPImplementation(tasks, taskCount, 2);

        Double[][] firstLayerTasks = new Double[2][];
        for (int i = 0; i < firstLayer.getColumnsCount(); i++) {
            ArrayList<Double> list = new ArrayList<Double>();
            for (int j = 0; j < firstLayer.getRowsCount(); j++)
                list.add(firstLayer.getValue(j, i));
            firstLayerTasks[i] = new Double[list.size()];
            list.toArray(firstLayerTasks[i]);
        }

        Matrix[] secondLayer = {
                CMPImplementation(firstLayerTasks[0], firstLayerTasks[0].length, processorsCount / 2),
                CMPImplementation(firstLayerTasks[1], firstLayerTasks[1].length, processorsCount / 2) };

        return Matrix.combine(secondLayer);
    }

    private static Matrix OFMTStep(int maxLayerCount, Matrix previousLayerMatrix, int previousLayerIndex) {
        if (previousLayerIndex == 0) {
            int taskCount = previousLayerMatrix.getRowsCount();
            Double[] tasks = new Double[taskCount];
            for (int i = 0; i < taskCount; i++)
                tasks[i] = previousLayerMatrix.getValue(i, 0);

            Matrix firstLayer = CMPImplementation(tasks, taskCount, 2);

            return OFMTStep(maxLayerCount, firstLayer, 1);
        }

        Double[][] currentLayerTasks = new Double[previousLayerMatrix.getColumnsCount()][];
        for (int i = 0; i < previousLayerMatrix.getColumnsCount(); i++) {
            ArrayList<Double> list = new ArrayList<Double>();
            for (int j = 0; j < previousLayerMatrix.getRowsCount(); j++)
                list.add(previousLayerMatrix.getValue(j, i));
            currentLayerTasks[i] = new Double[list.size()];
            list.toArray(currentLayerTasks[i]);
        }

        Matrix[] layer = new Matrix[previousLayerMatrix.getColumnsCount()];
        for (int i = 0; i < currentLayerTasks.length; i++)
            layer[i] = CMPImplementation(currentLayerTasks[i], currentLayerTasks[i].length, 2);

        Matrix layerMatrix = Matrix.combine(layer);

        if (previousLayerIndex + 1 >= maxLayerCount)
            return layerMatrix;

        return OFMTStep(maxLayerCount, layerMatrix, previousLayerIndex + 1);
    }

    private static Matrix OFMTImplementation(Double[] tasks, int taskCount, int processorsCount) {
        int maxLayerCount = (int) (Math.log(processorsCount) / Math.log(2));

        Matrix startMatrix = new Matrix(taskCount, processorsCount);
        for (int i = 0; i < taskCount; i++)
            startMatrix.setValue(i, 0, tasks[i]);

        return OFMTStep(maxLayerCount, startMatrix, 0);
    }
}
