package root.matrix;

public final class Matrix {
    private int width;
    private int length;

    private Number[][] values;
    
    public Matrix(int rows_amount, int columns_amount) {
        this.width = rows_amount;
        this.length = columns_amount;

        this.values = new Number[rows_amount][columns_amount];
    }

    public Matrix(Number[][] values) {
        this.width = values.length;
        this.length = values[0].length;

        this.values = values;
    }

    public int getWidth() {
        return this.width;
    }

    public int getLength() {
        return this.length;
    }

    public Number[][] getValues() {
        return this.values;
    }

    public void setValues(Number[][] values) {
        this.width = values.length;
        this.length = values[0].length;
        
        this.values = values;
    }

    public Number[] getRow(int row_index) {
        return this.values[row_index];
    }

    public Number[] getColumn(int column_index) {
        Number[] column = new Number[this.length];

        for (int i = 0; i < this.length; i++)
            column[i] = this.values[i][column_index];

        return column;
    }

    public Number getValue(int row_index, int column_index) {
        return this.values[row_index][column_index];
    }

    public void setValue(int row_index, int column_index, Number value) {
        this.values[row_index][column_index] = value;
    }

    public void addValue(Number value) {
        for (int i = 0; i < this.width; i++)
            for (int j = 0; j < this.length; j++)
                if (this.values[i][j] == null) {
                    this.values[i][j] = value;
                    return;
                }
                    
    }

    public Matrix add(Matrix matrix) {
        if (this.width != matrix.getWidth() || this.length != matrix.getLength())
            throw new IllegalArgumentException("Matrix dimensions must agree");

        Number[][] result = new Number[this.width][this.length];

        for (int i = 0; i < this.width; i++)
            for (int j = 0; j < this.length; j++)
                result[i][j] = this.values[i][j].doubleValue() + matrix.getValue(i, j).doubleValue();

        return new Matrix(result);
    }

    public Matrix subtract(Matrix matrix) {
        if (this.width != matrix.getWidth() || this.length != matrix.getLength())
            throw new IllegalArgumentException("Matrix dimensions must agree");

        Number[][] result = new Number[this.width][this.length];

        for (int i = 0; i < this.width; i++)
            for (int j = 0; j < this.length; j++)
                result[i][j] = this.values[i][j].doubleValue() - matrix.getValue(i, j).doubleValue();

        return new Matrix(result);
    }
    
    public Matrix multiply(Matrix matrix) {
        if (this.length != matrix.getWidth())
            throw new IllegalArgumentException("Matrix dimensions must agree");

        Number[][] result = new Number[this.width][matrix.getLength()];

        for (int i = 0; i < this.width; i++)
            for (int j = 0; j < matrix.getLength(); j++)
                for (int k = 0; k < this.length; k++)
                    result[i][j] = result[i][j].doubleValue() + this.values[i][k].doubleValue() * matrix.getValue(k, j).doubleValue();

        return new Matrix(result);
    }

    public Matrix multiply(Number scalar) {
        Number[][] result = new Number[this.width][this.length];

        for (int i = 0; i < this.width; i++)
            for (int j = 0; j < this.length; j++)
                result[i][j] = this.values[i][j].doubleValue() * scalar.doubleValue();

        return new Matrix(result);
    }

    public Matrix transpose() {
        Number[][] result = new Number[this.length][this.width];

        for (int i = 0; i < this.width; i++)
            for (int j = 0; j < this.length; j++)
                result[j][i] = this.values[i][j];

        return new Matrix(result);
    }

    public boolean isFull() {
        for (int i = 0; i < this.width; i++)
            for (int j = 0; j < this.length; j++)
                if (this.values[i][j] == null) 
                    return false;

        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.length; j++)
                builder.append(String.format("%.3f", this.values[i][j]) + "\t");

            builder.append("\n");
        }

        return builder.toString();
    }
}
