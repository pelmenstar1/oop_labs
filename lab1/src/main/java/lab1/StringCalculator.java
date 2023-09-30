package lab1;

import java.util.ArrayList;
import java.util.Collections;
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

        int offset = startIndex;

        while (true) {
            if (text.charAt(offset) != '[') {
                throwInvalidFormat();
            }

            int endBracketIndex = text.indexOf(']', offset + 1);
            if (endBracketIndex < 0) {
                // There must be ] after [
                throwInvalidFormat();
            }

            int possibleNlIndex = endBracketIndex + 1;

            if (possibleNlIndex == text.length()) {
                throwInvalidFormat();
            }

            if (text.charAt(possibleNlIndex) == '\n') {
                return possibleNlIndex;
            }

            offset = possibleNlIndex;
        }
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

        var delimiters = new ArrayList<String>();

        int offset = 0;

        while (offset < text.length()) {
            if (text.charAt(offset) != '[') {
                // The text must start with [
                throwInvalidFormat();
            }

            int endBracketIndex = text.indexOf(']', offset + 1);
            if (endBracketIndex < 0) {
                // There must be ] after [
                throwInvalidFormat();
            }

            String delimiter = text.substring(offset + 1, endBracketIndex);

            if (delimiter.isEmpty()) {
                // The delimiter can't be empty.
                throwInvalidFormat();
            }

            delimiters.add(delimiter);

            offset = endBracketIndex + 1;
        }

        delimiters.sort(Collections.reverseOrder(String::compareTo));

        var patternBuilder = new StringBuilder();

        for (String delimiter : delimiters) {
            // Add \Q to the begging and \E to the end to mark that the string between them should
            // be matched literally.
            patternBuilder.append("\\Q");
            patternBuilder.append(delimiter);
            patternBuilder.append("\\E|");
        }

        patternBuilder.append(",|\n");

        return patternBuilder.toString();
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
