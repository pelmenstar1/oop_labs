package lab2;

import java.util.Arrays;

public class MutableMatrix {
    private final double[] data;
    private final MatrixDimension dimen;

    public MutableMatrix() {
        data = new double[0];
        dimen = new MatrixDimension(0, 0);
    }

    public MutableMatrix(int columnCount, int rowCount) {
        Preconditions.ensureValidDimension(columnCount, "columnCount");
        Preconditions.ensureValidDimension(rowCount, "rowCount");

        data = new double[columnCount * rowCount];
        dimen = new MatrixDimension(columnCount, rowCount);
    }

    public MutableMatrix(MutableMatrix other) {
        data = Arrays.copyOf(other.data, other.data.length);
        dimen = new MatrixDimension(other.dimen);
    }

    public MatrixDimension getDimension() {
        return dimen;
    }

    public double get(int row, int column) {
        Preconditions.ensureValidIndexComponent(row, dimen.getRowCount(), "row");
        Preconditions.ensureValidIndexComponent(column, dimen.getColumnCount(), "column");

        return data[linearIndex(row, column)];
    }

    public double[] getRow(int index) {
        Preconditions.ensureValidIndexComponent(index, dimen.getRowCount(), "index");

        return MatrixOperations.getRow(data, index, dimen);
    }

    public double[] getColumn(int index) {
        Preconditions.ensureValidIndexComponent(index, dimen.getColumnCount(), "index");

        return MatrixOperations.getColumn(data, index, dimen);
    }

    public void set(int row, int column, double value) {
        Preconditions.ensureValidIndexComponent(row, dimen.getRowCount(), "row");
        Preconditions.ensureValidIndexComponent(column, dimen.getColumnCount(), "column");

        data[linearIndex(row, column)] = value;
    }

    public void setRow(int index, double[] row) {
        Preconditions.ensureValidIndexComponent(index, dimen.getRowCount(), "index");

        if (row.length != dimen.getColumnCount()) {
            throw new IllegalArgumentException("row's length should equal to the column count of the matrix");
        }

        int columnCount = dimen.getColumnCount();

        System.arraycopy(row, 0, data, index * columnCount, columnCount);
    }

    public void setColumn(int index, double[] column) {
        int columnCount = dimen.getColumnCount();

        Preconditions.ensureValidIndexComponent(index, columnCount, "index");

        if (column.length != dimen.getRowCount()) {
            throw new IllegalArgumentException("column's length should equal to the row count of the matrix");
        }

        for (int i = 0; i < column.length; i++) {
            data[i * columnCount + index] = column[i];
        }
    }

    public void set(double[][] rows) {
        int columnCount = dimen.getColumnCount();

        if (rows.length != dimen.getRowCount()) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MutableMatrix matrix = (MutableMatrix) o;

        return dimen.equals(matrix.dimen) && Arrays.equals(data, matrix.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data) * 31 + dimen.hashCode();
    }

    private int linearIndex(int row, int column) {
        return row * dimen.getColumnCount() + column;
    }
}
