package lab2;

import java.util.Arrays;
import java.util.Random;

public class ImmutableMatrix<T extends AbstractNumber<T>> {
    protected final T[] data;
    protected final MatrixDimension dimen;
    protected final AbstractNumberSupport<T> support;

    public ImmutableMatrix(AbstractNumberSupport<T> support) {
        this(support, 0, 0);
    }

    public ImmutableMatrix(AbstractNumberSupport<T> support, MatrixDimension dimen) {
        this(support, dimen.getRowCount(), dimen.getColumnCount());
    }

    public ImmutableMatrix(AbstractNumberSupport<T> support, int rowCount, int columnCount) {
        // MatrixDimension constructor checks whether columnCount and/or rowCount are valid.
        dimen = new MatrixDimension(rowCount, columnCount);
        data = support.newZeroArray(columnCount * rowCount);

        this.support = support;
    }

    public ImmutableMatrix(AbstractNumberSupport<T> support, T[][] rows) {
        if (rows.length == 0) {
            data = support.newArray(0);
            dimen = new MatrixDimension(0);
        } else {
            int columnCount = rows[0].length;
            int rowCount = rows.length;

            dimen = new MatrixDimension(rowCount, columnCount);
            data = support.newZeroArray(columnCount * rowCount);

            MatrixOperations.setRows(data, rows, dimen);
        }

        this.support = support;
    }

    public ImmutableMatrix(ImmutableMatrix<T> other) {
        data = Arrays.copyOf(other.data, other.data.length);
        dimen = other.dimen;
        support = other.support;
    }

    protected ImmutableMatrix(AbstractNumberSupport<T> support, T[] data, MatrixDimension dimen) {
        this.data = data;
        this.dimen = dimen;
        this.support = support;
    }

    public AbstractNumberSupport<T> getSupport() {
        return support;
    }

    public MatrixDimension getDimension() {
        return dimen;
    }

    public T get(int row, int column) {
        Preconditions.ensureValidIndexComponent(row, dimen.getRowCount(), "row");
        Preconditions.ensureValidIndexComponent(column, dimen.getColumnCount(), "column");

        return data[linearIndex(row, column)];
    }

    public T[] getRow(int index) {
        Preconditions.ensureValidIndexComponent(index, dimen.getRowCount(), "index");

        int columnCount = dimen.getColumnCount();
        int columnStart = index * columnCount;

        return Arrays.copyOfRange(data, columnStart, columnStart + columnCount);
    }

    public T[] getColumn(int index) {
        Preconditions.ensureValidIndexComponent(index, dimen.getColumnCount(), "index");

        T[] column = support.newArray(dimen.getRowCount());

        for (int i = 0; i < column.length; i++) {
            column[i] = data[linearIndex(i, index)];
        }

        return column;
    }

    public ImmutableMatrix<T> plus(ImmutableMatrix<T> other) {
        Preconditions.ensureSameDimensions(dimen, other.dimen);

        T[] output = support.newArray(data.length);

        for (int i = 0; i < data.length; i++) {
            output[i] = data[i].plus(other.data[i]);
        }

        return new ImmutableMatrix<>(support, output, dimen);
    }

    public ImmutableMatrix<T> multiplyBy(T scalar) {
        T[] output = support.newArray(data.length);

        for (int i = 0; i < data.length; i++) {
            output[i] = data[i].multiply(scalar);
        }

        return new ImmutableMatrix<>(support, output, dimen);
    }

