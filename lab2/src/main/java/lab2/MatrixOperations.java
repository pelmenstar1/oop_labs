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

    private static int getLinearIndex(int row, int column, int columnCount) {
        return row * columnCount + column;
    }
}