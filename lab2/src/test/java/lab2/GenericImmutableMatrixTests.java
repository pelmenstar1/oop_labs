package lab2;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GenericImmutableMatrixTests {
    public static Stream<Arguments> inverseTestArguments() {
        var realInputMat1x1 = GenericImmutableMatrix.createReal(new double[][]{
            new double[]{2}
        });

        var realInputMat2x2 = GenericImmutableMatrix.createReal(new double[][]{
            new double[]{-1, 1.5,},
            new double[]{1, -1},
        });

        var realInputMat3x3 = GenericImmutableMatrix.createReal(new double[][]{
            new double[]{1, 2, 3},
            new double[]{0, 1, 4},
            new double[]{5, 6, 0},
        });

        var realInputMat4x4 = GenericImmutableMatrix.createReal(new double[][]{
            new double[]{1, 2, 3, 4},
            new double[]{6, 7, 8, 9},
            new double[]{20, 20, 21, 22},
            new double[]{19, 7, 8, 1}
        });

        var realOutputMat1x1 = GenericImmutableMatrix.createReal(new double[][]{
            new double[]{0.5}
        });

        var realOutputMat2x2 = GenericImmutableMatrix.createReal(new double[][]{
            new double[]{2, 3},
            new double[]{2, 2},
        });

        var realOutputMat3x3 = GenericImmutableMatrix.createReal(new double[][]{
            new double[]{-24, 18, 5},
            new double[]{20, -15, -4},
            new double[]{-5, 4, 1},
        });

        var realOutputMat4x4 = GenericImmutableMatrix.createReal(new double[][]{
            new double[]{2.6, -3.6, 1, 0},
            new double[]{-2.575, 2.075, -0.375, -0.125},
            new double[]{-4.45, 7.45, -2.25, 0.25},
            new double[]{4.225, -5.725, 1.625, -0.125}
        });

        var complexInput2x2 = new GenericImmutableMatrix<>(ComplexNumber.support(), new ComplexNumber[][]{
            new ComplexNumber[]{new ComplexNumber(1, -1), new ComplexNumber(2)},
            new ComplexNumber[]{new ComplexNumber(0), new ComplexNumber(2, 3)}
        });

        var complexInput3x3 = new GenericImmutableMatrix<>(ComplexNumber.support(), new ComplexNumber[][]{
            new ComplexNumber[]{new ComplexNumber(1, -1), new ComplexNumber(2), new ComplexNumber(3)},
            new ComplexNumber[]{new ComplexNumber(0), new ComplexNumber(2, 3), new ComplexNumber(4)},
            new ComplexNumber[]{new ComplexNumber(1, 1), new ComplexNumber(0), new ComplexNumber(1)}
        });

        var complexOutput2x2 = new GenericImmutableMatrix<>(ComplexNumber.support(), new ComplexNumber[][]{
            new ComplexNumber[]{new ComplexNumber(0.5, 0.5), new ComplexNumber(-5.0 / 13, 1.0 / 13)},
            new ComplexNumber[]{new ComplexNumber(0), new ComplexNumber(2.0 / 13, -3.0 / 13)}
        });

        var complexOutput3x3 = new GenericImmutableMatrix<>(ComplexNumber.support(), new ComplexNumber[][]{
            new ComplexNumber[]{new ComplexNumber(7.0 / 146, 15.0 / 73), new ComplexNumber(-8.0 / 73, -3.0 / 73), new ComplexNumber(43.0 / 146, -33.0 / 73)},
            new ComplexNumber[]{new ComplexNumber(10.0 / 73, 22.0 / 73), new ComplexNumber(-2.0 / 73, -19.0 / 73), new ComplexNumber(-22.0 / 73, 10.0 / 73)},
            new ComplexNumber[]{new ComplexNumber(23.0 / 146, -37.0 / 146), new ComplexNumber(5.0 / 73, 11.0 / 73), new ComplexNumber(37.0 / 146, 23.0 / 146)}
        });

        return Stream.of(
            Arguments.of(realInputMat1x1, realOutputMat1x1),
            Arguments.of(realInputMat2x2, realOutputMat2x2),
            Arguments.of(realInputMat3x3, realOutputMat3x3),
            Arguments.of(realInputMat4x4, realOutputMat4x4),
            Arguments.of(complexInput2x2, complexOutput2x2),
            Arguments.of(complexInput3x3, complexOutput3x3)
        );
    }

    @ParameterizedTest
    @MethodSource("inverseTestArguments")
    public <T extends AbstractNumber<T>> void inverseTest(GenericImmutableMatrix<T> input, GenericImmutableMatrix<T> expectedResult) {
        var actualResult = input.inverse();

        assertTrue(
            expectedResult.equalsApproximate(actualResult, input.getSupport().getEpsilon()),
            "actual: " + actualResult + "\n expected: " + expectedResult
        );
    }
}
