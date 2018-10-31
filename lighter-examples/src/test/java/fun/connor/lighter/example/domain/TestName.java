package fun.connor.lighter.example.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestName {

    @Test
    public void testEquals() {
        Name n1 = new Name("David", "Bowie");
        Name n2 = new Name("David", "Bowie");

        assertEquals(n1, n2);
        assertEquals(n2, n1);
    }

    @Test
    public void testHashCode() {
        Name n1 = new Name("David", "Bowie");
        Name n2 = new Name("David", "Bowie");

        assertEquals(n1.hashCode(), n2.hashCode());
    }
}
