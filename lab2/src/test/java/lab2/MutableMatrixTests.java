package lab2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MutableMatrixTests {
    private static final class FakeRandom extends Random {
        private int index;

        @Override
        public double nextDouble() {
            return index++;
        }
    }

    public static Stream<Arguments> inverseTestArguments() {
        var realInputMat1x1 = MutableMatrix.createReal(new double[]{2});

        var realInputMat2x2 = MutableMatrix.createReal(new double[][]{
            {-1, 1.5,},
            {1, -1}
        });

        var realInputMat3x3 = MutableMatrix.createReal(new double[][]{
            {1, 2, 3},
            {0, 1, 4},
            {5, 6, 0}
        });

        var realInputMat3x3_2 = MutableMatrix.createReal(new double[][]{
            {0, 1, 1},
            {1, 0, -2},
            {-2, -3, 2}
        });

        var realInputMat4x4 = MutableMatrix.createReal(new double[][]{
            {1, 2, 3, 4},
            {6, 7, 8, 9},
            {20, 20, 21, 22},
            {19, 7, 8, 1}
        });

        var realOutputMat1x1 = MutableMatrix.createReal(
            new double[]{0.5}
        );

        var realOutputMat2x2 = MutableMatrix.createReal(new double[][]{
            {2, 3},
            {2, 2}
        });

        var realOutputMat3x3 = MutableMatrix.createReal(new double[][]{
            {-24, 18, 5},
            {20, -15, -4},
            {-5, 4, 1}
        });

        var realOutputMat3x3_2 = MutableMatrix.createReal(new double[][]{
            {6, 5, 2},
            {-2, -2, -1},
            {3, 2, 1}
        });

        var realOutputMat4x4 = MutableMatrix.createReal(new double[][]{
            {2.6, -3.6, 1, 0},
            {-2.575, 2.075, -0.375, -0.125},
            {-4.45, 7.45, -2.25, 0.25},
            {4.225, -5.725, 1.625, -0.125}
        });

        var complexInput2x2 = new MutableMatrix<>(ComplexNumber.support(), new ComplexNumber[][]{
            {new ComplexNumber(1, -1), new ComplexNumber(2)},
            {new ComplexNumber(0), new ComplexNumber(2, 3)}
        });

        var complexInput3x3 = new MutableMatrix<>(ComplexNumber.support(), new ComplexNumber[][]{
            {new ComplexNumber(1, -1), new ComplexNumber(2), new ComplexNumber(3)},
            {new ComplexNumber(0), new ComplexNumber(2, 3), new ComplexNumber(4)},
            {new ComplexNumber(1, 1), new ComplexNumber(0), new ComplexNumber(1)}
        });

        var complexInput3x3_2 = new MutableMatrix<>(ComplexNumber.support(), new ComplexNumber[][]{
            {new ComplexNumber(0), new ComplexNumber(1, 1), new ComplexNumber(3, 1)},
            {new ComplexNumber(1, -1), new ComplexNumber(0), new ComplexNumber(4, -3)},
            {new ComplexNumber(0), new ComplexNumber(1), new ComplexNumber(0)}
        });

        var complexOutput2x2 = new MutableMatrix<>(ComplexNumber.support(), new ComplexNumber[][]{
            {new ComplexNumber(0.5, 0.5), new ComplexNumber(-5.0 / 13, 1.0 / 13)},
            {new ComplexNumber(0), new ComplexNumber(2.0 / 13, -3.0 / 13)}
        });

        var complexOutput3x3 = new MutableMatrix<>(ComplexNumber.support(), new ComplexNumber[][]{
            {new ComplexNumber(7.0 / 146, 15.0 / 73), new ComplexNumber(-8.0 / 73, -3.0 / 73), new ComplexNumber(43.0 / 146, -33.0 / 73)},
            {new ComplexNumber(10.0 / 73, 22.0 / 73), new ComplexNumber(-2.0 / 73, -19.0 / 73), new ComplexNumber(-22.0 / 73, 10.0 / 73)},
            {new ComplexNumber(23.0 / 146, -37.0 / 146), new ComplexNumber(5.0 / 73, 11.0 / 73), new ComplexNumber(37.0 / 146, 23.0 / 146)}
        });

        var complexOutput3x3_2 = new MutableMatrix<>(ComplexNumber.support(), new ComplexNumber[][]{
            {new ComplexNumber(-1.1, 0.2), new ComplexNumber(0.5, 0.5), new ComplexNumber(1.3, 0.9)},
            {new ComplexNumber(0), new ComplexNumber(0), new ComplexNumber(1)},
            {new ComplexNumber(0.3, -0.1), new ComplexNumber(0), new ComplexNumber(-0.4, -0.2)}
        });

        return Stream.of(
            Arguments.of(realInputMat1x1, realOutputMat1x1),
            Arguments.of(realInputMat2x2, realOutputMat2x2),
            Arguments.of(realInputMat3x3, realOutputMat3x3),
            Arguments.of(realInputMat3x3_2, realOutputMat3x3_2),
            Arguments.of(realInputMat4x4, realOutputMat4x4),
            Arguments.of(complexInput2x2, complexOutput2x2),
            Arguments.of(complexInput3x3, complexOutput3x3),
            Arguments.of(complexInput3x3_2, complexOutput3x3_2)
        );
    }

    @ParameterizedTest
    @MethodSource("inverseTestArguments")
    public <T extends AbstractNumber<T>> void inverseTest(MutableMatrix<T> input, MutableMatrix<T> expectedResult) {
        var actualResult = input.inverse();

        assertTrue(
            expectedResult.equalsApproximate(actualResult, input.getSupport().getPrecision()),
            "actual: " + actualResult + "\nexpected: " + expectedResult
        );
    }

    @Test
    public void emptyConstructorTest() {
        var matrix = new MutableMatrix<>(RealNumber.support());

        assertEquals(new MatrixDimension(0, 0), matrix.getDimension());
    }

    @Test
    public void dimensionsConstructorTest() {
        var matrix = new MutableMatrix<>(RealNumber.support(), 3, 5);

        assertEquals(new MatrixDimension(3, 5), matrix.getDimension());
    }

    public static Stream<Arguments> dimensionsConstructorThrowsOnInvalidDimensionsTestArguments() {
        return Stream.of(
            Arguments.of(0, 5),
            Arguments.of(5, 0),
            Arguments.of(-2, -3)
        );
    }

    @ParameterizedTest
    @MethodSource("dimensionsConstructorThrowsOnInvalidDimensionsTestArguments")
    public void dimensionsConstructorThrowsOnInvalidDimensionsTest(int columnCount, int rowCount) {
        assertThrows(IllegalArgumentException.class, () -> new MutableMatrix<>(RealNumber.support(), rowCount, columnCount));
    }

    @Test
    public void arrayConstructorTest() {
        var row0 = new RealNumber[]{new RealNumber(1), new RealNumber(2), new RealNumber(3)};
        var row1 = new RealNumber[]{new RealNumber(4), new RealNumber(5), new RealNumber(6)};
        var row2 = new RealNumber[]{new RealNumber(7), new RealNumber(8), new RealNumber(9)};

        var matrix = new MutableMatrix<>(RealNumber.support(), new RealNumber[][]{row0, row1, row2});

        assertArrayEquals(row0, matrix.getRow(0));
        assertArrayEquals(row1, matrix.getRow(1));
        assertArrayEquals(row2, matrix.getRow(2));
    }

    @Test
    public void arrayConstructorEmptyRowsTest() {
        var matrix = new MutableMatrix<>(RealNumber.support(), new RealNumber[0][]);

        assertEquals(new MatrixDimension(0, 0), matrix.getDimension());
    }

    @Test
    public void getTest() {
        var matrix = new MutableMatrix<>(RealNumber.support(), new RealNumber[][]{
            new RealNumber[]{new RealNumber(0), new RealNumber(1)},
            new RealNumber[]{new RealNumber(2), new RealNumber(3)}
        });

        assertEquals(new RealNumber(0), matrix.get(0, 0));
        assertEquals(new RealNumber(1), matrix.get(0, 1));
        assertEquals(new RealNumber(2), matrix.get(1, 0));
        assertEquals(new RealNumber(3), matrix.get(1, 1));
    }

    @Test
    public void getThrowsOnInvalidIndicesTest() {
        var matrix = new MutableMatrix<>(RealNumber.support(), 2, 2);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.get(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.get(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.get(2, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.get(0, 2));
    }

    public static Stream<Arguments> getRowTestArguments() {
        var row1 = new RealNumber[]{new RealNumber(0), new RealNumber(1), new RealNumber(2)};
        var row2 = new RealNumber[]{new RealNumber(6), new RealNumber(8), new RealNumber(9)};
        var row3 = new RealNumber[]{new RealNumber(1.0)};
        var row4 = new RealNumber[]{new RealNumber(2), new RealNumber(3)};

        var matrix1 = new MutableMatrix<>(RealNumber.support(), new RealNumber[][]{
            row1,
            new RealNumber[]{new RealNumber(3), new RealNumber(4), new RealNumber(5)},
            row2
        });

        var matrix2 = new MutableMatrix<>(RealNumber.support(), new RealNumber[][]{row3});

        var matrix3 = new MutableMatrix<>(RealNumber.support(), new RealNumber[][]{
            new RealNumber[]{new RealNumber(0), new RealNumber(1)},
            row4,
            new RealNumber[]{new RealNumber(4), new RealNumber(5)}
        });

        return Stream.of(
            Arguments.of(matrix1, 0, row1),
            Arguments.of(matrix1, 2, row2),
            Arguments.of(matrix2, 0, row3),
            Arguments.of(matrix3, 1, row4)
        );
    }

    @ParameterizedTest
    @MethodSource("getRowTestArguments")
    public <T extends AbstractNumber<T>> void getRowTest(MutableMatrix<T> matrix, int index, T[] expectedResult) {
        T[] actualResult = matrix.getRow(index);

        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void getRowThrowsOnInvalidIndexTest() {
        var matrix = new MutableMatrix<>(RealNumber.support(), 2, 2);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getRow(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getRow(2));
    }

    public static Stream<Arguments> getColumnTestArguments() {
        var row1 = new RealNumber[]{new RealNumber(1.0)};

        var matrix1 = new MutableMatrix<>(RealNumber.support(), new RealNumber[][]{
            new RealNumber[]{new RealNumber(0), new RealNumber(1), new RealNumber(2)},
            new RealNumber[]{new RealNumber(3), new RealNumber(4), new RealNumber(5)},
            new RealNumber[]{new RealNumber(6), new RealNumber(8), new RealNumber(9)}
        });

        var matrix2 = new MutableMatrix<>(RealNumber.support(), new RealNumber[][]{row1});
        var matrix3 = new MutableMatrix<>(RealNumber.support(), new RealNumber[][]{
            new RealNumber[]{new RealNumber(0), new RealNumber(1)},
            new RealNumber[]{new RealNumber(2), new RealNumber(3)},
            new RealNumber[]{new RealNumber(4), new RealNumber(5)}
        });

        return Stream.of(
            Arguments.of(matrix1, 0, new RealNumber[]{new RealNumber(0), new RealNumber(3), new RealNumber(6)}),
            Arguments.of(matrix1, 2, new RealNumber[]{new RealNumber(2), new RealNumber(5), new RealNumber(9)}),
            Arguments.of(matrix2, 0, row1),
            Arguments.of(matrix3, 1, new RealNumber[]{new RealNumber(1), new RealNumber(3), new RealNumber(5)})
        );
    }

    @ParameterizedTest
    @MethodSource("getColumnTestArguments")
    public <T extends AbstractNumber<T>> void getColumnTest(MutableMatrix<T> matrix, int index, T[] expectedResult) {
        T[] actualResult = matrix.getColumn(index);

        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    public void getColumnThrowsOnInvalidIndexTest() {
        var matrix = new MutableMatrix<>(RealNumber.support(), 2, 2);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getColumn(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.getColumn(2));
    }

    @Test
    public void addTest() {
        var matrix1 = MutableMatrix.createReal(
            new double[]{0, 1, 2},
            new double[]{3, 4, 5},
            new double[]{6, 8, 9}
        );

        var matrix2 = MutableMatrix.createReal(
            new double[]{2, 3, 4},
            new double[]{5, 6, 7},
            new double[]{8, 9, 10}
        );

        var expectedResult = MutableMatrix.createReal(
            new double[]{2, 4, 6},
            new double[]{8, 10, 12},
            new double[]{14, 17, 19}
        );

        MutableMatrix<RealNumber> actual = matrix1.plus(matrix2);

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
        var origin = new MutableMatrix<>(RealNumber.support(), thisRowCount, thisColumnCount);
        var other = new MutableMatrix<>(RealNumber.support(), otherRowCount, otherColumnCount);

        assertThrows(IllegalArgumentException.class, () -> origin.plus(other));
    }

    @Test
    public void multiplyByScalarTest() {
        var matrix = MutableMatrix.createReal(
            new double[]{0, 1, 2},
            new double[]{3, 4, 5},
            new double[]{6, 8, 9}
        );

        var expectedResult = MutableMatrix.createReal(
            new double[]{0, 2, 4},
            new double[]{6, 8, 10},
            new double[]{12, 16, 18}
        );

        var actual = matrix.multiplyBy(new RealNumber(2.0));

        assertEquals(expectedResult, actual);
    }

    public static Stream<Arguments> multiplyByMatrixTestArguments() {
        var matrix1 = MutableMatrix.createReal(
            new double[]{0, 1, 2},
            new double[]{3, 4, 5},
            new double[]{6, 8, 9}
        );

        var matrix2 = MutableMatrix.createReal(
            new double[]{0, 2, 4},
            new double[]{6, 8, 10},
            new double[]{12, 16, 18}
        );

        var matrix_1_2_result = MutableMatrix.createReal(
            new double[]{30, 40, 46},
            new double[]{84, 118, 142},
            new double[]{156, 220, 266}
        );

        var matrix3 = MutableMatrix.createReal(
            new double[]{1, 2},
            new double[]{3, 4}
        );

        var matrix4 = MutableMatrix.createReal(
            new double[]{10, 20},
            new double[]{30, 40}
        );

        var matrix_3_4_result = MutableMatrix.createReal(
            new double[]{70, 100},
            new double[]{150, 220}
        );

        var matrix5 = MutableMatrix.createReal(
            new double[]{1, 2},
            new double[]{3, 4},
            new double[]{5, 6}
        );

        var matrix6 = MutableMatrix.createReal(
            new double[]{1, 2, 3},
            new double[]{4, 5, 6}
        );

        var matrix_5_6_result = MutableMatrix.createReal(
            new double[]{9, 12, 15},
            new double[]{19, 26, 33},
            new double[]{29, 40, 51}
        );

        return Stream.of(
            Arguments.of(matrix1, matrix2, matrix_1_2_result),
            Arguments.of(matrix3, matrix4, matrix_3_4_result),
            Arguments.of(matrix5, matrix6, matrix_5_6_result)
        );
    }

    @ParameterizedTest
    @MethodSource("multiplyByMatrixTestArguments")
    public <T extends AbstractNumber<T>> void multiplyByMatrixTest(MutableMatrix<T> a, MutableMatrix<T> b, MutableMatrix<T> expectedResult) {
        MutableMatrix<T> actual = a.multiplyBy(b);

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
        var origin = new MutableMatrix<>(RealNumber.support(), thisRowCount, thisColumnCount);
        var other = new MutableMatrix<>(RealNumber.support(), otherRowCount, otherColumnCount);

        assertThrows(IllegalArgumentException.class, () -> origin.multiplyBy(other));
    }

    @Test
    public void transposedTest() {
        var matrix = MutableMatrix.createReal(
            new double[]{1, 2, 3},
            new double[]{4, 5, 6}
        );

        var transposedMatrix = matrix.transposed();
        assertEquals(3, transposedMatrix.getDimension().getRowCount());
        assertEquals(2, transposedMatrix.getDimension().getColumnCount());

        // 1 4
        // 2 5
        // 3 6
        assertEquals(new RealNumber(1), transposedMatrix.get(0, 0));
        assertEquals(new RealNumber(4), transposedMatrix.get(0, 1));
        assertEquals(new RealNumber(2), transposedMatrix.get(1, 0));
        assertEquals(new RealNumber(5), transposedMatrix.get(1, 1));
        assertEquals(new RealNumber(3), transposedMatrix.get(2, 0));
        assertEquals(new RealNumber(6), transposedMatrix.get(2, 1));
    }

    @Test
    public void transposedEmptyMatrixTest() {
        var matrix = new MutableMatrix<>(RealNumber.support());
        var transposedMatrix = matrix.transposed();

        // Transposed empty matrix is empty matrix.
        assertEquals(transposedMatrix, matrix);
    }

    public static Stream<Arguments> createRealTestArguments() {
        var rows1 = new double[][]{
            new double[]{1}
        };

        var rows2 = new double[][]{
            new double[]{1, 2},
            new double[]{3, 4}
        };

        var rows3 = new double[][]{
            new double[]{1, 2, 3},
            new double[]{4, 5, 6}
        };

        var rows4 = new double[0][];

        var result1 = new MutableMatrix<>(RealNumber.support(), new RealNumber[][]{
            new RealNumber[]{new RealNumber(1)}
        });

        var result2 = new MutableMatrix<>(RealNumber.support(), new RealNumber[][]{
            new RealNumber[]{new RealNumber(1), new RealNumber(2)},
            new RealNumber[]{new RealNumber(3), new RealNumber(4)}
        });

        var result3 = new MutableMatrix<>(RealNumber.support(), new RealNumber[][]{
            new RealNumber[]{new RealNumber(1), new RealNumber(2), new RealNumber(3),},
            new RealNumber[]{new RealNumber(4), new RealNumber(5), new RealNumber(6)}
        });

        var result4 = new MutableMatrix<>(RealNumber.support());

        return Stream.of(
            Arguments.of(rows1, result1),
            Arguments.of(rows2, result2),
            Arguments.of(rows3, result3),
            Arguments.of(rows4, result4)
        );
    }

    @ParameterizedTest
    @MethodSource("createRealTestArguments")
    public void createRealTest(double[][] doubleRows, MutableMatrix<RealNumber> expectedResult) {
        var actualResult = MutableMatrix.createReal(doubleRows);

        assertEquals(expectedResult, actualResult);
    }

    public static Stream<Arguments> createDiagonalMatrixTestArguments() {
        var vec1 = new RealNumber[]{new RealNumber(1)};
        var vec2 = new RealNumber[]{new RealNumber(1), new RealNumber(2)};
        var vec3 = new RealNumber[]{new RealNumber(1), new RealNumber(2), new RealNumber(3)};

        var result1 = MutableMatrix.createReal(new double[][]{new double[]{1}});
        var result2 = MutableMatrix.createReal(
            new double[]{1, 0},
            new double[]{0, 2}
        );

        var result3 = MutableMatrix.createReal(
            new double[]{1, 0, 0},
            new double[]{0, 2, 0},
            new double[]{0, 0, 3}
        );

        return Stream.of(
            Arguments.of(vec1, RealNumber.support(), result1),
            Arguments.of(vec2, RealNumber.support(), result2),
            Arguments.of(vec3, RealNumber.support(), result3)
        );
    }

    @ParameterizedTest
    @MethodSource("createDiagonalMatrixTestArguments")
    public <T extends AbstractNumber<T>> void createDiagonalMatrixTest(T[] vector, AbstractNumberSupport<T> support, MutableMatrix<T> expectedMatrix) {
        var actualMatrix = MutableMatrix.createDiagonal(support, vector);

        assertEquals(expectedMatrix, actualMatrix);
    }

    public static Stream<Arguments> createIdentityTestArguments() {
        var matrix1 = MutableMatrix.createReal(new double[][]{new double[]{1.0}});
        var matrix2 = MutableMatrix.createReal(
            new double[]{1.0, 0.0},
            new double[]{0.0, 1.0}
        );
        var matrix3 = MutableMatrix.createReal(
            new double[]{1.0, 0.0, 0.0},
            new double[]{0.0, 1.0, 0.0},
            new double[]{0.0, 0.0, 1.0}
        );

        return Stream.of(
            Arguments.of(1, RealNumber.support(), matrix1),
            Arguments.of(2, RealNumber.support(), matrix2),
            Arguments.of(3, RealNumber.support(), matrix3)
        );
    }

    @ParameterizedTest
    @MethodSource("createIdentityTestArguments")
    public <T extends AbstractNumber<T>> void createIdentityTest(int size, AbstractNumberSupport<T> support, MutableMatrix<?> expectedMatrix) {
        var actualMatrix = MutableMatrix.createIdentity(support, size);

        assertEquals(expectedMatrix, actualMatrix);
    }

    @SuppressWarnings({"EqualsWithItself", "SimplifiableAssertion"}) // That's what we are testing
    @Test
    public void equalsTest() {
        var data1 = new double[][]{new double[]{1, 2}, new double[]{3, 4}};
        var data2 = new double[][]{new double[]{0, 2}, new double[]{3, 4}};

        var matrix = MutableMatrix.createReal(data1);
        var matrixWithSameData = MutableMatrix.createReal(data1);
        var matrixWithDifferentData = MutableMatrix.createReal(data2);
        var matrixWithDifferentSize = new MutableMatrix<>(RealNumber.support(), 3, 2);

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

        var matrix = MutableMatrix.createReal(data1);
        var matrixWithSameData = MutableMatrix.createReal(data1);

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
        var matrix = MutableMatrix.createRandomRowMatrix(RealNumber.support(), 3, new FakeRandom());
        var expectedResult = MutableMatrix.createReal(new double[]{0, 1, 2});

        assertEquals(expectedResult, matrix);
    }

    @Test
    public void createRandomColumnMatrixTest() {
        var matrix = MutableMatrix.createRandomColumnMatrix(RealNumber.support(), 3, new FakeRandom());
        var expectedResult = MutableMatrix.createReal(
            new double[]{0},
            new double[]{1},
            new double[]{2}
        );

        assertEquals(expectedResult, matrix);
    }

    @Test
    public void setTest() {
        var matrix = new MutableMatrix<>(RealNumber.support(), 2, 2);

        matrix.set(0, 0, new RealNumber(1));
        matrix.set(0, 1, new RealNumber(2));
        matrix.set(1, 0, new RealNumber(3));
        matrix.set(1, 1, new RealNumber(4));

        assertEquals(new RealNumber(1), matrix.get(0, 0));
        assertEquals(new RealNumber(2), matrix.get(0, 1));
        assertEquals(new RealNumber(3), matrix.get(1, 0));
        assertEquals(new RealNumber(4), matrix.get(1, 1));
    }

    @Test
    public void setThrowsOnInvalidIndicesTest() {
        var matrix = new MutableMatrix<>(RealNumber.support(), 2, 2);
        var number = new RealNumber(1);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.set(-1, 0, number));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.set(0, -1, number));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.set(2, 0, number));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.set(0, 2, number));
    }

    @Test
    public void setRowTest() {
        var matrix = new MutableMatrix<>(RealNumber.support(), 2, 2);
        matrix.setRow(0, new RealNumber[]{new RealNumber(1), new RealNumber(2)});
        matrix.setRow(1, new RealNumber[]{new RealNumber(3), new RealNumber(4)});

        assertEquals(new RealNumber(1), matrix.get(0, 0));
        assertEquals(new RealNumber(2), matrix.get(0, 1));
        assertEquals(new RealNumber(3), matrix.get(1, 0));
        assertEquals(new RealNumber(4), matrix.get(1, 1));
    }

    @Test
    public void setRowThrowsOnInvalidIndexTest() {
        var matrix = new MutableMatrix<>(RealNumber.support(), 2, 2);
        var row = new RealNumber[]{new RealNumber(1)};

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setRow(-1, row));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setRow(2, row));
    }

    @Test
    public void setRowThrowsOnIncompatibleInputTest() {
        var matrix = new MutableMatrix<>(RealNumber.support(), 2, 2);
        var row = new RealNumber[]{new RealNumber(1)};

        assertThrows(IllegalArgumentException.class, () -> matrix.setRow(1, row));
    }

    @Test
    public void setColumnTest() {
        var matrix = new MutableMatrix<>(RealNumber.support(), 2, 2);
        matrix.setColumn(0, new RealNumber[]{new RealNumber(1), new RealNumber(3)});
        matrix.setColumn(1, new RealNumber[]{new RealNumber(2), new RealNumber(4)});

        assertEquals(new RealNumber(1), matrix.get(0, 0));
        assertEquals(new RealNumber(2), matrix.get(0, 1));
        assertEquals(new RealNumber(3), matrix.get(1, 0));
        assertEquals(new RealNumber(4), matrix.get(1, 1));
    }

    @Test
    public void setColumnThrowsOnInvalidIndexTest() {
        var matrix = new MutableMatrix<>(RealNumber.support(), 2, 2);
        var column = new RealNumber[]{new RealNumber(1)};

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setColumn(-1, column));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setColumn(2, column));
    }

    @Test
    public void setColumnThrowsOnIncompatibleInputTest() {
        var matrix = new MutableMatrix<>(RealNumber.support(), 2, 2);
        var column = new RealNumber[]{new RealNumber(1)};

        assertThrows(IllegalArgumentException.class, () -> matrix.setColumn(1, column));
    }

    public static Stream<Arguments> setArrayThrowsOnInvalidDataTestArguments() {
        var data1 = new RealNumber[0][];
        var data2 = new RealNumber[][]{
            new RealNumber[]{new RealNumber(1)},
            new RealNumber[]{new RealNumber(1), new RealNumber(2)}
        };
        var data3 = new RealNumber[][]{
            new RealNumber[]{new RealNumber(1), new RealNumber(2)},
            new RealNumber[]{new RealNumber(1)}
        };

        return Stream.of(
            Arguments.of(RealNumber.support(), data1),
            Arguments.of(RealNumber.support(), data2),
            Arguments.of(RealNumber.support(), data3)
        );
    }

    @ParameterizedTest
    @MethodSource("setArrayThrowsOnInvalidDataTestArguments")
    public <T extends AbstractNumber<T>> void setArrayThrowsOnInvalidDataTest(AbstractNumberSupport<T> support, T[][] data) {
        var matrix = new MutableMatrix<>(support, 2, 2);

        assertThrows(IllegalArgumentException.class, () -> matrix.set(data));
    }
}
