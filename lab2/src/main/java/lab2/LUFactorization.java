package lab2;

public final class LUFactorization {
    private LUFactorization() {
    }

    private static <T extends AbstractNumber<T>> void luFactor(MutableMatrix<T> matrix, int[] pivots) {
        int order = matrix.getDimension().getColumnCount();

        // Initialize the pivot matrix to the identity permutation.
        for (var i = 0; i < order; i++) {
            pivots[i] = i;
        }

        AbstractNumberSupport<T> support = matrix.getSupport();
        T[] columnVec = support.newArray(order);

        T zero = support.getZero();

        // Outer loop.
        for (int j = 0; j < order; j++) {
            matrix.copyColumn(j, columnVec);

            // Apply previous transformations.
            for (int i = 0; i < order; i++) {
                // Most of the time is spent in the following dot product.
                int kmax = Math.min(i, j);

                T s = zero;
                for (int k = 0; k < kmax; k++) {
                    s = s.plus(matrix.get(i, k).multiply(columnVec[k]));
                }

                columnVec[i] = columnVec[i].minus(s);
                matrix.set(i, j, columnVec[i]);
            }

            // Find pivot and exchange if necessary.
            int pivotIndex = j;
            for (int i = j + 1; i < order; i++) {
                if (columnVec[i].compareMagnitude(columnVec[pivotIndex]) > 0) {
                    pivotIndex = i;
                }
            }

            if (pivotIndex != j) {
                for (int k = 0; k < order; k++) {
                    matrix.swapElements(pivotIndex, k, j, k);
                }

                pivots[j] = pivotIndex;
            }

            T diagValue = matrix.get(j, j);

            // Compute multipliers.
            if (!diagValue.equals(zero)) {
                final T diagInv = diagValue.inverse();

                for (int i = j + 1; i < order; i++) {
                    matrix.update(i, j, x -> x.multiply(diagInv));
                }
            }
        }
    }

    private static <T extends AbstractNumber<T>> void luSolveFactored(MutableMatrix<T> a, MutableMatrix<T> b, int[] pivots) {
        int order = a.getDimension().getColumnCount();

        // Compute the column vector P*B
        for (int i = 0; i < order; i++) {
            int pivotIndex = pivots[i];

            if (pivotIndex == i) {
                continue;
            }

            for (int j = 0; j < order; j++) {
                b.swapElements(pivotIndex, j, i, j);
            }
        }

        // Solve L*Y = P*B
        for (int k = 0; k < order; k++) {
            for (int i = k + 1; i < order; i++) {
                for (int j = 0; j < order; j++) {
                    b.set(i, j, b.get(i, j).minus(b.get(k, j).multiply(a.get(i, k))));
                }
            }
        }

        // Solve U*X = Y;
        for (int k = order - 1; k >= 0; k--) {
            final T invDiag = a.get(k, k).inverse();

            b.updateRow(k, x -> x.multiply(invDiag));

            for (int i = 0; i < k; i++) {
                for (int j = 0; j < order; j++) {
                    b.set(i, j, b.get(i, j).minus(b.get(k, j).multiply(a.get(i, k))));
                }
            }
        }
    }

    public static<T extends AbstractNumber<T>> MutableMatrix<T> inverseMatrix(MutableMatrix<T> matrix) {
        int order = matrix.getDimension().getColumnCount();

        int[] pivots = new int[order];
        luFactor(matrix, pivots);

        var result = MutableMatrix.createIdentity(matrix.getSupport(), order);
        luSolveFactored(matrix, result, pivots);

        return result;
    }
}
