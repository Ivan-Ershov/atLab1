package recognizers.flex;

import org.apache.tools.ant.filters.StringInputStream;
import recognizers.Recognizer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Flex implements Recognizer {
    @Override
    public Pair check(String input) {
        try {
            StringInputStream stream = new StringInputStream(input.concat("\n"));
            LexRecognizer lex = new LexRecognizer(new InputStreamReader(stream));

            int pattern = lex.yylex();

            if (pattern == 3) {
                return null;
            }

            LinkedList<String> result = new LinkedList<>();
            stream = new StringInputStream(lex.yytext());
            RelationRecognizer rLex = new RelationRecognizer(new InputStreamReader(stream));

            int index = rLex.yylex();
            while (index != LexRecognizer.YYEOF) {

                result.add(rLex.yytext().toLowerCase());

                index = rLex.yylex();
            }

            if (pattern == 1) {
                result.removeFirst();
                return new Pair(Command.CREATE, result);
            }

            if (pattern == 2) {
                if (result.size() == 3) {
                    result.remove(1);
                }
                return new Pair(Command.JOIN, result);
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return null;
    }
}
