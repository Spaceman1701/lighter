package fun.connor.lighter.processor.model;

import org.junit.Assert;
import org.junit.Test;

public class TestRoutePart {

    @Test
    public void testGetString() {
        RoutePart p = new RoutePart("foo", RoutePart.Kind.NORMAL);
        Assert.assertEquals(p.getString(), "foo");
    }

    @Test
    public void testGetKind() {
        RoutePart p = new RoutePart("foo", RoutePart.Kind.NORMAL);
        Assert.assertEquals(p.getKind(), RoutePart.Kind.NORMAL);
    }
}
