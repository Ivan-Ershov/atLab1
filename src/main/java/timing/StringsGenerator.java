package timing;

import java.util.Random;

public class StringsGenerator {
    private final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ._";
    private final String ALPHABET_WITH_DIGITS = ALPHABET.concat("0123456789");
    private final String WHITESPACES = " \t";

    private final Random random = new Random();

    public String getString(Pattern pattern, int length) {

        if (length <= 0) {
            throw new IllegalArgumentException("!!!length must be greater than zero!!!");
        }

        if (pattern == null) {
            throw new IllegalArgumentException("!!!pattern is not null!!!");
        }

        StringBuilder result = new StringBuilder();
        if (pattern == Pattern.INCORRECT) {
            int count;
            while (length != 0) {
                count = getNumber(length - 1) + 1;

                boolean isWhitespace = (getNumber(2) == 0);
                if (isWhitespace) {
                    addSymbols(result, WHITESPACES, count);
                } else {
                    addSymbols(result, ALPHABET_WITH_DIGITS, count);
                }

                length -= count;

            }

        } else if (pattern == Pattern.JOIN) {
            int count;
            int[] stages = {0, 1, 2, 3, 4, 0, 1, 2, 5};
            int currentStage = 0;
            while (length != 0) {
                switch (stages[currentStage]) {
                    case 0:

                        count = getNumber(length - 1);
                        addSymbols(result, WHITESPACES, count);
                        length -= count;

                        currentStage++;
                        break;

                    case 1:

                        addSymbols(result, ALPHABET, 1);
                        length--;

                        currentStage++;
                        break;

                    case 2:

                        count = getNumber(length + 1);
                        addSymbols(result, ALPHABET_WITH_DIGITS, count);
                        length -= count;

                        currentStage++;
                        break;

                    case 3:

                        count = getNumber(length);
                        addSymbols(result, WHITESPACES, count);
                        length -= count;

                        currentStage++;
                        break;

                    case 4:

                        if (length > 5) {
                            result.append("join");
                            addSymbols(result, WHITESPACES, 1);
                            length -= 5;

                            currentStage++;

                        } else {
                            currentStage = stages.length - 1;
                        }

                        break;

                    case 5:
                        addSymbols(result, WHITESPACES, length);
                        length = 0;
                        break;

                }
            }

        } else {
            int count;
            int[] stages = {0, 1, 0, 2, 3, 0, 4, 0, 2, 3, 0, 5, 6, 7};
            int currentStage;

            if (length < 11) {
                currentStage  = stages.length - 1;
            } else {
                currentStage = 0;
            }

            int shift = 11;

            while (length != 0) {
                switch (stages[currentStage]) {
                    case 0:

                        count = getNumber(length - shift);
                        addSymbols(result, WHITESPACES, count);
                        length -= count;

                        currentStage++;
                        break;

                    case 1:

                        result.append("create");
                        addSymbols(result, WHITESPACES, 1);
                        length -= 7;
                        shift -= 7;

                        currentStage++;

                        break;

                    case 2:

                        addSymbols(result, ALPHABET, 1);
                        length--;
                        shift--;

                        currentStage++;
                        break;

                    case 3:

                        count = getNumber(length - shift);
                        addSymbols(result, ALPHABET_WITH_DIGITS, count);
                        length -= count;

                        currentStage++;
                        break;

                    case 4:

                        result.append("(");
                        length -= 1;
                        shift -= 1;

                        currentStage++;

                        break;

                    case 5:

                        if ((length - shift) < 2) {
                            currentStage++;
                        } else {

                            shift += 2;

                            result.append(",");
                            length -= 1;
                            shift -= 1;

                            currentStage -= 4;

                        }

                        break;

                    case 6:

                        result.append(")");
                        length -= 1;
                        shift -= 1;

                        currentStage++;

                        break;

                    case 7:
                        addSymbols(result, WHITESPACES, length);
                        length = 0;
                        break;

                }
            }

        }

        return result.toString();
    }

    private int getNumber(int bound) {

        if (bound <= 0) {
            return 0;
        }

        return random.nextInt(bound);
    }

    private char getSymbol(String alphabet) {
        int index = random.nextInt(alphabet.length());
        return alphabet.charAt(index);
    }

    private void addSymbols(StringBuilder builder, String alphabet, int count) {
        for (int iterator = 0; iterator < count; iterator++) {
            builder.append(getSymbol(alphabet));
        }
    }

    public enum Pattern {
        CREATE,
        JOIN,
        INCORRECT
    }

}
