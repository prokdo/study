package ru.prokdo.fill;

public final class Filler {
    private Filler() {
    }

    public static double[] fill(int tasksCount, int lowerBound, int upperBound) {
        double[] result = new double[tasksCount];

        for (int i = 0; i < tasksCount; i++)
            result[i] = (Double) NumberGenerator.generate(lowerBound, upperBound, 0);

        return result;
    }
}