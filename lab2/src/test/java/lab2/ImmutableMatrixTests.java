package lab2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class ImmutableMatrixTests {
    @Test
    public void settersThrowTest() {
        var matrix = new ImmutableMatrix<>(RealNumber.support());

        assertThrows(UnsupportedOperationException.class, () -> matrix.set(new RealNumber[0][]));
        assertThrows(UnsupportedOperationException.class, () -> matrix.set(new MutableMatrix<>(RealNumber.support())));
        assertThrows(UnsupportedOperationException.class, () -> matrix.set(0, 0, new RealNumber(0)));
        assertThrows(UnsupportedOperationException.class, () -> matrix.setColumn(0, new RealNumber[0]));
        assertThrows(UnsupportedOperationException.class, () -> matrix.setRow(0, new RealNumber[0]));
    }
}
