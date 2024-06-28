package recognizers;

import org.junit.Assert;
import org.junit.Test;
import recognizers.flex.Flex;

import java.util.ArrayList;
import java.util.List;

public class FlexTest extends Assert {
    private final Flex flex = new Flex();

    @Test
    public void testIncorrectCreateCommand() {
        String input = "create a";
        Recognizer.Pair result = flex.check(input);

        assertNull(result);

        input = "create %";
        result = flex.check(input);

        assertNull(result);

        input = "create a (1sd)";
        result = flex.check(input);

        assertNull(result);

        input = "create 1ab (b)";
        result = flex.check(input);

        assertNull(result);

        input = "create a b";
        result = flex.check(input);

        assertNull(result);

        input = "create a (b c";
        result = flex.check(input);

        assertNull(result);

        input = "create a b)";
        result = flex.check(input);

        assertNull(result);

        input = "create a (b c)";
        result = flex.check(input);

        assertNull(result);

        input = "create ()";
        result = flex.check(input);

        assertNull(result);
    }

    @Test
    public void testIncorrectJoinCommand() {
        String input = "1sd";
        Recognizer.Pair result = flex.check(input);

        assertNull(result);

        input = "a d";
        result = flex.check(input);

        assertNull(result);

        input = "a join";
        result = flex.check(input);

        assertNull(result);

        input = "a join d c";
        result = flex.check(input);

        assertNull(result);
    }

    @Test
    public void testCreateCommandWithOneAttribute() {
        String input = "create a (b)";
        Recognizer.Pair result = flex.check(input);

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
        Recognizer.Pair result = flex.check(input);

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
        Recognizer.Pair result = flex.check(input);

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
        Recognizer.Pair result = flex.check(input);

        List<String> expected = new ArrayList<>();
        expected.add("a");

        assertNotNull(result);
        assertEquals(Recognizer.Command.JOIN, result.command());
        assertEquals(expected, result.relations());
    }

    @Test
    public void testJoinCommandWithTwoRelation() {
        String input = "a join b";
        Recognizer.Pair result = flex.check(input);

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
        Recognizer.Pair result = flex.check(input);

        List<String> expected = new ArrayList<>();
        expected.add("abc");
        expected.add("c1ba");

        assertNotNull(result);
        assertEquals(Recognizer.Command.JOIN, result.command());
        assertEquals(expected, result.relations());
    }

}
