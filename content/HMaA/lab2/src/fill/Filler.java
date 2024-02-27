package fill;

import util.Matrix;

public final class Filler {
    private Filler() {
    }

    public static Matrix fill(int tasksCount, int processorsCount, int lowerBound, int upperBound) {
        // double[][] values = {
        //     {60, 47, 8, 15}, 
        //     {59, 11, 32, 78},
        //     {32, 31, 91, 6},
        //     {81, 85, 16, 61},
        //     {54, 67, 6, 25},
        //     {82, 1, 61, 71},
        //     {32, 73, 46, 51},
        //     {93, 80, 25, 88},
        //     {18, 37, 82, 85},
        //     {72, 88, 6, 16}
        // };
        Matrix result = new Matrix(tasksCount, processorsCount);

        for (int i = 0; i < tasksCount; i++)
            for (int j = 0; j < processorsCount; j++)
                result.setValue(i, j, NumberGenerator.generate(lowerBound, upperBound, 0));


        // return new Matrix(values);
        return result;
    }
}