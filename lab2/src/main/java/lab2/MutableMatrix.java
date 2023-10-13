package lab2;

import java.util.Random;

public class MutableMatrix extends ImmutableMatrix {
    public MutableMatrix() {
        super();
    }

    public MutableMatrix(int columnCount, int rowCount) {
        super(columnCount, rowCount);
    }

    public MutableMatrix(ImmutableMatrix other) {
        super(other);
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
        if (rows.length != dimen.getRowCount()) {
            throw new IllegalArgumentException("rows length should equal to the row count of the matrix");
        }

        MatrixOperations.setRows(data, rows, dimen);
    }

    @Override
    public MutableMatrix plus(ImmutableMatrix other) {
        return new MutableMatrix(super.plus(other));
    }

    @Override
    public MutableMatrix multiplyBy(double scalar) {
        return new MutableMatrix(super.multiplyBy(scalar));
    }

    @Override
    public MutableMatrix multiplyBy(ImmutableMatrix other) {
        return new MutableMatrix(super.multiplyBy(other));
    }

    @Override
    public MutableMatrix transposed() {
        return new MutableMatrix(super.transposed());
    }

    public static MutableMatrix createDiagonal(double[] vector) {
        return new MutableMatrix(ImmutableMatrix.createDiagonal(vector));
    }

    public static MutableMatrix createIdentity(int size) {
        return new MutableMatrix(ImmutableMatrix.createIdentity(size));
    }

    public static MutableMatrix createRandomRowMatrix(int length, Random random) {
        return new MutableMatrix(ImmutableMatrix.createRandomRowMatrix(length, random));
    }

    public static MutableMatrix createRandomColumnMatrix(int length, Random random) {
        return new MutableMatrix(ImmutableMatrix.createRandomColumnMatrix(length, random));
    }
}
