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

    public void set(int row, int column, double value) {
        Preconditions.ensureValidIndexComponent(row,  getRowCount(), "row");
        Preconditions.ensureValidIndexComponent(column, columnCount, "column");

        data[linearIndex(row, column)] = value;
    }

    public void setRow(int index, double[] row) {
        Preconditions.ensureValidIndexComponent(index, getRowCount(), "index");

        if (row.length != columnCount) {
            throw new IllegalArgumentException("row's length should equal to the column count of the matrix");
        }

        System.arraycopy(row, 0, data, index * columnCount, columnCount);
    }

    public void setColumn(int index, double[] column) {
        Preconditions.ensureValidIndexComponent(index, columnCount, "index");

        if (column.length != getRowCount()) {
            throw new IllegalArgumentException("column's length should equal to the row count of the matrix");
        }

        for (int i = 0; i < column.length; i++) {
            data[i * columnCount + index] = column[i];
        }
    }

    public void set(double[][] rows) {
        if (rows.length != getRowCount()) {
            throw new IllegalArgumentException("rows length should equal to the row count of the matrix");
        }

        for (int i = 0; i < rows.length; i++) {
            double[] row = rows[i];

            if (row.length != columnCount) {
                throw new IllegalArgumentException("Length of a row should be the same as the column count of the matrix");
            }

            System.arraycopy(row, 0, data, i * columnCount, columnCount);
        }
    }

    private int linearIndex(int row, int column) {
        return row * columnCount + column;
    }
}
