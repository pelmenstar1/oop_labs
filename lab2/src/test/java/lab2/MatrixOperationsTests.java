package lab2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class MatrixOperationsTests {
    public static Stream<Arguments> setRowsTestArguments() {
        var rows1 = new RealNumber[][] {
            new RealNumber[] { new RealNumber( 1), new RealNumber(2), new RealNumber(3) },
            new RealNumber[] { new RealNumber( 4), new RealNumber(5), new RealNumber(6) },
            new RealNumber[] { new RealNumber( 7), new RealNumber(8), new RealNumber(9) },
        };

        var rows2 = new RealNumber[][] {
            new RealNumber[] { new RealNumber( 1) }
        };

        var rows3 = new RealNumber[][] {
            new RealNumber[] { new RealNumber( 1), new RealNumber(2) },
            new RealNumber[] { new RealNumber( 4), new RealNumber(5) },
            new RealNumber[] { new RealNumber( 7), new RealNumber(8) },
        };

        var result1 = new RealNumber[] {
            new RealNumber(1), new RealNumber(2), new RealNumber(3),
            new RealNumber(4), new RealNumber(5), new RealNumber(6),
            new RealNumber(7), new RealNumber(8), new RealNumber(9)
        };
        var result2 = new RealNumber[] { new RealNumber(1) };
        var result3 = new RealNumber[] {
            new RealNumber(1), new RealNumber(2),
            new RealNumber(4), new RealNumber(5),
            new RealNumber(7), new RealNumber(8),
        };

        var dimen1 = new MatrixDimension(3);
        var dimen2 = new MatrixDimension(1);
        var dimen3 = new MatrixDimension(3, 2);

        return Stream.of(
            Arguments.of(rows1, result1, dimen1),
            Arguments.of(rows2, result2, dimen2),
            Arguments.of(rows3, result3, dimen3)
        );
    }

    @ParameterizedTest
    @MethodSource("setRowsTestArguments")
    public void setRowsTest(RealNumber[][] rows, RealNumber[] expectedResult, MatrixDimension dimen) {
        var actualResult = new RealNumber[dimen.getColumnCount() * dimen.getRowCount()];
        MatrixOperations.setRows(actualResult, rows, dimen);

        assertArrayEquals(expectedResult, actualResult);
    }
}
