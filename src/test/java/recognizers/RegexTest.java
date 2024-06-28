package recognizers;

import static recognizers.Recognizer.Pair;
import org.junit.Assert;
import org.junit.Test;
import recognizers.regex.Regex;

import java.util.ArrayList;
import java.util.List;

public class RegexTest extends Assert {

    private final Regex regex = new Regex();

    @Test
    public void testIncorrectCreateCommand() {
        String input = "create a";
        Pair result = regex.check(input);

        assertNull(result);

        input = "create %";
        result = regex.check(input);

        assertNull(result);

        input = "create a (1sd)";
        result = regex.check(input);

        assertNull(result);

        input = "create 1ab (b)";
        result = regex.check(input);

        assertNull(result);

        input = "create a b";
        result = regex.check(input);

        assertNull(result);

        input = "create a (b c";
        result = regex.check(input);

        assertNull(result);

        input = "create a b)";
        result = regex.check(input);

        assertNull(result);

        input = "create a (b c)";
        result = regex.check(input);

        assertNull(result);

        input = "create ()";
        result = regex.check(input);

        assertNull(result);
    }

    @Test
    public void testIncorrectJoinCommand() {
        String input = "1sd";
        Pair result = regex.check(input);

        assertNull(result);

        input = "a d";
        result = regex.check(input);

        assertNull(result);

        input = "a join";
        result = regex.check(input);

        assertNull(result);

        input = "a join d c";
        result = regex.check(input);

        assertNull(result);
    }

    @Test
    public void testCreateCommandWithOneAttribute() {
        String input = "create a (b)";
        Pair result = regex.check(input);

        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");

        assertNotNull(result);
        assertEquals(Recognizer.Command.CREATE, result.command());
        assertEquals(expected, result.relations());
    }

    @Test
    public void testCreateCommandWithMoreThenOneAttribute() {
        String input = "create a (b, c, d)";
        Pair result = regex.check(input);

        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");
        expected.add("c");
        expected.add("d");

        assertNotNull(result);
        assertEquals(Recognizer.Command.CREATE, result.command());
        assertEquals(expected, result.relations());
    }

    @Test
    public void testCreateCommandWithComplexStructure() {
        String input = "          create           aBc    (   cba,       ef,     g1h)        ";
        Pair result = regex.check(input);

        List<String> expected = new ArrayList<>();
        expected.add("abc");
        expected.add("cba");
        expected.add("ef");
        expected.add("g1h");

        assertNotNull(result);
        assertEquals(Recognizer.Command.CREATE, result.command());
        assertEquals(expected, result.relations());
    }

    @Test
    public void testJoinCommandWithOneRelation() {
        String input = "a";
        Pair result = regex.check(input);

        List<String> expected = new ArrayList<>();
        expected.add("a");

        assertNotNull(result);
        assertEquals(Recognizer.Command.JOIN, result.command());
        assertEquals(expected, result.relations());
    }

    @Test
    public void testJoinCommandWithTwoRelation() {
        String input = "a join b";
        Pair result = regex.check(input);

        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");

        assertNotNull(result);
        assertEquals(Recognizer.Command.JOIN, result.command());
        assertEquals(expected, result.relations());
    }

    @Test
    public void testJoinCommandWithComplexStructure() {
        String input = "            aBc    join            c1ba    ";
        Pair result = regex.check(input);

        List<String> expected = new ArrayList<>();
        expected.add("abc");
        expected.add("c1ba");

        assertNotNull(result);
        assertEquals(Recognizer.Command.JOIN, result.command());
        assertEquals(expected, result.relations());
    }

}
