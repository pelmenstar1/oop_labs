package lab2;

import java.util.Random;

public class ImmutableMatrix<T extends AbstractNumber<T>> extends MutableMatrix<T> {
    public ImmutableMatrix(AbstractNumberSupport<T> support) {
        super(support);
    }

    public ImmutableMatrix(AbstractNumberSupport<T> support, int rowCount, int columnCount) {
        super(support, rowCount, columnCount);
    }

    public ImmutableMatrix(AbstractNumberSupport<T> support, T[][] rows) {
        super(support, rows);
    }

    public ImmutableMatrix(MutableMatrix<T> other) {
        super(other);
    }

    protected ImmutableMatrix(AbstractNumberSupport<T> support, T[] data, MatrixDimension dimen) {
        super(support, data, dimen);
    }

    @Override
    public void set(int row, int column, T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRow(int index, T[] row) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setColumn(int index, T[] column) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(T[][] rows) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(MutableMatrix<T> other) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableMatrix<T> plus(MutableMatrix<T> other) {
        return new ImmutableMatrix<>(super.plus(other));
    }

    @Override
    public ImmutableMatrix<T> multiplyBy(T scalar) {
        return new ImmutableMatrix<>(super.multiplyBy(scalar));
    }

    @Override
    public ImmutableMatrix<T> multiplyBy(MutableMatrix<T> other) {
        return new ImmutableMatrix<>(super.multiplyBy(other));
    }

    @Override
    public ImmutableMatrix<T> transposed() {
        return new ImmutableMatrix<>(super.transposed());
    }

    public ImmutableMatrix<T> inverse() {
        return new ImmutableMatrix<>(super.inverse());
    }

    public static ImmutableMatrix<RealNumber> createReal(double[]... data) {
        return new ImmutableMatrix<>(MutableMatrix.createReal(data));
    }

    public static<T extends AbstractNumber<T>> ImmutableMatrix<T> createDiagonal(
        AbstractNumberSupport<T> support,
        T[] vector
    ) {
        return new ImmutableMatrix<>(MutableMatrix.createDiagonal(support, vector));
    }

    public static<T extends AbstractNumber<T>> ImmutableMatrix<T> createIdentity(
        AbstractNumberSupport<T> support,
        int size
    ) {
        return new ImmutableMatrix<>(MutableMatrix.createIdentity(support, size));
    }

    public static<T extends AbstractNumber<T>> ImmutableMatrix<T> createRandomRowMatrix(
        AbstractNumberSupport<T> support,
        int length,
        Random random
    ) {
        return new ImmutableMatrix<>(MutableMatrix.createRandomRowMatrix(support, length, random));
    }

    public static<T extends AbstractNumber<T>> ImmutableMatrix<T> createRandomColumnMatrix(
        AbstractNumberSupport<T> support,
        int length,
        Random random
    ) {
        return new ImmutableMatrix<>(MutableMatrix.createRandomColumnMatrix(support, length, random));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
