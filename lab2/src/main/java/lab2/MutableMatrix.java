package lab2;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

public class MutableMatrix<T extends AbstractNumber<T>> {
    protected final T[] data;
    protected final MatrixDimension dimen;
    protected final AbstractNumberSupport<T> support;

    public MutableMatrix(AbstractNumberSupport<T> support) {
        this(support, 0, 0);
    }

    public MutableMatrix(AbstractNumberSupport<T> support, MatrixDimension dimen) {
        this(support, dimen.getRowCount(), dimen.getColumnCount());
    }

    public MutableMatrix(AbstractNumberSupport<T> support, int rowCount, int columnCount) {
        // MatrixDimension constructor checks whether columnCount and/or rowCount are valid.
        dimen = new MatrixDimension(rowCount, columnCount);
        data = support.newZeroArray(columnCount * rowCount);

        this.support = support;
    }

    public MutableMatrix(AbstractNumberSupport<T> support, T[][] rows) {
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

    public MutableMatrix(MutableMatrix<T> other) {
        data = Arrays.copyOf(other.data, other.data.length);
        dimen = other.dimen;
        support = other.support;
    }

    protected MutableMatrix(AbstractNumberSupport<T> support, T[] data, MatrixDimension dimen) {
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
        T[] column = support.newArray(dimen.getRowCount());
        copyColumn(index, column);

        return column;
    }

    public void copyColumn(int column, T[] outVector) {
        if (outVector.length != dimen.getRowCount()) {
            throw new IllegalArgumentException("outVector should be the same length as matrix's row count");
        }

        Preconditions.ensureValidIndexComponent(column, dimen.getColumnCount(), "column");

        for (int i = 0; i < dimen.getRowCount(); i++) {
            outVector[i] = get(i, column);
        }
    }

    public void set(int row, int column, T value) {
        Preconditions.ensureNonNull(value, "value");
        Preconditions.ensureValidIndexComponent(row, dimen.getRowCount(), "row");
        Preconditions.ensureValidIndexComponent(column, dimen.getColumnCount(), "column");

        data[linearIndex(row, column)] = value;
    }

    public void setRow(int index, T[] row) {
        Preconditions.ensureValidIndexComponent(index, dimen.getRowCount(), "index");

        if (row.length != dimen.getColumnCount()) {
            throw new IllegalArgumentException("row's length should equal to the column count of the matrix");
        }

        int columnCount = dimen.getColumnCount();

        System.arraycopy(row, 0, data, index * columnCount, columnCount);
    }

    public void setColumn(int index, T[] column) {
        Preconditions.ensureValidIndexComponent(index, dimen.getColumnCount(), "index");

        if (column.length != dimen.getRowCount()) {
            throw new IllegalArgumentException("column's length should equal to the row count of the matrix");
        }

        for (int i = 0; i < column.length; i++) {
            set(i, index, column[i]);
        }
    }

    public void set(T[][] rows) {
        if (rows.length != dimen.getRowCount()) {
            throw new IllegalArgumentException("rows length should equal to the row count of the matrix");
        }

        MatrixOperations.setRows(data, rows, dimen);
    }

    public void swapElements(int row1, int column1, int row2, int column2) {
        T t = get(row1, column1);
        set(row1, column1, get(row2, column2));
        set(row2, column2, t);
    }

    public void set(MutableMatrix<T> other) {
        Preconditions.ensureSameDimensions(getDimension(), other.getDimension());

        System.arraycopy(other.data, 0, data, 0, data.length);
    }

    public void update(int row, int column, Function<T, T> mapping) {
        set(row, column, mapping.apply(get(row, column)));
    }

    public void updateRow(int row, Function<T, T> mapping) {
        Preconditions.ensureValidIndexComponent(row, dimen.getRowCount(), "row");

        int columnCount = dimen.getColumnCount();
        int mapStart = row * columnCount;
        int mapEnd = mapStart + columnCount;

        for (int i = mapStart; i < mapEnd; i++) {
            data[i] = mapping.apply(data[i]);
        }
    }

    public MutableMatrix<T> plus(MutableMatrix<T> other) {
        Preconditions.ensureSameDimensions(dimen, other.dimen);

        T[] output = support.newArray(data.length);

        for (int i = 0; i < data.length; i++) {
            output[i] = data[i].plus(other.data[i]);
        }

        return new MutableMatrix<>(support, output, dimen);
    }

    public MutableMatrix<T> multiplyBy(T scalar) {
        T[] output = support.newArray(data.length);

        for (int i = 0; i < data.length; i++) {
            output[i] = data[i].multiply(scalar);
        }

        return new MutableMatrix<>(support, output, dimen);
    }

    public MutableMatrix<T> multiplyBy(MutableMatrix<T> other) {
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

        return new MutableMatrix<>(support, result, new MatrixDimension(rowCount, otherColumnCount));
    }

    public MutableMatrix<T> transposed() {
        T[] result = support.newArray(data.length);

        int rowCount = dimen.getRowCount();
        int columnCount = dimen.getColumnCount();

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                result[linearIndex(j, i, rowCount)] = get(i, j);
            }
        }

