package fun.connor.lighter.compiler.model;

import org.junit.Assert;
import org.junit.Test;

public class TestRoute {

    private static final String good1 = "/foo/bar/foobar/foo";
    private static final String good2 = "/foo/{bar}/bar";
    private static final String good3 = "/foo/{another}/bar";
    private static final String good4 = "/foo/*";
    private static final String good5 = "/foo/bar/";

    private static final String bad2 = "/{foo}/{foo}/bar";
    private static final String bad3 = "/foo/**";


    @Test
    public void testConstructorGood1() {
        Route r = new Route(good1);
        Assert.assertFalse(r.hasTrailingSlash());
    }

    @Test
    public void testConstructorGood2() {
        Route r = new Route(good2);
        Assert.assertFalse(r.hasTrailingSlash());
    }

    @Test
    public void testConstructorGood3() {
        Route r = new Route(good3);
        Assert.assertFalse(r.hasTrailingSlash());
    }

    @Test
    public void testConstructorGood4() {
        Route r = new Route(good4);
        Assert.assertFalse(r.hasTrailingSlash());
    }

    @Test
    public void testConstructorGood5() {
        Route r = new Route(good5);
        Assert.assertTrue(r.hasTrailingSlash());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorBad2() {
        Route r = new Route(bad2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorBad3() {
        Route r = new Route(bad3);
    }

    @Test
    public void testAppend() {
        Route r1 = new Route(good1);
        Route r2 = new Route(good2);

        Route appended = r1.append(r2);

        int r1Spec = r1.getSpecificity();
        int r2Spec = r2.getSpecificity();
        int expectedSpec = r1Spec + r2Spec;

        Assert.assertEquals(expectedSpec, appended.getSpecificity());
        Assert.assertFalse(appended.hasTrailingSlash());
    }

    @Test
    public void testGetSpecificity() {
        Route r1 = new Route(good1);
        Assert.assertEquals(4, r1.getSpecificity());

        Route r2 = new Route(good2);
        Assert.assertEquals(2, r2.getSpecificity());

        Route r3 = new Route(good3);
        Assert.assertEquals(2, r3.getSpecificity());

        Route r4 = new Route(good4);
        Assert.assertEquals(1, r4.getSpecificity());

        Route r5 = new Route(good5);
        Assert.assertEquals(2, r5.getSpecificity());
    }

    //TODO: implement equals
//    @Test
//    public void testEquals() {
//        Route r1a = new Route(good1);
//        Route r1b = new Route(good1);
//
//        Assert.assertEquals(r1a, r1b);
//        Assert.assertEquals(r1b, r1a);
//
//        Route r2 = new Route(good2);
//
//        Assert.assertNotEquals(r1a, r2);
//        Assert.assertNotEquals(r2, r1a);
//
//        Route r3 = new Route(good3);
//        //route r3 and r2 are SEMANTICALLY equal but not actually equal
//        Assert.assertNotEquals(r3, r2);
//        Assert.assertNotEquals(r2, r3);
//    }

    @Test
    public void testCaptures() {
        Route r1a = new Route(good1);
        Route r1b = new Route(good1);
        Route r2 = new Route(good2);
        Route r3 = new Route(good3);

        Assert.assertTrue(r1a.captures(r1b));
        Assert.assertTrue(r1b.captures(r1a));

        Assert.assertFalse(r2.captures(r1a));
        Assert.assertTrue(r2.captures(r3));
    }


    @Test
    public void testGetParams() {
        Route r1 = new Route(good1);

        Assert.assertTrue(r1.getParams().isEmpty());

        Route r2 = new Route(good2);
        Assert.assertEquals(1, r2.getParams().size());
        Assert.assertTrue(r2.getParams().containsKey("bar"));

        Route r3 = new Route(good3);
        Assert.assertEquals(1, r3.getParams().size());
        Assert.assertTrue(  r3.getParams().containsKey("another"));
    }

}
