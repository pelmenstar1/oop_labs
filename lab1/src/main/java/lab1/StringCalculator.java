package lab1;

public class StringCalculator {
    public int add(String numbers) {
        if (numbers.isEmpty()) {
            return 0;
        }

        String splitPattern = "[,\n]";
        int numberListStart = 0;

        if (numbers.startsWith("//")) {
            if (numbers.length() <= 3 || numbers.charAt(3) != '\n') {
                throwInvalidFormat();
            }

            char optSplitChar = numbers.charAt(2);

            splitPattern = "[,\n" + optSplitChar + "]";
            numberListStart = 4;
        }

        int sum = 0;

        // Set the limit to -1 to include empty strings.
        for (String numberStr : numbers.substring(numberListStart).split(splitPattern, -1)) {
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
