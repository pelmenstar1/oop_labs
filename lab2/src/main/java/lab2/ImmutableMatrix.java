package lab2;

import java.util.Arrays;

public class ImmutableMatrix {
    protected final double[] data;
    protected final MatrixDimension dimen;

    public ImmutableMatrix() {
        this(0, 0);
    }

    public ImmutableMatrix(int columnCount, int rowCount) {
        // MatrixDimension constructor checks whether columnCount and/or rowCount are valid.
        dimen = new MatrixDimension(columnCount, rowCount);
        data = new double[columnCount * rowCount];
    }

    public ImmutableMatrix(double[][] rows) {
        if (rows.length == 0) {
            data = new double[0];
            dimen = new MatrixDimension(0, 0);
        } else {
            int columnCount = rows[0].length;
            int rowCount = rows.length;

            dimen = new MatrixDimension(columnCount, rowCount);
            data = new double[columnCount * rowCount];

            MatrixOperations.setRows(data, rows, dimen);
        }
    }

    public ImmutableMatrix(ImmutableMatrix other) {
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

    protected int linearIndex(int row, int column) {
        return row * dimen.getColumnCount() + column;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ImmutableMatrix matrix) {
            return dimen.equals(matrix.dimen) && Arrays.equals(data, matrix.data);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data) * 31 + dimen.hashCode();
    }
}