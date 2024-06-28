package recognizers;

import java.util.List;

public interface Recognizer {
     Pair check(String input);

     enum Command {
          CREATE,
          JOIN
     }

     record Pair(Command command, List<String> relations) {}

}
