package ru.prokdo.schedule_problem;

import ru.prokdo.fill.NumberGenerator;
import ru.prokdo.info.ProblemInfo;
import ru.prokdo.info.ResultInfo;
import ru.prokdo.util.Matrix;

public final class ScheduleSolver {
    private ScheduleSolver() {
    };

    /**
     * Solves given schedule problem and returns result matrix and some information about solution
     * 
     * @param problemInfo <p> {@code ProblemInfo} instance with all information about problem to solve. </p>

     * @param algorithmType <p> {@code AlgorithmType} value from {@code AlgorithmType} enum. </p>

     * @return {@code ResultInfo} instance with all information about problem solution.
     * 
     * @see ru.prokdo.info.ProblemInfo ProblemInfo class
     * @see ru.prokdo.schedule_problem.AlgorithmType AlgorithmType enum
     * @see ru.prokdo.info.ResultInfo ResultInfo class
     */

    public static ResultInfo solve(ProblemInfo problemInfo, AlgorithmType algorithmType) {
        long startTime = System.nanoTime();
        Matrix resultMatrix;
        switch (algorithmType) {
            case CRON_METHOD -> { resultMatrix = ScheduleSolver.CronImplementation(problemInfo); }
            case CRON_METHOD_MODIFICATION -> { resultMatrix = ScheduleSolver.CronModificationImplementation(problemInfo); }
            default -> throw new IllegalArgumentException("Unknown algorithm type");
        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        double maxProcessorLoad = Double.MIN_VALUE;
        for (double processorLoad : resultMatrix.getColumnsSum())
            if (processorLoad > maxProcessorLoad) maxProcessorLoad = processorLoad;

        return new ResultInfo(resultMatrix, maxProcessorLoad, elapsedTime);
    }

    private static Matrix CronImplementation(ProblemInfo problemInfo) {        
        Matrix result = new Matrix(problemInfo.tasksCount, problemInfo.processorsCount);

        int[] processorsMatrixShifts = new int[problemInfo.processorsCount];
        for (Double taskWeight : problemInfo.tasksWeights) {
            int processorIndex = NumberGenerator.generate(0, problemInfo.processorsCount - 1, 0).intValue();
            result.setValue(processorsMatrixShifts[processorIndex], processorIndex, taskWeight);

            processorsMatrixShifts[processorIndex]++;
        }

        System.out.println();
        System.out.println("Случайное распределение метода Крона:");
        System.out.println();
        System.out.println(problemInfo);
        System.out.println("Первая версия матрицы распределения: ");
        System.out.println(result);

        while (true) {
            boolean isPermutated = false;

            double[] processorsLoad = result.getColumnsSum();

            double minProcessorLoad = Double.MAX_VALUE;
            int minLoadIndex = -1;

            double maxProcessorLoad = Double.MIN_VALUE;
            int maxLoadIndex = -1;

            for (int i = 0; i < processorsLoad.length; i++) {
                if (processorsLoad[i] < minProcessorLoad) {
                    minProcessorLoad = processorsLoad[i];
                    minLoadIndex = i;
                }

                if (processorsLoad[i] > maxProcessorLoad) {
                    maxProcessorLoad = processorsLoad[i];
                    maxLoadIndex = i;
                }
            }

            double delta = maxProcessorLoad - minProcessorLoad;
            for (int i = 0; i < result.getRowsCount(); i++) {
                if (result.getValue(i, maxLoadIndex) == 0) continue;
                
                if (result.getValue(i, maxLoadIndex) < delta) {
                    for (int j = 0; j < result.getRowsCount(); j++) {
                        if (result.getValue(j, minLoadIndex) != 0) continue;

                        result.setValue(j, minLoadIndex, result.getValue(i, maxLoadIndex));
                        result.setValue(i, maxLoadIndex, 0);

                        isPermutated = true;
                        break;
                    }

                    if (isPermutated) break;
                }

                if (isPermutated) break;
            }
        
            if (!isPermutated) break;
        }

        while (true) {
            boolean isPermutated = false;

            double[] processorsLoad = result.getColumnsSum();

            double minProcessorLoad = Double.MAX_VALUE;

            double maxProcessorLoad = Double.MIN_VALUE;
            int maxLoadIndex = -1;

            for (int i = 0; i < processorsLoad.length; i++) {
                if (processorsLoad[i] < minProcessorLoad) minProcessorLoad = processorsLoad[i];

                if (processorsLoad[i] > maxProcessorLoad) {
                    maxProcessorLoad = processorsLoad[i];
                    maxLoadIndex = i;
                }
            }

            double delta = maxProcessorLoad - minProcessorLoad;
            for (int i = 0; i < processorsLoad.length; i++) {
                if (processorsLoad[i] != minProcessorLoad) continue;

                for (int j = 0; j < result.getRowsCount(); j++) {
                    if (result.getValue(j, maxLoadIndex) == 0) continue;

                    for (int k = 0; k < result.getRowsCount(); k++) {
                        if (result.getValue(k, i) == 0) continue;

                        if (result.getValue(j, maxLoadIndex) - result.getValue(k, i) < delta && result.getValue(j, maxLoadIndex) > result.getValue(k, i)) {
                            double temp = result.getValue(j, maxLoadIndex);
                            result.setValue(j, maxLoadIndex, result.getValue(k, i));
                            result.setValue(k, i, temp);

                            isPermutated = true;
                            break;
                        }

                        if (isPermutated) break;
                    }

                    if (isPermutated) break;
                }

                if (isPermutated) break;
            }

            if (!isPermutated) break;
        }

        return result;
    }

    private static Matrix CronModificationImplementation(ProblemInfo problemInfo) {
        Matrix result = CMPImplementation(problemInfo);

        while (true) {
            boolean isPermutated = false;

            double[] processorsLoad = result.getColumnsSum();

            double minProcessorLoad = Double.MAX_VALUE;
            int minLoadIndex = -1;

            double maxProcessorLoad = Double.MIN_VALUE;
            int maxLoadIndex = -1;

            for (int i = 0; i < processorsLoad.length; i++) {
                if (processorsLoad[i] < minProcessorLoad) {
                    minProcessorLoad = processorsLoad[i];
                    minLoadIndex = i;
                }

                if (processorsLoad[i] > maxProcessorLoad) {
                    maxProcessorLoad = processorsLoad[i];
                    maxLoadIndex = i;
                }
            }

            double delta = maxProcessorLoad - minProcessorLoad;
            for (int i = 0; i < result.getRowsCount(); i++) {
                if (result.getValue(i, maxLoadIndex) == 0) continue;
                
                if (result.getValue(i, maxLoadIndex) < delta) {
                    for (int j = 0; j < result.getRowsCount(); j++) {
                        if (result.getValue(j, minLoadIndex) != 0) continue;

                        result.setValue(j, minLoadIndex, result.getValue(i, maxLoadIndex));
                        result.setValue(i, maxLoadIndex, 0);

                        isPermutated = true;
                        break;
                    }

                    if (isPermutated) break;
                }

                if (isPermutated) break;
            }
        
            if (!isPermutated) break;
        }

        while (true) {
            boolean isPermutated = false;

            double[] processorsLoad = result.getColumnsSum();

            double minProcessorLoad = Double.MAX_VALUE;

            double maxProcessorLoad = Double.MIN_VALUE;
            int maxLoadIndex = -1;

            for (int i = 0; i < processorsLoad.length; i++) {
                if (processorsLoad[i] < minProcessorLoad) minProcessorLoad = processorsLoad[i];

                if (processorsLoad[i] > maxProcessorLoad) {
                    maxProcessorLoad = processorsLoad[i];
                    maxLoadIndex = i;
                }
            }

            double delta = maxProcessorLoad - minProcessorLoad;
            for (int i = 0; i < processorsLoad.length; i++) {
                if (processorsLoad[i] != minProcessorLoad) continue;

                for (int j = 0; j < result.getRowsCount(); j++) {
                    if (result.getValue(j, maxLoadIndex) == 0) continue;

                    for (int k = 0; k < result.getRowsCount(); k++) {
                        if (result.getValue(k, i) == 0) continue;

                        if (result.getValue(j, maxLoadIndex) - result.getValue(k, i) < delta && result.getValue(j, maxLoadIndex) > result.getValue(k, i)) {
                            double temp = result.getValue(j, maxLoadIndex);
                            result.setValue(j, maxLoadIndex, result.getValue(k, i));
                            result.setValue(k, i, temp);

                            isPermutated = true;
                            break;
                        }

                        if (isPermutated) break;
                    }

                    if (isPermutated) break;
                }

                if (isPermutated) break;
            }

            if (!isPermutated) break;
        }

        return result;
    }
    
    private static Matrix CMPImplementation(ProblemInfo problemInfo) {
        Matrix result = new Matrix(problemInfo.tasksCount, problemInfo.processorsCount);

        double[] processorsLoad = new double[problemInfo.processorsCount];
        for (double taskWeight : problemInfo.tasksWeights) {
            double minLoad = Double.MAX_VALUE;
            int minLoadIndex = -1;
            for (int i = processorsLoad.length - 1; i > -1; i--)
                if (processorsLoad[i] <= minLoad) {
                    minLoad = processorsLoad[i];
                    minLoadIndex = i;
                }

            processorsLoad[minLoadIndex] += taskWeight;

            int i = 0;
            while (result.getValue(i, minLoadIndex) != 0) {
                i++;
            }
            result.setValue(i, minLoadIndex, taskWeight);
        }

        return result;
    }
}
