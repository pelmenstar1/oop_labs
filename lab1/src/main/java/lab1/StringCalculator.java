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
            int delListEnd = findDelimiterListEnd(numbers, 2);

            splitPattern = parseDelimiterList(numbers.substring(2, delListEnd));
            numberListStart = delListEnd + 1;
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
                } else if (number <= 1000) {
                    sum += number;
                }
            } catch (Exception e) {
                throwInvalidFormat();
            }
        }

        if (!negativeNumbers.isEmpty()) {
            throwOnNegativeNumbers(negativeNumbers);
        }

        return sum;
    }

    private static int findDelimiterListEnd(String text, int startIndex) {
        if (text.length() - startIndex <= 2) {
            throwInvalidFormat();
        }

        char firstChar = text.charAt(startIndex);

        if (firstChar != '[') {
            char secondChar = text.charAt(startIndex + 1);
            if (secondChar == '\n') {
                return startIndex + 1;
            }
        }

        if (firstChar != '[') {
            throwInvalidFormat();
        }

        int endBracketIndex = text.indexOf(']', 1);
        if (endBracketIndex < 0) {
            // There must be ] after [
            throwInvalidFormat();
        }

        int nlIndex = endBracketIndex + 1;

        if (nlIndex == text.length()) {
            throwInvalidFormat();
        }

        if (text.charAt(nlIndex) == '\n') {
            return nlIndex;
        }

        throwInvalidFormat();
        throw new RuntimeException();
    }

    /**
     * Returns the pattern for {@link String#split(String)} by parsing given text.
     */
    private static String parseDelimiterList(String text) {
        if (text.length() == 1) {
            // If the text is one-char, the pattern will consist of that char and default delimiters.
            char optSplitChar = text.charAt(0);

            return "[,\n" + optSplitChar + "]";
        }

        if (text.charAt(0) != '[') {
            // The text must start with [
            throwInvalidFormat();
        }

        int endBracketIndex = text.indexOf(']', 1);
        if (endBracketIndex < 0) {
            // There must be ] after [
            throwInvalidFormat();
        }

        String delimiter = text.substring(1, endBracketIndex);

        if (delimiter.isEmpty()) {
            // The delimiter can't be empty.
            throwInvalidFormat();
        }

        // Add \Q to the begging and \E to the end to mark that the string between them should
        // be matched literally.
        return "\\Q" + delimiter + "\\E|,|\n";
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
