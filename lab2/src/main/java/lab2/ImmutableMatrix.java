package lab2;

import java.util.Arrays;
import java.util.Random;

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
            dimen = new MatrixDimension(0);
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
        dimen = other.dimen;
    }

    protected ImmutableMatrix(double[] data, MatrixDimension dimen) {
        this.data = data;
        this.dimen = dimen;
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

        int columnCount = dimen.getColumnCount();
        int columnStart = index * columnCount;

        return Arrays.copyOfRange(data, columnStart, columnStart + columnCount);
    }

    public double[] getColumn(int index) {
        Preconditions.ensureValidIndexComponent(index, dimen.getColumnCount(), "index");

        double[] column = new double[dimen.getRowCount()];

        for (int i = 0; i < column.length; i++) {
            column[i] = data[linearIndex(i, index)];
        }

        return column;
    }

    public ImmutableMatrix plus(ImmutableMatrix other) {
        Preconditions.ensureSameDimensions(dimen, other.dimen);

        double[] output = new double[data.length];

        for (int i = 0; i < data.length; i++) {
            output[i] = data[i] + other.data[i];
        }

        return new ImmutableMatrix(output, dimen);
    }

    public ImmutableMatrix multiplyBy(double scalar) {;
        double[] output = new double[data.length];

        for (int i = 0; i < data.length; i++) {
            output[i] = data[i] * scalar;
        }

        return new ImmutableMatrix(output, dimen);
    }

    public ImmutableMatrix multiplyBy(ImmutableMatrix other) {
        if (dimen.getColumnCount() != other.dimen.getRowCount()) {
            throw new IllegalArgumentException("Cannot multiply this matrix by other: column count of this matrix is incompatible with other's row count");
        }

        int rowCount = dimen.getRowCount();

        int otherColumnCount = other.dimen.getColumnCount();
        double[] result = new double[rowCount * otherColumnCount];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < otherColumnCount; j++) {
                double sum = 0;

                for (int k = 0; k < dimen.getColumnCount(); k++) {
                    sum += get(i, k) * other.get(k, j);
                }

                result[i * otherColumnCount + j] = sum;
            }
        }

        return new ImmutableMatrix(result, new MatrixDimension(otherColumnCount, rowCount));
    }

    public ImmutableMatrix transposed() {
        double[] result = new double[data.length];

        int rowCount = dimen.getRowCount();
        int columnCount = dimen.getColumnCount();

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                result[linearIndex(j, i, rowCount)] = get(i, j);
            }
        }

        return new ImmutableMatrix(result, dimen.interchanged());
    }

    public static ImmutableMatrix createDiagonal(double[] vector) {
        double[] data = new double[vector.length * vector.length];

        for (int i = 0; i < vector.length; i++) {
            data[linearIndex(i, i, vector.length)] = vector[i];
        }

        return new ImmutableMatrix(data, new MatrixDimension(vector.length));
    }

    public static ImmutableMatrix createIdentity(int size) {
        Preconditions.ensureValidDimension(size, "size");

        double[] data = new double[size * size];

        for (int i = 0; i < size; i++) {
            data[linearIndex(i, i, size)] = 1.0;
        }

        return new ImmutableMatrix(data, new MatrixDimension(size));
    }

    public static ImmutableMatrix createRandomRowMatrix(int length, Random random) {
       return createRandomMatrixInternal(length, random, new MatrixDimension(length, 1));
    }

    public static ImmutableMatrix createRandomColumnMatrix(int length, Random random) {
        return createRandomMatrixInternal(length, random, new MatrixDimension(1, length));
    }

    private static ImmutableMatrix createRandomMatrixInternal(int length, Random random, MatrixDimension dimen) {
        Preconditions.ensureValidLength(length);

        double[] data = new double[length];

        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextDouble();
        }

        return new ImmutableMatrix(data, dimen);
    }

    protected int linearIndex(int row, int column) {
        return linearIndex(row, column, dimen.getColumnCount());
    }

    private static int linearIndex(int row, int column, int columnCount) {
        return row * columnCount + column;
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

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("ImmutableMatrix { dimension=");
        sb.append(dimen.getRowCount()).append('x').append(dimen.getColumnCount());
        sb.append(", data=[");

        for (int i = 0; i < data.length; i++) {
            sb.append(data[i]);

            if (i < data.length - 1) {
                sb.append(", ");
            }
        }

        sb.append("] }");

        return sb.toString();
    }
}
