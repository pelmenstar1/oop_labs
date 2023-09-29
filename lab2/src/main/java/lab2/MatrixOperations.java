package lab2;

/*package-private*/ class MatrixOperations {
    public static double[] getRow(double[] data, int index, int columnCount) {
        double[] row = new double[columnCount];
        System.arraycopy(data, index * columnCount, row, 0, columnCount);

        return row;
    }

    public static double[] getColumn(double[] data, int index, int columnCount) {
        int rowCount = data.length / columnCount;
        double[] column = new double[rowCount];

        for (int i = 0; i < column.length; i++) {
            column[i] = data[getLinearIndex(i, index, columnCount)];
        }

        return column;
    }

    private static int getLinearIndex(int row, int column, int columnCount) {
        return row * columnCount + column;
    }
}