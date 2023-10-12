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
}
