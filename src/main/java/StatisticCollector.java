import recognizers.Recognizer;
import recognizers.Recognizer.Command;
import recognizers.Recognizer.Pair;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class StatisticCollector {

    private final HashMap<String, Set<String>> data;

    private final Scanner inputStream;
    private final PrintStream outputStream;
    private final PrintStream errorStream;
    private final Recognizer recognizer;

    public StatisticCollector(InputStream inputStream, OutputStream outputStream,
                              OutputStream errorStream, Recognizer recognizer) {
        this.inputStream = new Scanner(inputStream);
        this.outputStream = new PrintStream(outputStream);
        this.errorStream = new PrintStream(errorStream);
        this.recognizer = recognizer;
        this.data = new HashMap<>();
    }

    public void start() {
        while (inputStream.hasNextLine()) {
            String input = inputStream.nextLine();
            Pair result = recognizer.check(input);

            boolean isIncorrectString = (result == null);
            if (isIncorrectString) {
                errorStream.print("!!!Incorrect string!!!\n");
                continue;
            }

            boolean isCreateCommand = (result.command() == Command.CREATE);
            if (isCreateCommand) {
                String relation = result.relations().getFirst();
                result.relations().removeFirst();

                boolean isRelationExist = data.containsKey(relation);
                if (isRelationExist) {
                    data.get(relation).addAll(result.relations());
                } else {
                    data.put(relation, new HashSet <>(result.relations()));
                }

            } else {
                boolean relationsSizeIsOne = (result.relations().size() == 1);
                if (relationsSizeIsOne) {
                    String relation = result.relations().getFirst();

                    boolean isRelationNotExist = !data.containsKey(relation);
                    if (isRelationNotExist) {
                        errorStream.print("!!!Relation (".concat(relation).concat(") does not exit!!!\n"));
                        continue;
                    }

                    String output = data.get(relation).stream()
                            .reduce((out, element) -> out.concat(" ").concat(element)).get();

                    outputStream.print(output.concat("\n"));

                } else {
                    String relation1 = result.relations().getFirst();
                    String relation2 = result.relations().getLast();

                    boolean isRelation1NotExist = !data.containsKey(relation1);
                    if (isRelation1NotExist) {
                        errorStream.print("!!!Relation (".concat(relation1).concat(") does not exit!!!\n"));
                        continue;
                    }

                    boolean isRelation2NotExist = !data.containsKey(relation2);
                    if (isRelation2NotExist) {
                        errorStream.print("!!!Relation (".concat(relation2).concat(") does not exit!!!\n"));
                        continue;
                    }

                    Set<String> attributes1 = data.get(relation1);
                    Set<String> attributes2 = data.get(relation2);

                    String firstAttribute;
                    boolean isFirstAttributeExist;
                    {
                        firstAttribute = attributes1.iterator().next();

                        isFirstAttributeExist = (attributes2.contains(firstAttribute));
                        if (isFirstAttributeExist) {

                            outputStream.print(relation1.concat(".").concat(firstAttribute)
                                    .concat(" ").concat(relation2).concat(".").concat(firstAttribute));

                            attributes2.remove(firstAttribute);
                        } else {
                            outputStream.print(firstAttribute);
                        }
                        attributes1.remove(firstAttribute);
                    }

                    for (String attribute : attributes1) {
                        outputStream.print(" ");

                        boolean isAttributeExist = (attributes2.contains(attribute));
                        if (isAttributeExist) {
                            outputStream.print(relation1.concat(".").concat(attribute)
                                    .concat(" ").concat(relation2).concat(".").concat(attribute));
                        } else {
                            outputStream.print(attribute);
                        }

                    }

                    for (String attribute: attributes2) {
                        boolean isAttributeNotExist = !(attributes1.contains(attribute));
                        if (isAttributeNotExist) {
                            outputStream.print(" ");
                            outputStream.print(attribute);
                        }
                    }

                    attributes1.add(firstAttribute);
                    if (isFirstAttributeExist) {
                        attributes2.add(firstAttribute);
                    }

                    outputStream.print("\n");

                }
            }

        }
    }

}
