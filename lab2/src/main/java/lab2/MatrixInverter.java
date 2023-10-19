package lab2;

import java.util.Arrays;

public final class MatrixInverter<T extends AbstractNumber<T>> {
    private final GenericImmutableMatrix<T> matrix;
   // private final GenericMutableMatrix<T> augmented;

    public MatrixInverter(GenericImmutableMatrix<T> matrix) {


        this.matrix = matrix;
        /*
        MatrixDimension dimen = matrix.getDimension();
        Preconditions.ensureSquareDimension(dimen);
        int size = dimen.getRowCount();

        augmented = new GenericMutableMatrix<>(matrix.getSupport(), size + 1, size);
        clearAugmentedMatrix();
        */
    }

    private AbstractNumberSupport<T> numberSupport() {
        return matrix.getSupport();
    }

    /*
    private void clearAugmentedMatrix() {
        MatrixDimension dimen = matrix.getDimension();
        T zero = numberSupport().getZero();

        for (int i = 0; i < dimen.getRowCount(); i++) {
            for (int j = 0; j < dimen.getColumnCount(); j++) {
                augmented.set(i, j, matrix.get(i, j));
            }

            augmented.set(i, dimen.getColumnCount(), zero);
        }
    }
    */

    public GenericImmutableMatrix<T> compute() {
        var result = new GenericMutableMatrix<>(matrix);
        int size = matrix.getDimension().getRowCount();

        T one = numberSupport().getOne();
        T zero = numberSupport().getZero();

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

        /*
        int size = matrix.getDimension().getRowCount();
        AbstractNumberSupport<T> support = matrix.getSupport();

        T[] solveVector = support.newArray(size);
        T[] linearSystemResult = support.newArray(size);

        T zero = support.getZero();
        T one = support.getOne();

        Arrays.fill(solveVector, zero);
        Arrays.fill(linearSystemResult, zero);

        var resultMatrix = new GenericMutableMatrix<>(support, size, size);

        for (int i = 0; i < size; i++) {
            if (i > 0) {
                solveVector[i - 1] = zero;
            }

            solveVector[i] = one;

            solveLinearSystem(solveVector, linearSystemResult);
            resultMatrix.setColumn(i, linearSystemResult);


        }

        return resultMatrix;
        */
    }

    /*
    private void solveLinearSystem(T[] vector, T[] outVector) {
        // Extend the augmented matrix
        augmented.setColumn(vector.length, vector);

        triangularizeAugmentedMatrixWithPivoting();

        T zero = numberSupport().getZero();
        if (augmented.diagonalProduct().absolute().equals(zero)) {
            throw new IllegalStateException("Cannot find inverse of a matrix");
        }

        Arrays.fill(outVector, zero);
        backSubstitution(outVector);
        clearAugmentedMatrix();
    }

    private void triangularizeAugmentedMatrixWithPivoting() {
        MatrixDimension dimen = augmented.getDimension();
        T zero = numberSupport().getZero();

        for (int i = 0; i < dimen.getRowCount() - 1; i++) {
            int maxIndex = i;
            T maxItem = augmented.get(i, i).absolute();

            for (int k = i + 1; k < dimen.getRowCount(); k++) {
                T value = augmented.get(k, i);

                if (value.absolute().compareTo(maxItem.absolute()) > 0) {
                    maxItem = value;
                    maxIndex = k;
                }
            }

            if (maxItem.equals(zero)) {
                throw new IllegalArgumentException("This system can't be solved.");
            }

            if (maxIndex > i) {
                augmented.swapRows(maxIndex, i);
            }

            for (int j = i + 1; j < dimen.getRowCount(); j++) {
                T c = augmented.get(j, i).divide(augmented.get(i, i));
                augmented.set(j, i, zero);

                for (int k = i + 1; k < dimen.getColumnCount(); k++) {
                    T newVal = augmented.get(j, k).minus(augmented.get(i, k).multiply(c));

                    augmented.set(j, k, newVal);
                }
            }
        }
    }

    private void backSubstitution(T[] result) {
        int size = matrix.getDimension().getRowCount();
        T zero = numberSupport().getZero();

        for (int i = size - 1; i >= 0; i--) {
            T acc = zero;

            for (int j = i + 1; j < size; j++) {
                acc = acc.plus(result[j].multiply(augmented.get(i, j)));
            }

            result[i] = augmented.get(i, size).minus(acc).divide(augmented.get(i, i));
        }
    }
    */
}
