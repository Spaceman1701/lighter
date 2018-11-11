package fun.connor.lighter.compiler;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static fun.connor.lighter.compiler.Combinations.*;

public class TestCombinations {

    @Test
    public void testCombinationsOf() {
        List<Integer> a = Arrays.asList(1, 2, 3);

        List<Pair<Integer>> result = CombinationsOf(a);

        Assert.assertEquals(3, result.size());


        Assert.assertTrue(result.contains(Pair.of(1, 2)));
        Assert.assertTrue(result.contains(Pair.of(1, 3)));
        Assert.assertTrue(result.contains(Pair.of(2, 3)));
    }
}
