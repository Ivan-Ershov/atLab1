import recognizers.Recognizer;
import recognizers.flex.Flex;
import recognizers.regex.Regex;
import recognizers.smc.Smc;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        try {
            Scanner in = new Scanner(System.in);

            System.out.println("Please enter name of recognizer realization (regex, smc, flex): ");
            Recognizer recognizer;
            String input = in.nextLine();
            switch (input) {
                case "regex":
                    recognizer = new Regex();
                    break;
                case "smc":
                    recognizer = new Smc();
                    break;
                case "flex":
                    recognizer = new Flex();
                    break;
                default:
                    System.err.println("!!!Error: incorrect name of realization!!!");
                    return;
            }

            StatisticCollector collector =
                    new StatisticCollector(System.in, System.out, System.err, recognizer);

            collector.start();

        } catch (RuntimeException ex) {
            ex.printStackTrace(System.err);
        }

    }
}
