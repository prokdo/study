package util;

public class Matrix {
    private int rowsCount;
    private int columnsCount;
    private double[][] values;

    public Matrix(int rowsCount, int columnsCount) {
        this.rowsCount = rowsCount;
        this.columnsCount = columnsCount;

        values = new double[rowsCount][columnsCount];
    }

    public Matrix(double[][] values) {
        this.values = values;
        this.rowsCount = values.length;
        this.columnsCount = values[0].length;
    }

    public int getRowsCount() {
        return this.rowsCount;
    }

    public int getColumnsCount() {
        return this.columnsCount;
    }

    public double getValue(int i, int j) {
        return this.values[i][j];
    }

    public void setValue(int i, int j, double value) {
        this.values[i][j] = value;
    }

    public static Matrix combine(Matrix[] matrixToCombine) {
        int maxRowsCount = -1;
        int resultColumnsCount = 0;
        for (Matrix matrix : matrixToCombine) {
            if (matrix.getRowsCount() > maxRowsCount)
                maxRowsCount = matrix.getRowsCount();

            resultColumnsCount += matrix.getColumnsCount();
        }

        Matrix result = new Matrix(maxRowsCount, resultColumnsCount);

        int resultColumnsPos = 0;
        for (int i = 0; i < result.getRowsCount(); i++) {
            for (Matrix matrix : matrixToCombine) {
                if (matrix.getRowsCount() < i + 1)
                    continue;

                for (int j = 0; j < matrix.getColumnsCount(); j++) {
                    result.setValue(i, resultColumnsPos, matrix.getValue(i, j));

                    resultColumnsPos++;
                }
            }

            resultColumnsPos = 0;
        }
        return result;
    }

    public double[] getRow(int index) {
        return this.values[index];
    }

    public double[] getColumn(int index) {
        double[] result = new double[this.columnsCount];

        for (int i = 0; i < this.columnsCount; i++)
            result[i] = this.values[i][index];

        return result;
    }

    public boolean setRow(double[] values, int index) {
        if (values.length != this.getColumnsCount())
            return false;

        this.values[index] = values;

        return true;
    }

    public boolean setColumn(double[] values, int index) {
        if (values.length != this.getRowsCount())
            return false;

        for (int i = 0; i < this.getRowsCount(); i++)
            this.values[i][index] = values[i];

        return true;
    }

    public Matrix clone() {
        return new Matrix(this.values.clone());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.rowsCount; i++) {
            for (int j = 0; j < this.columnsCount; j++) {
                result.append(String.format("%.0f", this.values[i][j]));
                if (j != this.columnsCount - 1)
                    result.append("\t");
            }

            if (i != this.rowsCount - 1)
                result.append("\n");
        }
        return result.toString();
    }
}
