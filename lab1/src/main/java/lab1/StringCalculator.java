package lab1;

import java.util.Arrays;

public class StringCalculator {
    public int add(String numbers) {
        if (numbers.isEmpty()) {
            return 0;
        }

        try {
            return Arrays.stream(numbers.split(",")).map(Integer::parseInt).reduce(0, Integer::sum);
        } catch (Exception e) {
            throwInvalidFormat();
            throw new RuntimeException();
        }
    }

    private static void throwInvalidFormat() {
        throw new IllegalArgumentException("Text has invalid format");
    }
}
