package lab2;

public final class MatrixDimension {
    private final int columnCount;
    private final int rowCount;

    public MatrixDimension(int size) {
        this(size, size);
    }

    public MatrixDimension(int rowCount, int columnCount) {
        if (rowCount == 0 && columnCount == 0) {
            this.rowCount = 0;
            this.columnCount = 0;

            return;
        }

        Preconditions.ensureValidDimension(rowCount, "rowCount");
        Preconditions.ensureValidDimension(columnCount, "columnCount");

        this.columnCount = columnCount;
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public boolean hasSameComponents() {
        return rowCount == columnCount;
    }

    public MatrixDimension interchanged() {
        return new MatrixDimension(columnCount, rowCount);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MatrixDimension other &&
            columnCount == other.columnCount &&
            rowCount == other.rowCount;
    }

    @Override
    public int hashCode() {
        return columnCount * 31 + rowCount;
    }

    @Override
    public String toString() {
        return "MatrixDimension { columnCount=" + columnCount + ", rowCount=" + rowCount + " }";
    }
}
