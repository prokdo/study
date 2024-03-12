package ru.prokdo.info;

import ru.prokdo.util.Matrix;

public class ResultInfo extends Info {
    public final Matrix resultMatrix;
    public final double maxProcessorLoad;
    public final long elapsedTime;

    public ResultInfo(Matrix resultMatrix, double maxProcessorLoad, long elapsedTime) {
        this.resultMatrix = resultMatrix;
        this.maxProcessorLoad = maxProcessorLoad;
        this.elapsedTime = elapsedTime / 1000000;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("Результат работы алгоритма:\n");
        result.append(String.format("Матрица нагрузки процессоров: \n%s", this.resultMatrix));
        result.append(String.format("Максимальная нагрузка на процессор: %.0f\n", this.maxProcessorLoad));
        result.append(String.format("Время работы алгоритма (мс): %d\n", this.elapsedTime));
        
        return result.toString();
    }
}
