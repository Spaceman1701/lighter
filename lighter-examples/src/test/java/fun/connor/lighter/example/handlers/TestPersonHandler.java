package fun.connor.lighter.example.handlers;

import fun.connor.lighter.example.domain.Name;
import fun.connor.lighter.example.domain.Person;
import fun.connor.lighter.example.persistance.InMemoryPersonRepo;
import fun.connor.lighter.handler.Request;
import fun.connor.lighter.response.Response;
import fun.connor.lighter.testutils.TestRequest;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class TestPersonHandler {

    @Test
    public void testCreatePerson() {

        PersonHandler handler = new PersonHandler(new InMemoryPersonRepo());

        Request request = new TestRequest();


        Response<Person> response  =
                handler.createPerson("David", "Hume", Optional.of(false), request);


        assertEquals(response.getStatus(), 201);
        assertEquals(response.getContent(), new Person(new Name("David", "Hume"), false));
    }

    @Test
    public void testSayHello() {
        PersonHandler handler = new PersonHandler(new InMemoryPersonRepo());

        Request request = new TestRequest();

        Response<Person> response  =
                handler.createPerson("David", "Hume", Optional.of(false), request);


        Response<Map<String, String>> response1 = handler.sayHello("David", "Hume", request);

        assertEquals(response1.getStatus(), 200);
        assertEquals(response1.getContent().get("words"), "Hello, David!");
    }
}
