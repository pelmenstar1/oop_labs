package lab2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ImmutableMatrixTests {
    private static final class FakeRandom extends Random {
        private int index;

        @Override
        public double nextDouble() {
            return index++;
        }
    }

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

    public static Stream<Arguments> getRowTestArguments() {
        var matrix1 = new ImmutableMatrix(new double[][]{
            new double[]{0, 1, 2},
            new double[]{3, 4, 5},
            new double[]{6, 8, 9}
        });

        var matrix2 = new ImmutableMatrix(new double[][]{new double[]{1.0}});
        var matrix3 = new ImmutableMatrix(new double[][]{
            new double[]{0, 1},
            new double[]{2, 3},
            new double[]{4, 5}
        });

        return Stream.of(
            Arguments.of(matrix1, 0, new double[]{0, 1, 2}),
            Arguments.of(matrix1, 2, new double[]{6, 8, 9}),
            Arguments.of(matrix2, 0, new double[]{1.0}),
            Arguments.of(matrix3, 1, new double[]{2, 3})
        );
    }

    @ParameterizedTest
    @MethodSource("getRowTestArguments")
    public void getRowTest(ImmutableMatrix matrix, int index, double[] expectedResult) {
        double[] actualResult = matrix.getRow(index);

        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void getRowThrowsOnInvalidIndexTest() {
        var matrix = new ImmutableMatrix(2, 2);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getRow(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getRow(2));
    }

    public static Stream<Arguments> getColumnTestArguments() {
        var matrix1 = new ImmutableMatrix(new double[][]{
            new double[]{0, 1, 2},
            new double[]{3, 4, 5},
            new double[]{6, 8, 9}
        });

        var matrix2 = new ImmutableMatrix(new double[][]{new double[]{1.0}});
        var matrix3 = new ImmutableMatrix(new double[][]{
            new double[]{0, 1},
            new double[]{2, 3},
            new double[]{4, 5}
        });

        return Stream.of(
            Arguments.of(matrix1, 0, new double[]{0, 3, 6}),
            Arguments.of(matrix1, 2, new double[]{2, 5, 9}),
            Arguments.of(matrix2, 0, new double[]{1}),
            Arguments.of(matrix3, 1, new double[]{1, 3, 5})
        );
    }

    @ParameterizedTest
    @MethodSource("getColumnTestArguments")
    public void getColumnTest(ImmutableMatrix matrix, int index, double[] expectedResult) {
        double[] actualResult = matrix.getColumn(index);

        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void getColumnThrowsOnInvalidIndexTest() {
        var matrix = new ImmutableMatrix(2, 2);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getColumn(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getColumn(2));
    }

    @Test
    public void addTest() {
        var matrix1 = new ImmutableMatrix(new double[][]{
            new double[]{0, 1, 2},
            new double[]{3, 4, 5},
            new double[]{6, 8, 9}
        });

        var matrix2 = new ImmutableMatrix(new double[][]{
            new double[]{2, 3, 4},
            new double[]{5, 6, 7},
            new double[]{8, 9, 10}
        });

        var expectedResult = new ImmutableMatrix(new double[][]{
            new double[]{2, 4, 6},
            new double[]{8, 10, 12},
            new double[]{14, 17, 19}
        });

        ImmutableMatrix actual = matrix1.plus(matrix2);

        assertEquals(expectedResult, actual);
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

    @Test
    public void multiplyByScalarTest() {
        var matrix = new ImmutableMatrix(new double[][]{
            new double[]{0, 1, 2},
            new double[]{3, 4, 5},
            new double[]{6, 8, 9}
        });

        var expectedResult = new ImmutableMatrix(new double[][]{
            new double[]{0, 2, 4},
            new double[]{6, 8, 10},
            new double[]{12, 16, 18}
        });

        var actual = matrix.multiplyBy(2.0);

        assertEquals(expectedResult, actual);
    }

    public static Stream<Arguments> multiplyByMatrixTestArguments() {
        var matrix1 = new ImmutableMatrix(new double[][]{
            new double[]{0, 1, 2},
            new double[]{3, 4, 5},
            new double[]{6, 8, 9}
        });

        var matrix2 = new ImmutableMatrix(new double[][]{
            new double[]{0, 2, 4},
            new double[]{6, 8, 10},
            new double[]{12, 16, 18}
        });

        var matrix_1_2_result = new ImmutableMatrix(new double[][]{
            new double[]{30, 40, 46},
            new double[]{84, 118, 142},
            new double[]{156, 220, 266}
        });

        var matrix3 = new ImmutableMatrix(new double[][]{
            new double[]{1, 2},
            new double[]{3, 4}
        });

        var matrix4 = new ImmutableMatrix(new double[][]{
            new double[]{10, 20},
            new double[]{30, 40}
        });

        var matrix_3_4_result = new ImmutableMatrix(new double[][]{
            new double[]{70, 100},
            new double[]{150, 220}
        });

        var matrix5 = new ImmutableMatrix(new double[][]{
            new double[]{1, 2},
            new double[]{3, 4},
            new double[]{5, 6}
        });

        var matrix6 = new ImmutableMatrix(new double[][]{
            new double[]{1, 2, 3},
            new double[]{4, 5, 6}
        });

        var matrix_5_6_result = new ImmutableMatrix(new double[][]{
            new double[]{9, 12, 15},
            new double[]{19, 26, 33},
            new double[]{29, 40, 51}
        });

        return Stream.of(
            Arguments.of(matrix1, matrix2, matrix_1_2_result),
            Arguments.of(matrix3, matrix4, matrix_3_4_result),
            Arguments.of(matrix5, matrix6, matrix_5_6_result)
        );
    }

    @ParameterizedTest
    @MethodSource("multiplyByMatrixTestArguments")
    public void multiplyByMatrixTest(ImmutableMatrix a, ImmutableMatrix b, ImmutableMatrix expectedResult) {
        ImmutableMatrix actual = a.multiplyBy(b);

        assertEquals(expectedResult, actual);
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

    public static Stream<Arguments> createDiagonalMatrixTestArguments() {
        double[] vec1 = new double[]{1};
        double[] vec2 = new double[]{1, 2};
        double[] vec3 = new double[]{1, 2, 3};

        var result1 = new ImmutableMatrix(new double[][]{new double[]{1}});
        var result2 = new ImmutableMatrix(new double[][]{
            new double[]{1, 0},
            new double[]{0, 2}
        });

        var result3 = new ImmutableMatrix(new double[][]{
            new double[]{1, 0, 0},
            new double[]{0, 2, 0},
            new double[]{0, 0, 3}
        });

        return Stream.of(
            Arguments.of(vec1, result1),
            Arguments.of(vec2, result2),
            Arguments.of(vec3, result3)
        );
    }

    @ParameterizedTest
    @MethodSource("createDiagonalMatrixTestArguments")
    public void createDiagonalMatrixTest(double[] vector, ImmutableMatrix expectedMatrix) {
        ImmutableMatrix actualMatrix = ImmutableMatrix.createDiagonal(vector);

        assertEquals(expectedMatrix, actualMatrix);
    }

    public static Stream<Arguments> createIdentityTestArguments() {
        var matrix1 = new ImmutableMatrix(new double[][]{new double[]{1.0}});
        var matrix2 = new ImmutableMatrix(new double[][]{
            new double[]{1.0, 0.0},
            new double[]{0.0, 1.0}
        });
        var matrix3 = new ImmutableMatrix(new double[][]{
            new double[]{1.0, 0.0, 0.0},
            new double[]{0.0, 1.0, 0.0},
            new double[]{0.0, 0.0, 1.0}
        });

        return Stream.of(
            Arguments.of(1, matrix1),
            Arguments.of(2, matrix2),
            Arguments.of(3, matrix3)
        );
    }

    @ParameterizedTest
    @MethodSource("createIdentityTestArguments")
    public void createIdentityTest(int size, ImmutableMatrix expectedMatrix) {
        var actualMatrix = ImmutableMatrix.createIdentity(size);

        assertEquals(expectedMatrix, actualMatrix);
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

    @Test
    public void createRandomRowMatrixTest() {
        var matrix = ImmutableMatrix.createRandomRowMatrix(3, new FakeRandom());
        var expectedResult = new ImmutableMatrix(new double[][]{new double[]{0, 1, 2}});

        assertEquals(expectedResult, matrix);
    }

    @Test
    public void createRandomColumnMatrixTest() {
        var matrix = ImmutableMatrix.createRandomColumnMatrix(3, new FakeRandom());
        var expectedResult = new ImmutableMatrix(new double[][]{
            new double[]{0},
            new double[]{1},
            new double[]{2}
        });

        assertEquals(expectedResult, matrix);
    }
}
