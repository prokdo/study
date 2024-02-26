package fill;

import util.Matrix;

public final class Filler {
    private Filler() {
    }

    public static Matrix fill(int tasksCount, int processorsCount, int lowerBound, int upperBound) {
        Matrix result = new Matrix(tasksCount, processorsCount);

        for (int i = 0; i < tasksCount; i++)
            for (int j = 0; j < processorsCount; j++)
                result.setValue(i, j, NumberGenerator.generate(lowerBound, upperBound, 0));

        return result;
    }
}