    public ImmutableMatrix<T> multiplyBy(ImmutableMatrix<T> other) {
        if (dimen.getColumnCount() != other.dimen.getRowCount()) {
            throw new IllegalArgumentException("Cannot multiply this matrix by other: column count of this matrix is incompatible with other's row count");
        }

        int rowCount = dimen.getRowCount();

        int otherColumnCount = other.dimen.getColumnCount();
        T[] result = support.newArray(rowCount * otherColumnCount);

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < otherColumnCount; j++) {
                T sum = support.getZero();

                for (int k = 0; k < dimen.getColumnCount(); k++) {
                    sum = sum.plus(get(i, k).multiply(other.get(k, j)));
                }

                result[i * otherColumnCount + j] = sum;
            }
        }

        return new ImmutableMatrix<>(support, result, new MatrixDimension(rowCount, otherColumnCount));
    }

    public ImmutableMatrix<T> transposed() {
        T[] result = support.newArray(data.length);

        int rowCount = dimen.getRowCount();
        int columnCount = dimen.getColumnCount();

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                result[linearIndex(j, i, rowCount)] = get(i, j);
            }
        }

        return new ImmutableMatrix<>(support, result, dimen.interchanged());
    }

    public ImmutableMatrix<T> inverse() {
        Preconditions.ensureSquareDimension(getDimension());

        var result = new MutableMatrix<>(this);
        int size = getDimension().getRowCount();

        T one = support.getOne();
        T zero = support.getZero();

        for (int k = 0; k < size; k++) {
            T diagVal = result.get(k, k);

            result.set(k, k, one);

            T invDiagVal = diagVal.inverse();

            for (int j = 0; j < size; j++) {
                result.set(k, j, result.get(k, j).multiply(invDiagVal));
            }

            for (int i = 0; i < size; i++) {
                if (i == k) {
                    continue;
                }

                T d = result.get(i, k);
                result.set(i, k, zero);

                for (int j  = 0; j < size; j++) {
                    result.set(i, j, result.get(i, j).minus(d.multiply(result.get(k, j))));
                }
            }
        }

        return result;
    }

    public static ImmutableMatrix<RealNumber> createReal(double[]... data) {
        if (data.length == 0) {
            return new ImmutableMatrix<>(RealNumber.support());
        }

        int rowCount = data.length;
        int columnCount = data[0].length;
        var dimen = new MatrixDimension(rowCount, columnCount);

        var convertedData = new RealNumber[rowCount * columnCount];
        MatrixOperations.setRowsReal(convertedData, data, dimen);

        return new ImmutableMatrix<>(RealNumber.support(), convertedData, dimen);
    }

    public static <T extends AbstractNumber<T>> ImmutableMatrix<T> createDiagonal(
        AbstractNumberSupport<T> support,
        T[] vector
    ) {
        T[] data = support.newZeroArray(vector.length * vector.length);

        for (int i = 0; i < vector.length; i++) {
            data[linearIndex(i, i, vector.length)] = vector[i];
        }

        return new ImmutableMatrix<>(support, data, new MatrixDimension(vector.length));
    }

    public static <T extends AbstractNumber<T>> ImmutableMatrix<T> createIdentity(
        AbstractNumberSupport<T> support,
        int size
    ) {
        Preconditions.ensureValidDimension(size, "size");

        T[] data = support.newZeroArray(size * size);
        T one = support.getOne();

        for (int i = 0; i < size; i++) {
            data[linearIndex(i, i, size)] = one;
        }

        return new ImmutableMatrix<>(support, data, new MatrixDimension(size));
    }

    public static <T extends AbstractNumber<T>> ImmutableMatrix<T> createRandomRowMatrix(
        AbstractNumberSupport<T> support,
        int length, Random random
    ) {
        return createRandomMatrixInternal(support, length, random, new MatrixDimension(1, length));
    }

    public static <T extends AbstractNumber<T>> ImmutableMatrix<T> createRandomColumnMatrix(
        AbstractNumberSupport<T> support,
        int length, Random random
    ) {
        return createRandomMatrixInternal(support, length, random, new MatrixDimension(length, 1));
    }

    private static <T extends AbstractNumber<T>> ImmutableMatrix<T> createRandomMatrixInternal(
        AbstractNumberSupport<T> support,
        int length, Random random, MatrixDimension dimen
    ) {
        Preconditions.ensureValidLength(length);

        T[] data = support.newArray(length);

        for (int i = 0; i < data.length; i++) {
            data[i] = support.randomNumber(random);
        }

        return new ImmutableMatrix<>(support, data, dimen);
    }

    protected int linearIndex(int row, int column) {
        return linearIndex(row, column, dimen.getColumnCount());
    }

    private static int linearIndex(int row, int column, int columnCount) {
        return row * columnCount + column;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ImmutableMatrix<?> matrix &&
            support.equals(matrix.support) &&
            dimen.equals(matrix.dimen) &&
            Arrays.equals(data, matrix.data);
    }

    public boolean equalsApproximate(ImmutableMatrix<T> other, T precision) {
        if (!support.equals(other.support) || !dimen.equals(other.dimen)) {
            return false;
        }

        for (int i = 0; i < data.length; i++) {
            T a = data[i];
            T b = other.data[i];

            if (!a.equalsApproximately(b, precision)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(data);
        result = result * 31 + dimen.hashCode();
        result = result * 31 + support.hashCode();

        return result;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("ImmutableMatrix<");
        sb.append(support.getNumberClass());
        sb.append("> {");
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
