package lab2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class MatrixTests {
    @Test
    public void setTest() {
        var matrix = new MutableMatrix(2, 2);

        matrix.set(0, 0, 1);
        matrix.set(0, 1, 2);
        matrix.set(1, 0, 3);
        matrix.set(1, 1, 4);

        assertEquals(1, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(3, matrix.get(1, 0));
        assertEquals(4, matrix.get(1, 1));
    }

    @Test
    public void setThrowsOnInvalidIndicesTest() {
        var matrix = new MutableMatrix(2, 2);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.set(-1, 0, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.set(0, -1, 20));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.set(2, 0, 30));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.set(0, 2, 40));
    }

    @Test
    public void setRowTest() {
        var matrix = new MutableMatrix(2, 2);
        matrix.setRow(0, new double[]{1, 2});
        matrix.setRow(1, new double[]{3, 4});

        assertEquals(1, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(3, matrix.get(1, 0));
        assertEquals(4, matrix.get(1, 1));
    }

    @Test
    public void setRowThrowsOnInvalidIndexTest() {
        var matrix = new MutableMatrix(2, 2);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setRow(-1, new double[]{1, 2}));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setRow(2, new double[]{1, 2}));
    }

    @Test
    public void setRowThrowsOnIncompatibleInputTest() {
        var matrix = new MutableMatrix(2, 2);
        assertThrows(IllegalArgumentException.class, () -> matrix.setRow(1, new double[]{1}));
    }

    @Test
    public void setColumnTest() {
        var matrix = new MutableMatrix(2, 2);
        matrix.setColumn(0, new double[]{1, 3});
        matrix.setColumn(1, new double[]{2, 4});

        assertEquals(1, matrix.get(0, 0));
        assertEquals(2, matrix.get(0, 1));
        assertEquals(3, matrix.get(1, 0));
        assertEquals(4, matrix.get(1, 1));
    }

    @Test
    public void setColumnThrowsOnInvalidIndexTest() {
        var matrix = new MutableMatrix(2, 2);

        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setColumn(-1, new double[]{1, 2}));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.setColumn(2, new double[]{1, 2}));
    }

    @Test
    public void setColumnThrowsOnIncompatibleInputTest() {
        var matrix = new MutableMatrix(2, 2);
        assertThrows(IllegalArgumentException.class, () -> matrix.setColumn(1, new double[]{1}));
    }

    @Test
    public void setArrayThrowsOnInvalidDataTest() {
        var matrix = new MutableMatrix(2, 2);

        assertThrows(IllegalArgumentException.class, () -> matrix.set(new double[0][]));
        assertThrows(IllegalArgumentException.class, () -> matrix.set(new double[][]{new double[]{1}, new double[]{1, 2}}));
        assertThrows(IllegalArgumentException.class, () -> matrix.set(new double[][]{new double[]{1, 2}, new double[]{1}}));
    }
}
