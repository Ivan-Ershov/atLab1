package recognizers.smc;

import recognizers.Recognizer;

import java.util.List;

public class Smc implements Recognizer {

    private final JoinCommandRecognizer join =
            new JoinCommandRecognizer();

    private final CreateCommandRecognizer create =
            new CreateCommandRecognizer();

    @Override
    public Pair check(String input) {

        List<String> result = create.check(input);
        if (result != null) {
            return new Pair(Command.CREATE, result);
        }

        result = join.check(input);
        if (result != null) {
            return new Pair(Command.JOIN, result);
        }

        return null;
    }

}
