package lab2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class MatrixDimensionTests {
    public static Stream<Arguments> sizeConstructorTestArguments() {
        return Stream.of(
            Arguments.of(1, 2),
            Arguments.of(2, 1),
            Arguments.of(0, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("sizeConstructorTestArguments")
    public void sizeConstructorTest(int columnCount, int rowCount) {
        var dimen = new MatrixDimension(columnCount, rowCount);

        assertEquals(columnCount, dimen.getColumnCount());
        assertEquals(rowCount, dimen.getRowCount());
    }

    public static Stream<Arguments> sizeConstructorThrowsTestArguments() {
        return Stream.of(
            Arguments.of(0, 1),
            Arguments.of(1, 0),
            Arguments.of(-1, -2)
        );
    }

    @ParameterizedTest
    @MethodSource("sizeConstructorThrowsTestArguments")
    public void sizeConstructorThrowsTest(int columnCount, int rowCount) {
        assertThrows(IllegalArgumentException.class, () -> new MatrixDimension(columnCount, rowCount));
    }

    public static Stream<Arguments> interchangedTestArguments() {
        return Stream.of(
            Arguments.of(new MatrixDimension(0, 0), new MatrixDimension(0, 0)),
            Arguments.of(new MatrixDimension(1, 2), new MatrixDimension(2, 1))
        );
    }

    @ParameterizedTest
    @MethodSource("interchangedTestArguments")
    public void interchangedTest(MatrixDimension dimen, MatrixDimension expected) {
        MatrixDimension actual = dimen.interchanged();

        assertEquals(expected, actual);
    }

    // that's what we're testing
    @SuppressWarnings({"SimplifiableAssertion", "EqualsWithItself", "ConstantValue"})
    @Test
    public void equalsTest() {
        var instance1 = new MatrixDimension(1, 2);
        var instance2 = new MatrixDimension(1, 2);
        var instance3 = new MatrixDimension(2, 1);

        assertTrue(instance1.equals(instance1)); // should be equal to itself
        assertTrue(instance1.equals(instance2)); // should be equal to the instance with same data
        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(new Object()));
    }

    @Test
    public void hashCodeTest() {
        var instance1 = new MatrixDimension(1, 2);
        var instance2 = new MatrixDimension(1, 2);

        // There might be collisions, so we can't actually except that instances with different data have different hashes.
        // But, at least, instances with same data must have same hashes.
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();

        assertEquals(hash1, hash2);
    }

    @Test
    public void toStringTest() {
        var instance = new MatrixDimension(2, 3);

        String actualStr = instance.toString();
        String expectedStr = "MatrixDimension { columnCount=2, rowCount=3 }";

        assertEquals(expectedStr, actualStr);
    }
}
