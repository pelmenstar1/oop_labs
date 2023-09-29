package lab2;

import java.util.Arrays;

public final class Matrix {
    private final double[] data;
    private final int columnCount;

    public Matrix() {
        data = new double[0];
        columnCount = 0;
    }

    public Matrix(int columnCount, int rowCount) {
        Preconditions.ensureValidDimension(columnCount, "columnCount");
        Preconditions.ensureValidDimension(rowCount, "rowCount");

        data = new double[columnCount * rowCount];
        this.columnCount = columnCount;
    }

    public Matrix(Matrix other) {
        data = Arrays.copyOf(other.data, other.data.length);
        columnCount = other.columnCount;
    }

    private Matrix(double[] data, int columnCount) {
        this.data = data;
        this.columnCount = columnCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getRowCount() {
        return data.length == 0 ? 0 : data.length / columnCount;
    }
}
