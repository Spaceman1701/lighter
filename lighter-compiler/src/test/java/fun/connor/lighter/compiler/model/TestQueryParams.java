package fun.connor.lighter.compiler.model;

import org.junit.Assert;
import org.junit.Test;

public class TestQueryParams {

    private static final String[] example1 =
            {"foo", "foo:Bar", "foo_bar:fooBar"};

    private static final String[] malformed1 =
            {"foo:"};

    private static final String[] malformed2 =
            {":foo"};

    private static final String[] malformed3 =
            {"foo:bar:2"};

    @Test
    public void testParseNoException() {
        QueryParams q = new QueryParams(example1);
        Assert.assertNotNull(q);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithExceptionMalformed1() {
        QueryParams q = new QueryParams(malformed1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithExceptionMalformed2() {
        QueryParams q = new QueryParams(malformed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithExceptionMalformed3() {
        QueryParams q = new QueryParams(malformed3);
    }
}
