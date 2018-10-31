package fun.connor.lighter.example.persistance;

import fun.connor.lighter.example.domain.Name;
import fun.connor.lighter.example.domain.Person;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestInMemoryPersonRepo {

    @Test
    public void testCreateAndRead() {
        InMemoryPersonRepo repo = new InMemoryPersonRepo();
        Person testPerson = new Person(new Name("Immanuel", "Kant"), true);

        repo.createPerson(testPerson);

        Person got = repo.readPerson(testPerson.getName());
        assertNotNull(got);
    }
}
