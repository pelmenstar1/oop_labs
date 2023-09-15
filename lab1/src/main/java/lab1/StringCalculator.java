package lab1;

import java.util.ArrayList;
import java.util.List;

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

        var negativeNumbers = new ArrayList<Integer>();
        int sum = 0;

        // Set the limit to -1 to include empty strings.
        for (String numberStr : numbers.substring(numberListStart).split(splitPattern, -1)) {
            if (numberStr.isEmpty()) {
                throwInvalidFormat();
            }

            try {
                int number = Integer.parseInt(numberStr);
                if (number < 0) {
                    negativeNumbers.add(number);
                    continue;
                }

                sum += number;
            } catch (Exception e) {
                throwInvalidFormat();
            }
        }

        if (!negativeNumbers.isEmpty()) {
            throwOnNegativeNumbers(negativeNumbers);
        }

        return sum;
    }

    private static void throwOnNegativeNumbers(List<Integer> negativeNumbers) {
        var builder = new StringBuilder();
        builder.append("Negative numbers are not allowed. In the text, there ");

        if (negativeNumbers.size() == 1) {
            builder.append("was such number: ");
            builder.append(negativeNumbers.get(0));
        } else {
            builder.append("were such numbers: ");

            for (int i = 0; i < negativeNumbers.size(); i++) {
                builder.append(negativeNumbers.get(i));

                if (i < negativeNumbers.size() - 1) {
                    builder.append(", ");
                }
            }
        }

        throw new IllegalArgumentException(builder.toString());
    }

    private static void throwInvalidFormat() {
        throw new IllegalArgumentException("Text has invalid format");
    }
}
