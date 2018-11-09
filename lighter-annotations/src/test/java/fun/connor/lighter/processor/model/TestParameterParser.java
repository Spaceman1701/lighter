package fun.connor.lighter.processor.model;

import org.junit.Assert;
import org.junit.Test;


public class TestParameterParser {

    @Test
    public void testMappedName() {
        ParameterParser p = new ParameterParser("foo:bar");

        Assert.assertEquals(p.getExposedName(), "foo");
        Assert.assertEquals(p.getNameOnMethod(), "bar");
    }

    @Test
    public void testSingleName() {
        ParameterParser p = new ParameterParser("foo");

        Assert.assertEquals(p.getNameOnMethod(), "foo");
        Assert.assertEquals(p.getExposedName(), "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMalformedNameEmptySecondHalf() {
        ParameterParser p = new ParameterParser("foo:");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMalformedNameEmptyFirstHalf() {
        ParameterParser p = new ParameterParser(":foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMalformedNameTooManyParts() {
        ParameterParser p = new ParameterParser("foo:bar:stuff");
    }
}