        return new MutableMatrix<>(support, result, dimen.interchanged());
    }

    public MutableMatrix<T> inverse() {
        Preconditions.ensureSquareDimension(getDimension());

        var matrixClone = new MutableMatrix<>(this);

        return LUFactorization.inverseMatrix(matrixClone);
    }

    public static MutableMatrix<RealNumber> createReal(double[]... data) {
        if (data.length == 0) {
            return new MutableMatrix<>(RealNumber.support());
        }

        int rowCount = data.length;
        int columnCount = data[0].length;
        var dimen = new MatrixDimension(rowCount, columnCount);

        var convertedData = new RealNumber[rowCount * columnCount];
        MatrixOperations.setRowsReal(convertedData, data, dimen);

        return new MutableMatrix<>(RealNumber.support(), convertedData, dimen);
    }

    public static <T extends AbstractNumber<T>> MutableMatrix<T> createDiagonal(
        AbstractNumberSupport<T> support,
        T[] vector
    ) {
        T[] data = support.newZeroArray(vector.length * vector.length);

        for (int i = 0; i < vector.length; i++) {
            data[linearIndex(i, i, vector.length)] = vector[i];
        }

        return new MutableMatrix<>(support, data, new MatrixDimension(vector.length));
    }

    public static <T extends AbstractNumber<T>> MutableMatrix<T> createIdentity(
        AbstractNumberSupport<T> support,
        int size
    ) {
        Preconditions.ensureValidDimension(size, "size");

        T[] data = support.newZeroArray(size * size);
        T one = support.getOne();

        for (int i = 0; i < size; i++) {
            data[linearIndex(i, i, size)] = one;
        }

        return new MutableMatrix<>(support, data, new MatrixDimension(size));
    }

    public static <T extends AbstractNumber<T>> MutableMatrix<T> createRandomRowMatrix(
        AbstractNumberSupport<T> support,
        int length, Random random
    ) {
        return createRandomMatrix(support, 1, length, random);
    }

    public static <T extends AbstractNumber<T>> MutableMatrix<T> createRandomColumnMatrix(
        AbstractNumberSupport<T> support,
        int length, Random random
    ) {
        return createRandomMatrix(support, length, 1, random);
    }

    public static <T extends AbstractNumber<T>> MutableMatrix<T> createRandomMatrix(
        AbstractNumberSupport<T> support,
        int rowCount, int columnCount,
        Random random
    ) {
        Preconditions.ensureValidLength(rowCount);
        Preconditions.ensureValidLength(columnCount);

        T[] data = support.newArray(rowCount * columnCount);

        for (int i = 0; i < data.length; i++) {
            data[i] = support.randomNumber(random);
        }

        return new MutableMatrix<>(support, data, new MatrixDimension(rowCount, columnCount));
    }

    protected int linearIndex(int row, int column) {
        return linearIndex(row, column, dimen.getColumnCount());
    }

    private static int linearIndex(int row, int column, int columnCount) {
        return row * columnCount + column;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MutableMatrix<?> matrix &&
            o.getClass() == getClass() &&
            support.equals(matrix.support) &&
            dimen.equals(matrix.dimen) &&
            Arrays.equals(data, matrix.data);
    }

    public boolean equalsApproximate(MutableMatrix<T> other, T precision) {
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
        sb.append(getClass().getSimpleName());
        sb.append('<');
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
