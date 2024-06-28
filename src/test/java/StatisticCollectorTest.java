import org.apache.tools.ant.filters.StringInputStream;
import org.junit.Assert;
import org.junit.Test;
import recognizers.regex.Regex;

import java.io.ByteArrayOutputStream;

public class StatisticCollectorTest extends Assert {

    @Test
    public void testIncorrectInputStrings() {
        StringInputStream inputStream = new StringInputStream(
                """
                         create a (a b)
                         create a
                         a join""");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        StatisticCollector collector =
                new StatisticCollector(inputStream, outputStream, errorStream, new Regex());

        collector.start();

        String expectedOutputStream = "";
        String expectedErrorStream = """
                                        !!!Incorrect string!!!
                                        !!!Incorrect string!!!
                                        !!!Incorrect string!!!
                                        """;

        assertEquals(expectedOutputStream, outputStream.toString());
        assertEquals(expectedErrorStream, errorStream.toString());
    }

    @Test
    public void testRelationDoesNotExist() {
        StringInputStream inputStream = new StringInputStream(
                """
                         a
                         a join b
                         create a (c)
                         a join b""");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        StatisticCollector collector =
                new StatisticCollector(inputStream, outputStream, errorStream, new Regex());

        collector.start();

        String expectedOutputStream = "";
        String expectedErrorStream = """
                                        !!!Relation (a) does not exit!!!
                                        !!!Relation (a) does not exit!!!
                                        !!!Relation (b) does not exit!!!
                                        """;

        assertEquals(expectedOutputStream, outputStream.toString());
        assertEquals(expectedErrorStream, errorStream.toString());
    }

    @Test
    public void testCorrectInputString() {
        StringInputStream inputStream = new StringInputStream(
                """
                         create a (c, d)
                         create a (f)
                         a
                         create e (h, j)
                         a join e
                         create ab (cd, efg, hjk)
                         create op (zx, efg, cd)
                         ab
                         op
                         ab join op""");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        StatisticCollector collector =
                new StatisticCollector(inputStream, outputStream, errorStream, new Regex());

        collector.start();

        String expectedErrorStream = "";
        String expectedOutputStream = """
                                        c d f
                                        c d f h j
                                        cd efg hjk
                                        cd efg zx
                                        ab.cd op.cd ab.efg op.efg hjk zx
                                        """;

        assertEquals(expectedOutputStream, outputStream.toString());
        assertEquals(expectedErrorStream, errorStream.toString());
    }


}
