package lab2;

import java.util.Random;

public class MutableMatrix<T extends AbstractNumber<T>> extends ImmutableMatrix<T> {
    public MutableMatrix(AbstractNumberSupport<T> support) {
        super(support);
    }

    public MutableMatrix(AbstractNumberSupport<T> support, int rowCount, int columnCount) {
        super(support, rowCount, columnCount);
    }

    public MutableMatrix(AbstractNumberSupport<T> support, T[][] rows) {
        super(support, rows);
    }

    public MutableMatrix(ImmutableMatrix<T> other) {
        super(other);
    }

    protected MutableMatrix(AbstractNumberSupport<T> support, T[] data, MatrixDimension dimen) {
        super(support, data, dimen);
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

    public void set(ImmutableMatrix<T> other) {
        Preconditions.ensureSameDimensions(getDimension(), other.getDimension());

        System.arraycopy(other.data, 0, data, 0, data.length);
    }

    @Override
    public MutableMatrix<T> plus(ImmutableMatrix<T> other) {
        return new MutableMatrix<>(super.plus(other));
    }

    @Override
    public MutableMatrix<T> multiplyBy(T scalar) {
        return new MutableMatrix<>(super.multiplyBy(scalar));
    }

    @Override
    public MutableMatrix<T> multiplyBy(ImmutableMatrix<T> other) {
        return new MutableMatrix<>(super.multiplyBy(other));
    }

    @Override
    public MutableMatrix<T> transposed() {
        return new MutableMatrix<>(super.transposed());
    }

    public MutableMatrix<T> inverse() {
        return new MutableMatrix<>(super.inverse());
    }

    public static<T extends AbstractNumber<T>> MutableMatrix<T> createDiagonal(
        AbstractNumberSupport<T> support,
        T[] vector
    ) {
        return new MutableMatrix<>(ImmutableMatrix.createDiagonal(support, vector));
    }

    public static<T extends AbstractNumber<T>> MutableMatrix<T> createIdentity(
        AbstractNumberSupport<T> support,
        int size
    ) {
        return new MutableMatrix<>(ImmutableMatrix.createIdentity(support, size));
    }

    public static<T extends AbstractNumber<T>> MutableMatrix<T> createRandomRowMatrix(
        AbstractNumberSupport<T> support,
        int length,
        Random random
    ) {
        return new MutableMatrix<>(ImmutableMatrix.createRandomRowMatrix(support, length, random));
    }

    public static<T extends AbstractNumber<T>> MutableMatrix<T> createRandomColumnMatrix(
        AbstractNumberSupport<T> support,
        int length,
        Random random
    ) {
        return new MutableMatrix<>(ImmutableMatrix.createRandomColumnMatrix(support, length, random));
    }
}
