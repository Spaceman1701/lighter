package fun.connor.lighter.example.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestPerson {

    @Test
    public void testGetPreferredName() {
        Person p = new Person(new Name("Alexander", "Hamilton"), false);
        assertEquals(p.getPreferredName(), "Alexander");

        Person p2 = new Person(new Name("Alexander", "Hamilton"), true);
        assertEquals(p2.getPreferredName(), "Alexander Hamilton");
    }
}
