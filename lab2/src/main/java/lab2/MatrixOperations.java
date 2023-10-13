package lab2;

/*package-private*/ class MatrixOperations {
    public static void setRows(double[] data, double[][] rows, MatrixDimension dimen) {
        int columnCount = dimen.getColumnCount();

        for (int i = 0; i < rows.length; i++) {
            double[] row = rows[i];

            if (row.length != columnCount) {
                throw new IllegalArgumentException("Length of a row should be the same as the column count of the matrix");
            }

            System.arraycopy(row, 0, data, i * columnCount, columnCount);
        }
    }
}