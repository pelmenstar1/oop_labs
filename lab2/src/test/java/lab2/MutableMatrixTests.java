package lab2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class MutableMatrixTests {
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
