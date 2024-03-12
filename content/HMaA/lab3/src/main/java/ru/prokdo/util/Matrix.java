package ru.prokdo.util;

import java.util.Collection;

public class Matrix {
    private int rowsCount;
    private int columnsCount;
    private double[][] values;

    public Matrix(int rowsCount, int columnsCount) {
        this.rowsCount = rowsCount;
        this.columnsCount = columnsCount;

        this.values = new double[rowsCount][columnsCount];
    }

    public Matrix(double[][] values) {
        this.values = values;
        this.rowsCount = values.length;
        this.columnsCount = values[0].length;
    }

    public Matrix(Collection<Collection<Double>> values) {
        this.rowsCount = values.size();
        this.columnsCount = values.iterator().next().size();
        this.values = new double[this.rowsCount][this.columnsCount];

        int i = 0;
        for (Collection<Double> row : values) {
            int j = 0;
            for (double value : row) {
                this.values[i][j] = value;
                j++;
            }
            i++;
        }
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

    public static Matrix combine(Collection<Matrix> matrixToCombine) {
        int maxRowsCount = -1;
        int maxColumnsCount = -1;
        for (Matrix matrix : matrixToCombine) {
            if (matrix.getRowsCount() > maxRowsCount)
                maxRowsCount = matrix.getRowsCount();

            if (matrix.getColumnsCount() > maxColumnsCount)
                maxColumnsCount = matrix.getColumnsCount();
        }

        Matrix result = new Matrix(maxRowsCount, maxColumnsCount);

        int resultRowPos = 0;
        int resultColumnPos = 0;
        for (Matrix matrix : matrixToCombine) {
            for (int i = 0; i < matrix.getRowsCount(); i++) {
                for (int j = 0; j < matrix.getColumnsCount(); j++) {
                    result.setValue(resultRowPos, resultColumnPos, matrix.getValue(i, j));

                    resultColumnPos++;
                }

                resultRowPos++;
            }
            resultRowPos = 0;
        }
        return result;
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

    public double[] getRowsSum() {
        double[] result = new double[this.rowsCount];

        for (int i = 0; i < this.rowsCount; i++) {
            double sum = 0;
            for (int j = 0; j < this.columnsCount; j++)
                sum += this.values[i][j];

            result[i] = sum;
        }

        return result;
    }

    public double[] getColumnsSum() {
        double[] result = new double[this.columnsCount];

        for (int i = 0; i < this.columnsCount; i++) {
            double sum = 0;
            for (int j = 0; j < this.rowsCount; j++)
                sum += this.values[j][i];

            result[i] = sum;
        }

        return result;
    }

    public double[] getRow(int index) {
        return this.values[index].clone();
    }

    public double[] getColumn(int index) {
        double[] column = new double[this.rowsCount];

        for (int i = 0; i < column.length; i++)
            column[i] = this.values[i][index];

        return column;
    }

    public boolean setRow(int index, Collection<Double> collection) {
        if (collection.size() != this.columnsCount) return false;
        
        int columnIndex = 0;
        for (Double item : collection) {
            this.values[index][columnIndex] = item;

            columnIndex++;
        }

        return true;
    }

    public boolean setRow(int index, double[] array) {
        if (array.length != this.columnsCount) return false;
        
        this.values[index] = array.clone();

        return true;
    }

    public boolean setColumn(int index, Collection<Double> collection) {
        if (collection.size() != this.rowsCount) return false;

        int rowIndex = 0;
        for (Double item : collection) {
            this.values[rowIndex][index] = item;

            rowIndex++;
        }

        return true;
    }

    public boolean setColumn(int index, double[] array) {
        if (array.length != this.rowsCount) return false;

        int rowIndex = 0;
        for (double item : array) {
            this.values[rowIndex][index] = item;
            
            rowIndex++;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.rowsCount; i++) {
            for (int j = 0; j < this.columnsCount; j++) {
                if (this.values[i][j] % 1 == 0) result.append(String.format("%.0f", this.values[i][j]));
                else result.append(String.format("%.3f", this.values[i][j]));
                
                if (j != this.columnsCount - 1)
                    result.append("\t");
            }

            if (i != this.rowsCount - 1)
                result.append("\n");
        }
        result.append('\n');

        return result.toString();
    }

    @Override
    public Matrix clone() {
        return new Matrix(this.values.clone());
    }
}
