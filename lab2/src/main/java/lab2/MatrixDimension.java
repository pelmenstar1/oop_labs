package lab2;

public final class MatrixDimension {
    private final int columnCount;
    private final int rowCount;

    public MatrixDimension(int columnCount, int rowCount) {
        if (columnCount == 0 && rowCount == 0) {
            this.columnCount = 0;
            this.rowCount = 0;

            return;
        }

        Preconditions.ensureValidDimension(columnCount, "columnCount");
        Preconditions.ensureValidDimension(rowCount, "rowCount");

        this.columnCount = columnCount;
        this.rowCount = rowCount;
    }

    public MatrixDimension(MatrixDimension other) {
        columnCount = other.columnCount;
        rowCount = other.rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public MatrixDimension interchanged() {
        return new MatrixDimension(rowCount, columnCount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatrixDimension other = (MatrixDimension) o;

        return columnCount == other.columnCount && rowCount == other.rowCount;
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
