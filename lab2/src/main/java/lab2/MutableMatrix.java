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

    protected MutableMatrix(double[] data, MatrixDimension dimen) {
        super(data, dimen);
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
        Preconditions.ensureSameDimensions(dimen, other.dimen);

        return new MutableMatrix(MatrixOperations.add(data, other.data), dimen);
    }

    @Override
    public MutableMatrix multiplyBy(double scalar) {
        return new MutableMatrix(MatrixOperations.multiplyByScalar(data, scalar), dimen);
    }

    @Override
    public MutableMatrix multiplyBy(ImmutableMatrix other) {
        ensureCompatibleForMultiplication(other);

        return new MutableMatrix(
            MatrixOperations.multiplyMatrices(data, other.data, dimen, other.dimen),
            getDimensionForMultiplicationResult(other)
        );
    }

    @Override
    public MutableMatrix transposed() {
        return new MutableMatrix(MatrixOperations.transpose(data, dimen), dimen.interchanged());
    }

    public static MutableMatrix createDiagonal(double[] vector) {
        return new MutableMatrix(
            MatrixOperations.createDiagonalMatrix(vector),
            new MatrixDimension(vector.length)
        );
    }

    public static MutableMatrix createIdentity(int size) {
        Preconditions.ensureValidDimension(size, "size");

        return new MutableMatrix(MatrixOperations.createIdentity(size), new MatrixDimension(size));
    }

    public static MutableMatrix createRandomRowMatrix(int length, Random random) {
        return createRandomMatrixInternal(length, random, new MatrixDimension(length, 1));
    }

    public static MutableMatrix createRandomColumnMatrix(int length, Random random) {
        return createRandomMatrixInternal(length, random, new MatrixDimension(1, length));
    }

    private static MutableMatrix createRandomMatrixInternal(int length, Random random, MatrixDimension dimen) {
        Preconditions.ensureValidLength(length);

        return new MutableMatrix(MatrixOperations.createRandomMatrix(length, random), dimen);
    }
}
