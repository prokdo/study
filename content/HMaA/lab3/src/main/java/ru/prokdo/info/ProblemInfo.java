package ru.prokdo.info;

public class ProblemInfo extends Info {
    public final int processorsCount;
    public final int tasksCount;
    public final double[] tasksWeights;

    public ProblemInfo(int processorsCount, int tasksCount, double[] tasksWeights) {
        this.processorsCount = processorsCount;
        this.tasksCount = tasksCount;
        this.tasksWeights = tasksWeights;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("Характеристика задачи:\n");
        result.append(String.format("Количество процессоров (N): %d\n", this.processorsCount));
        result.append(String.format("Количество задач (M): %d\n", this.tasksCount));
        
        result.append("Список задач (T): [");
        for (int i = 0; i < this.tasksWeights.length; i++) {
            result.append(String.format("%.0f", this.tasksWeights[i]));

            if (i != this.tasksWeights.length - 1) result.append(", ");
            else result.append("]\n");
        }

        return result.toString();
    }
}
