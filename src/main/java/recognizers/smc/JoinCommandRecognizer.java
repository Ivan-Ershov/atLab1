package recognizers.smc;

import java.util.LinkedList;
import java.util.List;

public class JoinCommandRecognizer {

    private final JoinCommandRecognizerContext stateMachine =
            new JoinCommandRecognizerContext(this);
    private boolean isAcceptable = false;
    private StringBuilder relation = null;
    private final LinkedList<String> result = new LinkedList<>();

    public List<String> check(String string)
    {
        stateMachine.reset();

        char current;
        int length = string.length();
        for (int iterator = 0; iterator < length; iterator++) {
            current = string.charAt(iterator);

            boolean isSymbol = ((('a' <= current) && (current <= 'z')) ||
                    (('A' <= current) && (current <= 'Z')) ||
                    (current == '.') || (current == '_'));
            if (isSymbol) {
                stateMachine.symbol(current);
                continue;
            }

            boolean isDigit = (('0' <= current) && (current <= '9'));
            if (isDigit) {
                stateMachine.digit(current);
                continue;
            }

            boolean isWhitespace = ((current == ' ') || (current == '\t'));
            if (isWhitespace) {
                stateMachine.whitespace();
                continue;
            }

            stateMachine.other();
        }

        stateMachine.EOS();

        load();

        if (isAcceptable) {
            return result;
        }

        return null;
    }

    public void accept() {
        isAcceptable = true;
    }

    public void reject() {
        isAcceptable = false;
    }

    public void save(char symbol) {
        boolean isNull = (relation == null);
        if (isNull) {
            relation = new StringBuilder();
        }

        relation.append(symbol);

    }

    public void load() {
        if (relation != null) {
            result.add(relation.toString().toLowerCase());
            relation = null;
        }
    }

    public void clear() {
        isAcceptable = false;
        relation = null;
        result.clear();
    }

}
