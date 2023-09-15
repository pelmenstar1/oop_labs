package lab1;

public class StringCalculator {
    public int add(String numbers) {
        if (numbers.isEmpty()) {
            return 0;
        }

        try {
            int commaIndex = numbers.indexOf(',');
            if (commaIndex < 0) {
                // If comma is not in the 'numbers', then the whole 'numbers' is a one number.
                return Integer.parseInt(numbers);
            } else {
                int a = Integer.parseInt(numbers.substring(0, commaIndex));
                int b = Integer.parseInt(numbers.substring(commaIndex + 1));

                return a + b;
            }
        } catch (Exception e) {
            throwInvalidFormat();
            throw new RuntimeException();
        }
    }

    private static void throwInvalidFormat() {
        throw new IllegalArgumentException("Text has invalid format");
    }
}
