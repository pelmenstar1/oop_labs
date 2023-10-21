package lab2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.BiFunction;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexNumberTests {
    private void binaryOpTestHelper(ComplexNumber a, ComplexNumber b, ComplexNumber expectedResult, BiFunction<ComplexNumber, ComplexNumber, ComplexNumber> op) {
        ComplexNumber actualResult = op.apply(a, b);

        assertEquals(expectedResult, actualResult);
    }

    public static Stream<Arguments> addTestArguments() {
        return Stream.of(
            Arguments.of(new ComplexNumber(1, 0), new ComplexNumber(2, 3), new ComplexNumber(3, 3)),
            Arguments.of(new ComplexNumber(2, 3), new ComplexNumber(2, 3), new ComplexNumber(4, 6))
        );
    }

    @ParameterizedTest
    @MethodSource("addTestArguments")
    public void addTest(ComplexNumber a, ComplexNumber b, ComplexNumber expectedResult) {
        binaryOpTestHelper(a, b, expectedResult, (x, y) -> x.plus(b));
    }

    public static Stream<Arguments> multiplyTestArguments() {
        return Stream.of(
            Arguments.of(new ComplexNumber(1, 2), new ComplexNumber(3, 4), new ComplexNumber(-5, 10)),
            Arguments.of(new ComplexNumber(1, -3), new ComplexNumber(1, 2), new ComplexNumber(7, -1))
        );
    }

    @ParameterizedTest
    @MethodSource("multiplyTestArguments")
    public void multiplyTest(ComplexNumber a, ComplexNumber b, ComplexNumber expectedResult) {
        binaryOpTestHelper(a, b, expectedResult, (x, y) -> x.multiply(b));
    }

    @Test
    public void negatedTest() {
        var number = new ComplexNumber(1, -3);
        var expectedResult = new ComplexNumber(-1, 3);

        var actualResult = number.negated();

        assertEquals(expectedResult, actualResult);
    }

    public static Stream<Arguments> inverseTestArguments() {
        return Stream.of(
            Arguments.of(new ComplexNumber(1, -3), new ComplexNumber(0.1, 0.3)),
            Arguments.of(new ComplexNumber(2, 5), new ComplexNumber(2.0 / 29, -5.0 / 29)),
            Arguments.of(new ComplexNumber(4), new ComplexNumber(0.25)),
            Arguments.of(new ComplexNumber(0), new ComplexNumber(Double.NaN, Double.NaN))
        );
    }

    @ParameterizedTest
    @MethodSource("inverseTestArguments")
    public void inverseTest(ComplexNumber number, ComplexNumber expectedResult) {
        var actualResult = number.inverse();

        assertEquals(expectedResult, actualResult);
    }

    @SuppressWarnings({"SimplifiableAssertion", "ConstantValue"})
    @Test
    public void equalsTest() {
        var a = new ComplexNumber(5, 5);
        var b = new ComplexNumber(5, 5);
        var c = new ComplexNumber(4, 5);

        assertFalse(a.equals(null));
        assertFalse(a.equals(new Object()));
        assertTrue(a.equals(b));
        assertFalse(a.equals(c));
    }

    public static Stream<Arguments> equalsSpecialValuesTestArguments() {
        return Stream.of(
            Arguments.of(new ComplexNumber(Double.NaN, Double.NaN), new ComplexNumber(Double.NaN, Double.NaN)),
            Arguments.of(new ComplexNumber(+0.0, -0.0), new ComplexNumber(-0.0, +0.0))
        );
    }

    @ParameterizedTest
    @MethodSource("equalsSpecialValuesTestArguments")
    public void equalsSpecialValuesTest(ComplexNumber a, ComplexNumber b) {
        assertEquals(a, b);
    }

    public static Stream<Arguments> hashCodeSameTestArguments() {
        return Stream.of(
            Arguments.of(new ComplexNumber(5, 5), new ComplexNumber(5, 5)),
            Arguments.of(new ComplexNumber(Double.NaN, Double.NaN), new ComplexNumber(Double.NaN, Double.NaN)),
            Arguments.of(new ComplexNumber(-0.0, +0.0), new ComplexNumber(+0.0, -0.0))
        );
    }

    @ParameterizedTest
    @MethodSource("hashCodeSameTestArguments")
    public void hashCodeSameTest(ComplexNumber a, ComplexNumber b) {
        int hash1 = a.hashCode();
        int hash2 = b.hashCode();

        assertEquals(hash1, hash2);
    }

    @Test
    public void toStringTest() {
        var number = new ComplexNumber(3, 4);
        var expectedResult = "(3.0;4.0)";
        String actualResult = number.toString();

        assertEquals(expectedResult, actualResult);
    }
}
