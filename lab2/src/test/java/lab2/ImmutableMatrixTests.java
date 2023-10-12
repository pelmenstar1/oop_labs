package lab2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ImmutableMatrixTests {
    @Test
    public void emptyConstructorTest() {
        var matrix = new ImmutableMatrix();

        assertEquals(new MatrixDimension(0, 0), matrix.getDimension());
    }

    @Test
    public void dimensionsConstructorTest() {
        var matrix = new ImmutableMatrix(5, 3);

        assertEquals(new MatrixDimension(5, 3), matrix.getDimension());
    }

    @Test
    public void dimensionsConstructorThrowsOnInvalidDimensionsTest() {
        assertThrows(IllegalArgumentException.class, () -> new ImmutableMatrix(0, 5));
        assertThrows(IllegalArgumentException.class, () -> new ImmutableMatrix(5, 0));
        assertThrows(IllegalArgumentException.class, () -> new ImmutableMatrix(-2, -3));
    }

    @Test
    public void arrayConstructorTest() {
        var row0 = new double[]{1, 2, 3};
        var row1 = new double[]{4, 5, 6};
        var row2 = new double[]{7, 8, 9};

        var matrix = new ImmutableMatrix(new double[][]{row0, row1, row2});

        assertArrayEquals(row0, matrix.getRow(0));
        assertArrayEquals(row1, matrix.getRow(1));
        assertArrayEquals(row2, matrix.getRow(2));
    }

    @Test
    public void arrayConstructorEmptyRowsTest() {
        var matrix = new ImmutableMatrix(new double[0][]);

        assertEquals(new MatrixDimension(0, 0), matrix.getDimension());
    }

    @Test
    public void copyConstructorTest() {
        var row0 = new double[]{1, 2, 3};
        var row1 = new double[]{4, 5, 6};
        var row2 = new double[]{7, 8, 9};

        var originMatrix = new ImmutableMatrix(new double[][]{row0, row1, row2});
        var copiedMatrix = new ImmutableMatrix(originMatrix);

        assertArrayEquals(row0, copiedMatrix.getRow(0));
        assertArrayEquals(row1, copiedMatrix.getRow(1));
        assertArrayEquals(row2, copiedMatrix.getRow(2));
    }

    @Test
    public void getTest() {
        var matrix = new ImmutableMatrix(new double[][]{new double[]{0, 1}, new double[]{2, 3}});

        assertEquals(0, matrix.get(0, 0));
        assertEquals(1, matrix.get(0, 1));
        assertEquals(2, matrix.get(1, 0));
        assertEquals(3, matrix.get(1, 1));
    }

    @Test
    public void getThrowsOnInvalidIndicesTest() {
        var matrix = new ImmutableMatrix(2, 2);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.get(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.get(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.get(2, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.get(0, 2));
    }

    @Test
    public void getRowThrowsOnInvalidIndexTest() {
        var matrix = new ImmutableMatrix(2, 2);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getRow(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getRow(2));
    }

    @Test
    public void getColumnThrowsOnInvalidIndexTest() {
        var matrix = new ImmutableMatrix(2, 2);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getColumn(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getColumn(2));
    }

    public static Stream<Arguments> addThrowsOnInvalidDimensionsTestArguments() {
        return Stream.of(
            Arguments.of(2, 2, 1, 1),
            Arguments.of(2, 2, 1, 2),
            Arguments.of(2, 2, 2, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("addThrowsOnInvalidDimensionsTestArguments")
    public void addThrowsOnInvalidDimensionsTest(int thisColumnCount, int thisRowCount, int otherColumnCount, int otherRowCount) {
        var origin = new ImmutableMatrix(thisColumnCount, thisRowCount);
        var other = new ImmutableMatrix(otherColumnCount, otherRowCount);

        assertThrows(IllegalArgumentException.class, () -> origin.plus(other));
    }

    public static Stream<Arguments> multiplyByMatrixThrowsOnIncompatibleOtherMatrixTestArguments() {
        return Stream.of(
            Arguments.of(2, 1, 2, 3),
            Arguments.of(3, 1, 2, 2)
        );
    }

    @ParameterizedTest
    @MethodSource("multiplyByMatrixThrowsOnIncompatibleOtherMatrixTestArguments")
    public void multiplyByMatrixThrowsOnIncompatibleOtherMatrixTest(int thisColumnCount, int thisRowCount, int otherColumnCount, int otherRowCount) {
        var origin = new ImmutableMatrix(thisColumnCount, thisRowCount);
        var other = new ImmutableMatrix(otherColumnCount, otherRowCount);

        assertThrows(IllegalArgumentException.class, () -> origin.multiplyBy(other));
    }

    @Test
    public void transposedTest() {
        var matrix = new ImmutableMatrix(new double[][]{new double[]{1, 2, 3}, new double[]{4, 5, 6}});

        var transposedMatrix = matrix.transposed();
        assertEquals(3, transposedMatrix.getDimension().getRowCount());
        assertEquals(2, transposedMatrix.getDimension().getColumnCount());

        // 1 4
        // 2 5
        // 3 6
        assertEquals(1, transposedMatrix.get(0, 0));
        assertEquals(4, transposedMatrix.get(0, 1));
        assertEquals(2, transposedMatrix.get(1, 0));
        assertEquals(5, transposedMatrix.get(1, 1));
        assertEquals(3, transposedMatrix.get(2, 0));
        assertEquals(6, transposedMatrix.get(2, 1));
    }

    @Test
    public void transposedEmptyMatrixTest() {
        var matrix = new ImmutableMatrix();
        var transposedMatrix = matrix.transposed();

        // Transposed empty matrix is empty matrix.
        assertEquals(transposedMatrix, matrix);
    }

    @SuppressWarnings({"EqualsWithItself", "SimplifiableAssertion"}) // That's what we are testing
    @Test
    public void equalsTest() {
        var data1 = new double[][]{new double[]{1, 2}, new double[]{3, 4}};
        var data2 = new double[][]{new double[]{0, 2}, new double[]{3, 4}};

        var matrix = new ImmutableMatrix(data1);
        var matrixWithSameData = new ImmutableMatrix(data1);
        var matrixWithDifferentData = new ImmutableMatrix(data2);
        var matrixWithDifferentSize = new ImmutableMatrix(2, 3);

        assertTrue(matrix.equals(matrix));

        // Cannot be equal to the object of different type.
        assertFalse(matrix.equals(new Object()));

        assertTrue(matrix.equals(matrixWithSameData));
        assertFalse(matrix.equals(matrixWithDifferentData));
        assertFalse(matrix.equals(matrixWithDifferentSize));
    }

    @Test
    public void hashCodeTest() {
        var data1 = new double[][]{new double[]{1, 2}, new double[]{3, 4}};

        var matrix = new ImmutableMatrix(data1);
        var matrixWithSameData = new ImmutableMatrix(data1);

        int hash1 = matrix.hashCode();
        int hash2 = matrixWithSameData.hashCode();

        // There might be collisions, so we can't actually except that instances with different data have different hashes.
        // But, at least, instances with same data must have same hashes.
        assertEquals(hash1, hash2);

        // Test determinism
        int hash3 = matrix.hashCode();

        assertEquals(hash1, hash3);
    }
}
