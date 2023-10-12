package lab2;

/*package-private*/ class MatrixOperations {
    public static double[] getRow(double[] data, int index, MatrixDimension dimen) {
        int columnCount = dimen.getColumnCount();

        double[] row = new double[columnCount];
        System.arraycopy(data, index * columnCount, row, 0, columnCount);

        return row;
    }

    public static double[] getColumn(double[] data, int index, MatrixDimension dimen) {
        int rowCount = dimen.getRowCount();
        int columnCount = dimen.getColumnCount();

        double[] column = new double[rowCount];

        for (int i = 0; i < column.length; i++) {
            column[i] = data[getLinearIndex(i, index, columnCount)];
        }

        return column;
    }

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

    public static double[] add(double[] matrix1, double[] matrix2) {
        double[] output = new double[matrix1.length];

        for (int i = 0; i < matrix1.length; i++) {
            output[i] = matrix1[i] + matrix2[i];
        }

        return output;
    }

    public static double[] multiplyByScalar(double[] matrix, double multiplier) {
        double[] output = new double[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            output[i] = matrix[i] * multiplier;
        }

        return output;
    }

    private static int getLinearIndex(int row, int column, int columnCount) {
        return row * columnCount + column;
    }
}