package timing;

import recognizers.Recognizer;
import recognizers.flex.Flex;
import recognizers.regex.Regex;
import recognizers.smc.Smc;

public class TimeTable {
    public static void main(String[] args) {
        StringsGenerator generator = new StringsGenerator();
        String[] messages = {"Regex: ", "Smc: ", "Flex: "};
        Recognizer[] recognizers = {new Regex(), new Smc(), new Flex()};

        int length = 50;
        for (int step = 0; step < 25; step++) {

            String[] strings = new String[30];
            for (int iteration = 0; iteration < 10; iteration++) {
                strings[iteration] = generator.getString(StringsGenerator.Pattern.INCORRECT, length);
            }
            for (int iteration = 10; iteration < 20; iteration++) {
                strings[iteration] = generator.getString(StringsGenerator.Pattern.JOIN, length);
            }
            for (int iteration = 20; iteration < 30; iteration++) {
                strings[iteration] = generator.getString(StringsGenerator.Pattern.CREATE, length);
            }

            for (int number = 0; number < 3; number++) {
                long start = System.currentTimeMillis();
                for (int iteration = 0; iteration < 30; iteration++) {
                    recognizers[number].check(strings[iteration]);
                }
                long end = System.currentTimeMillis();

                System.out.print(messages[number]);
                System.out.print("Step: ");
                System.out.print(step);
                System.out.print(" ");
                System.out.print("Length: ");
                System.out.print(length);
                System.out.print(" ");
                System.out.print("Time: ");
                System.out.println((double) (end - start) / 30);

            }

            length = length * 2;
        }


    }
}
