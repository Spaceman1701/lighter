package fun.connor.lighter.example.handlers;

import fun.connor.lighter.declarative.*;
import fun.connor.lighter.example.domain.Name;
import fun.connor.lighter.example.domain.Person;
import fun.connor.lighter.example.domain.Subject;
import fun.connor.lighter.example.persistance.PersonRepository;
import fun.connor.lighter.handler.Request;
import fun.connor.lighter.handler.Response;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ResourceController("/person")
public class PersonHandler {

    private PersonRepository repository;

    @Inject
    public PersonHandler(PersonRepository repository) {
        this.repository = repository;
    }


    @Post @QueryParams({"first_name:firstName", "last_name:lastName", "use_full_name:useFullName"})
    public Response<Person> createPerson
            (String firstName, String lastName, Optional<Boolean> useFullName, Request request) {
        boolean prefersFirstName = useFullName.orElse(false);

        Person person = new Person(new Name(firstName, lastName), prefersFirstName);
        System.out.println("created: " + person.getName());
        repository.createPerson(person);

        return Response.<Person>builder()
                .content(person)
                .status(201)
                .build();
    }

    @Get("/say_hello/{firstName}/{lastName}")
    public Response<Map<String, String>> sayHello
            (String firstName, String lastName, Request request) {
        Name name = new Name(firstName, lastName);
        System.out.println("Saying hello to: " + name);
        Person person = repository.readPerson(name);

        String result = "Hello, " + person.getPreferredName() + "!";
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("words", result);
        return Response.<Map<String, String>>builder()
                .content(resultMap)
                .status(200)
                .build();

    }

    @Put
    public Response<Person> updatePerson(@Body Person person, Subject subject) {
        if (!subject.isAdmin()) {
           return Response.<Person>builder()
                   .content(null)
                   .status(400)
                   .build();
        }

        repository.updatePerson(person);
        return Response.<Person>builder()
                .content(person)
                .status(200)
                .build();
    }

    @Get("/all")
    public Response<List<Person>> getAll() {
        List<Person> people = repository.getAllPeople();
        return Response.<List<Person>>builder()
                .content(people)
                .status(200)
                .build();
    }
}
