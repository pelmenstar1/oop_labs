package lab1;

public class StringCalculator {
    public int add(String numbers) {
        if (numbers.isEmpty()) {
            return 0;
        }

        int sum = 0;

        // Set the limit to -1 to include empty strings.
        for (String numberStr : numbers.split("[,\n]", -1)) {
            if (numberStr.isEmpty()) {
                throwInvalidFormat();
            }

            try {
                sum += Integer.parseInt(numberStr);
            } catch (Exception e) {
                throwInvalidFormat();
            }
        }

        return sum;
    }

    private static void throwInvalidFormat() {
        throw new IllegalArgumentException("Text has invalid format");
    }
}
