package lab2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class MatrixOperationsTests {
    public static Stream<Arguments> getRowTestArguments() {
        double[] matrix1 = new double[]{
            0, 1, 2,
            3, 4, 5,
            6, 8, 9
        };

        double[] matrix2 = new double[]{1};
        double[] matrix3 = new double[]{
            0, 1,
            2, 3,
            4, 5
        };

        var dimen1 = new MatrixDimension(3, 3);
        var dimen2 = new MatrixDimension(1, 1);
        var dimen3 = new MatrixDimension(2, 3);

        return Stream.of(
            Arguments.of(matrix1, 0, dimen1, new double[]{0, 1, 2}),
            Arguments.of(matrix1, 2, dimen1, new double[]{6, 8, 9}),
            Arguments.of(matrix2, 0, dimen2, matrix2),
            Arguments.of(matrix3, 1, dimen3, new double[]{2, 3})
        );
    }

    @ParameterizedTest
    @MethodSource("getRowTestArguments")
    public void getRowTest(double[] matrix, int index, MatrixDimension dimen, double[] expectedRow) {
        double[] actualRow = MatrixOperations.getRow(matrix, index, dimen);

        assertArrayEquals(expectedRow, actualRow);
    }

    public static Stream<Arguments> getColumnTestArguments() {
        double[] matrix1 = new double[]{
            0, 1, 2,
            3, 4, 5,
            6, 8, 9
        };

        double[] matrix2 = new double[]{1};
        double[] matrix3 = new double[]{
            0, 1,
            2, 3,
            4, 5
        };

        var dimen1 = new MatrixDimension(3, 3);
        var dimen2 = new MatrixDimension(1, 1);
        var dimen3 = new MatrixDimension(2, 3);

        return Stream.of(
            Arguments.of(matrix1, 0, dimen1, new double[]{0, 3, 6}),
            Arguments.of(matrix1, 2, dimen1, new double[]{2, 5, 9}),
            Arguments.of(matrix2, 0, dimen2, matrix2),
            Arguments.of(matrix3, 1, dimen3, new double[]{1, 3, 5})
        );
    }

    @ParameterizedTest
    @MethodSource("getColumnTestArguments")
    public void getColumnTest(double[] matrix, int index, MatrixDimension dimen, double[] expectedColumn) {
        double[] actualColumn = MatrixOperations.getColumn(matrix, index, dimen);

        assertArrayEquals(expectedColumn, actualColumn);
    }

    public static Stream<Arguments> addTestArguments() {
        double[] matrix1 = new double[]{
            0, 1, 2,
            3, 4, 5,
            6, 8, 9
        };


        double[] matrix2 = new double[]{
            2, 3, 4,
            5, 6, 7,
            8, 9, 10
        };

        double[] matrix3 = new double[]{
            2, 4, 6,
            8, 10, 12,
            14, 17, 19
        };

        return Stream.of(Arguments.of(matrix1, matrix2, matrix3));
    }

    @ParameterizedTest
    @MethodSource("addTestArguments")
    public void addTest(double[] matrix1, double[] matrix2, double[] expectedResult) {
        double[] actualResult = MatrixOperations.add(matrix1, matrix2);

        assertArrayEquals(expectedResult, actualResult);
    }

    public static Stream<Arguments> multiplyScalarTestArguments() {
        double[] matrix1 = new double[]{
            0, 1, 2,
            3, 4, 5,
            6, 8, 9
        };

        double[] matrix2 = new double[]{
            0, 2, 4,
            6, 8, 10,
            12, 16, 18
        };

        return Stream.of(Arguments.of(matrix1, 2, matrix2));
    }

    @ParameterizedTest
    @MethodSource("multiplyScalarTestArguments")
    public void multiplyScalarTest(double[] matrix1, double scalar, double[] expectedResult) {
        double[] actualResult = MatrixOperations.multiplyByScalar(matrix1, scalar);

        assertArrayEquals(expectedResult, actualResult);
    }

    public static Stream<Arguments> multiplyMatricesTestArguments() {
        var dimen2x2 = new MatrixDimension(2, 2);
        var dimen3x3 = new MatrixDimension(3, 3);
        var dimen3x2 = new MatrixDimension(2, 3);
        var dimen2x3 = new MatrixDimension(3, 2);

        double[] matrix1 = new double[]{
            0, 1, 2,
            3, 4, 5,
            6, 8, 9
        };

        double[] matrix2 = new double[]{
            0, 2, 4,
            6, 8, 10,
            12, 16, 18
        };

        double[] matrix_1_2_result = new double[] {
            30, 40, 46,
            84, 118, 142,
            156, 220, 266
        };

        double[] matrix3 = new double[] {
            1, 2,
            3, 4
        };

        double[] matrix4 = new double[] {
            10, 20,
            30, 40
        };

        double[] matrix_3_4_result = new double[] {
            70, 100,
            150, 220
        };

        double[] matrix5 = new double[] {
            1, 2,
            3, 4,
            5, 6
        };

        double[] matrix6 = new double[] {
            1, 2, 3,
            4, 5, 6
        };

        double[] matrix_5_6_result = new double[] {
            9, 12, 15,
            19, 26, 33,
            29, 40, 51
        };

        return Stream.of(
            Arguments.of(matrix1, matrix2, dimen3x3, dimen3x3, matrix_1_2_result),
            Arguments.of(matrix3, matrix4, dimen2x2, dimen2x2, matrix_3_4_result),
            Arguments.of(matrix5, matrix6, dimen3x2, dimen2x3, matrix_5_6_result)
        );
    }

    @ParameterizedTest
    @MethodSource("multiplyMatricesTestArguments")
    public void multiplyMatricesTest(double[] matrix1, double[] matrix2, MatrixDimension dimen1, MatrixDimension dimen2, double[] expectedResult) {
        double[] actualResult = MatrixOperations.multiplyMatrices(matrix1, matrix2, dimen1, dimen2);

        assertArrayEquals(expectedResult, actualResult);
    }

    public static Stream<Arguments> createDiagonalMatrixTestArguments() {
        double[] vec1 = new double[] { 1 };
        double[] vec2 = new double[] { 1, 2 };
        double[] vec3 = new double[] { 1, 2, 3 };

        double[] result1 = new double[] { 1 };
        double[] result2 = new double[] {
            1, 0,
            0, 2
        };

        double[] result3 = new double[] {
            1, 0, 0,
            0, 2, 0,
            0, 0, 3,
        };

        return Stream.of(
            Arguments.of(vec1, result1),
            Arguments.of(vec2, result2),
            Arguments.of(vec3, result3)
        );
    }

    @ParameterizedTest
    @MethodSource("createDiagonalMatrixTestArguments")
    public void createDiagonalMatrixTest(double[] vector, double[] expectedMatrix) {
        double[] actualMatrix = MatrixOperations.createDiagonalMatrix(vector);

        assertArrayEquals(expectedMatrix, actualMatrix);
    }
}
