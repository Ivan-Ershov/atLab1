package recognizers.regex;

import recognizers.Recognizer;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex implements Recognizer {
    private final String RELATION_REGEX = "[a-z._][a-z0-9._]*";
    private final String CREATE_COMMAND_REGEX =
            "\\s*create\\s+(%s)\\s*\\(\\s*((%s)\\s*(\\s*,\\s*(%s))*)\\s*\\)\\s*"
                    .replaceAll("%s", RELATION_REGEX);
    private final String JOIN_COMMAND_REGEX =
            "\\s*(%s)\\s*(\\sjoin\\s+(%s)\\s*)?".replaceAll("%s", RELATION_REGEX);

    private final Pattern createCommand;
    private final Pattern joinCommand;
    private final Pattern relation;

    public Regex() {
        createCommand = Pattern.compile(CREATE_COMMAND_REGEX, Pattern.CASE_INSENSITIVE);
        joinCommand = Pattern.compile(JOIN_COMMAND_REGEX, Pattern.CASE_INSENSITIVE);
        relation = Pattern.compile(RELATION_REGEX, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public Pair check(String input) {

        Matcher createCommandMather = createCommand.matcher(input);

        boolean isCreateCommand = createCommandMather.matches();
        if (isCreateCommand) {
            List<String> relations = new LinkedList<>();
            relations.add(createCommandMather.group(1).toLowerCase());

            String attributes = createCommandMather.group(2);
            Matcher relationMatcher = relation.matcher(attributes);
            while (relationMatcher.find()) {
                relations.add(relationMatcher.group(0).toLowerCase());
            }

            return new Pair(Command.CREATE, relations);
        }

        Matcher joinCommandMather = joinCommand.matcher(input);

        boolean isJoinCommand  = joinCommandMather.matches();
        if (isJoinCommand) {
            List<String> relations = new LinkedList<>();
            relations.add(joinCommandMather.group(1).toLowerCase());

            boolean isNotNull = (joinCommandMather.group(3) != null);
            if (isNotNull) {
                relations.add(joinCommandMather.group(3).toLowerCase());
            }

            return new Pair(Command.JOIN, relations);
        }

        return null;
    }

}
