package lab2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class MatrixOperationsTests {
    public static Stream<Arguments> setRowsTestArguments() {
        var rows1 = new double[][] {
            new double[] { 1, 2, 3 },
            new double[] { 4, 5, 6 },
            new double[] { 7, 8, 9 },
        };

        var rows2 = new double[][] {
            new double[] { 1 }
        };

        var rows3 = new double[][] {
            new double[] { 1, 2 },
            new double[] { 4, 5 },
            new double[] { 7, 8 },
        };

        var result1 = new double[] {
            1, 2, 3,
            4, 5, 6,
            7, 8, 9
        };
        var result2 = new double[] { 1 };
        var result3 = new double[] {
            1, 2,
            4, 5,
            7, 8,
        };

        var dimen1 = new MatrixDimension(3);
        var dimen2 = new MatrixDimension(1);
        var dimen3 = new MatrixDimension(2, 3);

        return Stream.of(
            Arguments.of(rows1, result1, dimen1),
            Arguments.of(rows2, result2, dimen2),
            Arguments.of(rows3, result3, dimen3)
        );
    }

    @ParameterizedTest
    @MethodSource("setRowsTestArguments")
    public void setRowsTest(double[][] rows, double[] expectedResult, MatrixDimension dimen) {
        double[] actualResult = new double[dimen.getColumnCount() * dimen.getRowCount()];
        MatrixOperations.setRows(actualResult, rows, dimen);

        assertArrayEquals(expectedResult, actualResult);
    }
}
