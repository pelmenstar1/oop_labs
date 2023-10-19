package lab2;

/*package-private*/ class GenericMatrixOperations {
    public static<T extends AbstractNumber<T>> void setRows(T[] data, T[][] rows, MatrixDimension dimen) {
        int columnCount = dimen.getColumnCount();

        for (int i = 0; i < rows.length; i++) {
            T[] row = rows[i];

            if (row.length != columnCount) {
                throw new IllegalArgumentException("Length of a row should be the same as the column count of the matrix");
            }

            System.arraycopy(row, 0, data, i * columnCount, columnCount);
        }
    }

    public static void setRowsReal(RealNumber[] data, double[][] rows, MatrixDimension dimen) {
        int columnCount = dimen.getColumnCount();

        for (int i = 0; i < rows.length; i++) {
            double[] row = rows[i];

            if (row.length != columnCount) {
                throw new IllegalArgumentException("Length of a row should be the same as the column count of the matrix");
            }

            int offset = i * columnCount;

            for (int j = 0; j < columnCount; j++) {
                data[offset + j] = new RealNumber(row[j]);
            }
        }
    }
}
