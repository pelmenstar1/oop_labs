package lab2;

import java.util.Random;

public class GenericMutableMatrix<T extends AbstractNumber<T>> extends GenericImmutableMatrix<T> {
    public GenericMutableMatrix(AbstractNumberSupport<T> support) {
        super(support);
    }

    public GenericMutableMatrix(AbstractNumberSupport<T> support, int columnCount, int rowCount) {
        super(support, columnCount, rowCount);
    }

    public GenericMutableMatrix(AbstractNumberSupport<T> support, T[][] rows) {
        super(support, rows);
    }

    public GenericMutableMatrix(GenericImmutableMatrix<T> other) {
        super(other);
    }

    protected GenericMutableMatrix(AbstractNumberSupport<T> support, T[] data, MatrixDimension dimen) {
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

        GenericMatrixOperations.setRows(data, rows, dimen);
    }

    public void set(GenericImmutableMatrix<T> other) {
        Preconditions.ensureSameDimensions(getDimension(), other.getDimension());

        System.arraycopy(other.data, 0, data, 0, data.length);
    }

    @Override
    public GenericMutableMatrix<T> plus(GenericImmutableMatrix<T> other) {
        return new GenericMutableMatrix<>(super.plus(other));
    }

    @Override
    public GenericMutableMatrix<T> multiplyBy(T scalar) {
        return new GenericMutableMatrix<>(super.multiplyBy(scalar));
    }

    @Override
    public GenericMutableMatrix<T> multiplyBy(GenericImmutableMatrix<T> other) {
        return new GenericMutableMatrix<>(super.multiplyBy(other));
    }

    @Override
    public GenericMutableMatrix<T> transposed() {
        return new GenericMutableMatrix<>(super.transposed());
    }

    public GenericMutableMatrix<T> inverse() {
        return new GenericMutableMatrix<>(super.inverse());
    }

    public static<T extends AbstractNumber<T>> GenericMutableMatrix<T> createDiagonal(
        AbstractNumberSupport<T> support,
        T[] vector
    ) {
        return new GenericMutableMatrix<>(GenericImmutableMatrix.createDiagonal(support, vector));
    }

    public static<T extends AbstractNumber<T>> GenericMutableMatrix<T> createIdentity(
        AbstractNumberSupport<T> support,
        int size
    ) {
        return new GenericMutableMatrix<>(GenericImmutableMatrix.createIdentity(support, size));
    }

    public static<T extends AbstractNumber<T>> GenericMutableMatrix<T> createRandomRowMatrix(
        AbstractNumberSupport<T> support,
        int length,
        Random random
    ) {
        return new GenericMutableMatrix<>(GenericImmutableMatrix.createRandomRowMatrix(support, length, random));
    }

    public static<T extends AbstractNumber<T>> GenericMutableMatrix<T> createRandomColumnMatrix(
        AbstractNumberSupport<T> support,
        int length,
        Random random
    ) {
        return new GenericMutableMatrix<>(GenericImmutableMatrix.createRandomColumnMatrix(support, length, random));
    }
}
