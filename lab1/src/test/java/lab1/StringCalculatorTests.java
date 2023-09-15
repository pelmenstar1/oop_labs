package lab1;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StringCalculatorTests {
    public static Stream<Arguments> addTestArguments() {
        return Stream.of(
                Arguments.of("", 0),
                Arguments.of("1", 1),
                Arguments.of("1,2", 3),
                Arguments.of("1,2,3", 6),
                Arguments.of("10,20,30", 60),
                Arguments.of("1\n2,3", 6),
                Arguments.of("//;\n1;2", 3),
                Arguments.of("//;\n1;2,3", 6),
                Arguments.of("//,\n1,2", 3)
        );
    }

    @ParameterizedTest
    @MethodSource("addTestArguments")
    public void addTest(String input, int expectedSum) {
        assertEquals(expectedSum, new StringCalculator().add(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1,\n",
            ",",
            "\n",
            "1,2,3\n",
            "1,2,3,",
            "1,\n2",
            "//",
            "//\n",
            "//\n1,2",
            "//;\n1*2"
    })
    public void addDoesNotPassValidationOnInvalidInput(String input) {
        assertThrows(IllegalArgumentException.class, () -> new StringCalculator().add(input));
    }
}