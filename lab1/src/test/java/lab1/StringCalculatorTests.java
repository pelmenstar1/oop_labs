package lab1;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringCalculatorTests {
    public static Stream<Arguments> addTestArguments() {
        return Stream.of(
                Arguments.of("", 0),
                Arguments.of("1", 1),
                Arguments.of("1,2", 3)
        );
    }

    @ParameterizedTest
    @MethodSource("addTestArguments")
    public void addTest(String input, int expectedSum) {
        assertEquals(expectedSum, new StringCalculator().add(input));
    }
}
